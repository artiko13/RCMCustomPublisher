package pl.akozioro.rcm;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.akozioro.rcm.model.ClockModel;
import pl.akozioro.rcm.model.GridRowModel;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Task extends Thread {
    private static final Logger LOGGER = LogManager.getLogger();
    private String eventName;
    private String host;
    private int port;
    private FirebaseHandler firebaseHandler;
    protected JTextArea logTextArea;
    protected boolean stop = false;
    private static final Pattern CLOCK_PATTERN = Pattern.compile(".*CLOCK_UPDATE\\|A=(\\d+)\\|B=(\\d+)\\|C=(\\d+).*");
    private static final Pattern GRID_PATTERN = Pattern.compile("\\|C\\d+=([^\\|]+).*?\\|D\\d+=([^\\|]+).*?G\\d+=([^\\|]+).*?H\\d+=([^\\|]+).*?K\\d+=([^\\|]+).*?N\\d+=([^\\|]+).*?HA\\d+=([^\\|]+)");

    private List<GridRowModel> previouslySendGrid = null;
    private ClockModel previouslySendClock = null;

    public Task(String eventName, String host, int port, FirebaseHandler firebaseHandler, JTextArea logTextArea) {
        this.eventName = eventName;
        this.host = host;
        this.port = port;
        this.firebaseHandler = firebaseHandler;
        this.logTextArea = logTextArea;
    }

    @Override
    public void run() {
        stop = false;
        LOGGER.info("Task started.");
        int i=1;
        while (true) {

            logTextArea.setText("");
            logTextArea.append("Connecting to " + host + ":" + port + " ..." + "\n");

            try (final Socket connection = new Socket(host, port);
                 InputStream in = connection.getInputStream();
                 InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8)) {

                BufferedReader br = new BufferedReader(isr);
                logTextArea.setText("");
                logTextArea.append("Connected to " + host + ":" + port + ". Working ..." + "\n");
                while (true) {
                    if (stop) {
                        logTextArea.append("Stopped." + "\n");
                        LOGGER.error("stopping in task");
                        return;
                    }
                    String line = br.readLine();
                    if (StringUtils.isNotBlank(line)) {
                        line = line.replaceAll("[^\\x20-\\x7E]", "").trim();
                        handleLine(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error(e);
                logTextArea.append("Exception: " + e.getMessage() + "\n");
            }
            long start = System.currentTimeMillis();
            long delay = 30*1000;
            while (System.currentTimeMillis() - start < delay) {
                try {
                    if (stop) {
                        LOGGER.error("stopping in task");
                        return;
                    }
                    else {
                        String logs = logTextArea.getText();
                        if (logs.contains("Reconnect try in ")) {
                            logs = logs.replaceAll("Reconnect\\stry\\sin\\s[^\\.]+\\.", "Reconnect try in " + (int)(delay/1000 - (System.currentTimeMillis() - start) / 1000) + "s.");
                        }
                        else {
                            logs += "Reconnect try in " + (int)(delay/1000 - (System.currentTimeMillis() - start) / 1000) + "s.\n";
                        }
                        logTextArea.setText(logs);
                    }
                    sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.error(e);
                }
            }

        }
    }

    protected void handleLine(String line) throws IOException {
        String command = line.replaceAll(".*\\|Command=([^|]+).*", "$1").trim();
        if ("GRID_UPDATE".equals(command)) {
            handleGrid(line);
        } else if ("CLOCK_UPDATE".equals(command)) {
            handleClock(line);
        }
    }

    private void handleClock(String line) {
        Matcher matcher = CLOCK_PATTERN.matcher(line);
        if (matcher.find()) {
            try {
                ClockModel clock = new ClockModel(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(3)));
                if (!areTheClocks(previouslySendClock, clock)) {
                    previouslySendClock = clock;
                    firebaseHandler.putClock(clock);
                }
            } catch (Exception e) {
                logTextArea.append("Exception: " + e.getMessage() + "\n");
                LOGGER.error(e.getMessage());
            }
        }
    }

    public void stopTask() {
        stop = true;
    }

    private void handleGrid(String line) throws IOException {
        Matcher matcher = GRID_PATTERN.matcher(line);
        List<GridRowModel> grid = new ArrayList<>();
        while (matcher.find()) {
            String timesStr = matcher.group(7);
            List<String> times = Optional.ofNullable(timesStr)
                    .map(s -> s.split("\\s*-\\s*"))
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(t -> t.replaceAll("\\$[a-zA-Z]*", ""))
                    .collect(Collectors.toList());
            grid.add(GridRowModel.builder()
                    .number(matcher.group(6))
                    .count(matcher.group(3))
                    .lastLap(matcher.group(4))
                    .bestLap(matcher.group(5))
                    .times(times).build());
        }
        if (!areTheSameGrids(previouslySendGrid, grid)) {
            previouslySendGrid = grid;
            firebaseHandler.putGrid(grid);
        }
    }

    private boolean areTheSameGrids(List<GridRowModel> first, List<GridRowModel> second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (first.size() != second.size()) {
            return false;
        }
        Iterator<GridRowModel> secondIt = second.iterator();
        for (GridRowModel row : first) {
            if (!Objects.equals(secondIt.next(), row)) {
                return false;
            }
        }
        return true;
    }

    private boolean areTheClocks(ClockModel first, ClockModel second) {
        return Objects.equals(first, second);
    }
}