package pl.akozioro.rcm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.Properties;

public class MainApp extends JPanel implements Thread.UncaughtExceptionHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private FirebaseHandler firebaseHandler = new FirebaseHandler();
    private static final String APPLICATION_NAME = "RCM Custom Publisher";
    private JLabel hostLabel;
    private JTextField hostField;
    private String mockValue;
    private JLabel portLabel;
    private JTextField portField;
    private JTextArea statusLabel;
    private JButton startButton;
    private JButton stopButton;
    private String propertiesFile = "RCMCustomPublisher.properties";
    private Task task = null;
    private Properties properties;

    private static Color hexToColor(String hexColor) {
        if (hexColor.length() != 7 || hexColor.charAt(0) != '#') {
            throw new IllegalArgumentException("Nieprawidłowy format kodu koloru");
        }
        int r = Integer.parseInt(hexColor.substring(1, 3), 16);
        int g = Integer.parseInt(hexColor.substring(3, 5), 16);
        int b = Integer.parseInt(hexColor.substring(5, 7), 16);
        return new Color(r, g, b);
    }

    public MainApp() throws IOException {
        setBackground(MainApp.hexToColor("#68A5BB"));
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 0);

        // host label and field
        hostLabel = new JLabel("Host:");
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(30, 30, 0, 0);
        add(hostLabel, c);

        hostField = new JTextField(30);
        hostField.setBackground(hexToColor("#7a97a1"));
        EmptyBorder paddingBorder = new EmptyBorder(5, 5, 5, 5);
        hostField.setBorder(paddingBorder);
//        hostField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(30, 5, 0, 30);
        add(hostField, c);

        // port label and field
        portLabel = new JLabel("Port:");
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(10, 30, 0, 0);
        add(portLabel, c);

        portField = new JTextField(5);
        portField.setBackground(hexToColor("#7a97a1"));
        portField.setBorder(paddingBorder);
        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(10, 5, 0, 30);
        add(portField, c);

        // space
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        add(Box.createVerticalStrut(10), c);
        c.insets = new Insets(10, 30, 0, 0);
        c.gridwidth = 1;

        // start and stop buttons
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.anchor = GridBagConstraints.LINE_END;
        add(startButton, c);

        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(10, 10, 0, 30);
        add(stopButton, c);
        c.gridwidth = 1;
        c.weightx = 0;

        // space
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        add(Box.createVerticalStrut(10), c);
        c.gridwidth = 1;

        // console
        statusLabel = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(statusLabel);
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10, 30, 30, 30);
        add(scrollPane, c);

        init();
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTask();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopTask();
            }
        });
    }

    private void startTask() {
        store();
        statusLabel.setText("");
        String host = hostField.getText();
        int port = Integer.parseInt(portField.getText());
        if (task != null && task.isAlive() && !task.isInterrupted()) {
            statusLabel.append("Previous thread is alive!" + "\n");
            return;
        }
        boolean enableMock = Optional.ofNullable(properties).map(ps -> ps.getProperty("mock")).map(m -> "true".equalsIgnoreCase(m)).orElse(false);
        task = enableMock ? new MockedTask(host, port, firebaseHandler, statusLabel) : new Task(host, port, firebaseHandler, statusLabel);
        task.setUncaughtExceptionHandler(this);
        task.start();
        changeButtons(false);
    }

    private boolean stopTask() {
        statusLabel.append("Stopping..." + "\n");
        if (task != null) {
            try {
                task.stopTask();
                LOGGER.error("After interrupt = " + task.isInterrupted());
            } catch (Exception e) {
                LOGGER.error(e);
                return false;
            }
        }
        changeButtons(true);
        statusLabel.setText("");
        statusLabel.append("Stopped." + "\n");
        return true;
    }

    private void changeButtons(boolean stopped) {
        hostField.setEnabled(stopped);
        portField.setEnabled(stopped);
        startButton.setEnabled(stopped);
        stopButton.setEnabled(!stopped);
    }

    private void init() {
        // odczytanie wartości hosta i portu z pliku "myapp.properties"
        properties = new Properties();
        try {
            InputStream input = new FileInputStream(propertiesFile);
            properties.load(input);
            hostField.setText(properties.getProperty("host"));
            portField.setText(properties.getProperty("port"));
            mockValue = properties.getProperty("mock");
            startTask();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error - app configuration reading: " + e.getMessage());
        }
    }

    private void store() {
        String host = hostField.getText();
        int port = Integer.parseInt(portField.getText());
        // zapisanie wartości hosta i portu do pliku "myapp.properties"
        Properties properties = new Properties();
        properties.setProperty("host", host);
        properties.setProperty("port", Integer.toString(port));
        properties.setProperty("mock", mockValue != null ? mockValue : "false");
        try {
            OutputStream output = new FileOutputStream(propertiesFile);
            properties.store(output, "Ustawienia aplikacji");
        } catch (Exception e) {
            LOGGER.error("Error - app configuration saving: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame(APPLICATION_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new MainApp());
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            statusLabel.append(e.getMessage() + "\n");
            LOGGER.error("Task return an error.", e);
            t.interrupt();
            LOGGER.error("After interrupt.");
            changeButtons(true);
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
    }

}
