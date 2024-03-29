package pl.akozioro.rcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.NodeUtilities;
import com.google.firebase.database.snapshot.PriorityUtilities;
import com.google.firebase.database.utilities.Validation;
import com.google.firebase.database.utilities.encoding.CustomClassMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.akozioro.rcm.model.ClockModel;
import pl.akozioro.rcm.model.GridRowModel;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class FirebaseHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseHandler.class);

    private FirebaseDatabase database;
    private DatabaseReference clockRef;
    private DatabaseReference gridRef;

    public FirebaseHandler(String auth_token) throws IOException {
        initialize(auth_token);
    }

    private void initialize(String auth_token) throws IOException {
        InputStream serviceAccount = getClass().getResourceAsStream("/serviceAccountKey.json");
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .setDatabaseUrl("https://rcmcustompublisher-default-rtdb.europe-west1.firebasedatabase.app")
                .setDatabaseUrl("https://rcmapp-4137c-default-rtdb.europe-west1.firebasedatabase.app")
                .build();
        FirebaseApp.initializeApp(options);
        database = FirebaseDatabase.getInstance();
        clockRef = database.getReference("grids/" + auth_token + "/clock");
        gridRef = database.getReference("grids/" + auth_token + "/grid");
//        clockRef = database.getReference("clock");
//        gridRef = database.getReference("grid");
    }

    public void putClock(ClockModel clock) {
        clockRef.setValue(clock, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                LOGGER.warn(error.getMessage());
            }
        });
    }

    public void putGrid(List<GridRowModel> gridRows) {
        Path path = new Path("grids/exist8/grid");
        Node priority = PriorityUtilities.parsePriority(path, null);
        Object bouncedValue = CustomClassMapper.convertToPlainJavaTypes(gridRows);
        Validation.validateWritableObject(bouncedValue);
        final Node node = NodeUtilities.NodeFromJSON(bouncedValue, priority);
        System.out.println(node.getValue());

        sendData(String.valueOf(node.getValue()), path.toString());

//        gridRef.setValue(gridRows, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError error, DatabaseReference ref) {
//                LOGGER.warn(error.getMessage());
//            }
//        });
    }

    private void sendData(String jsonData, String path) {
        try {
            URL url = new URL("https://rcmapp-4137c-default-rtdb.europe-west1.firebasedatabase.app/.json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.writeBytes(jsonData);
                outputStream.flush();
            }
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            StringBuilder response;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            System.out.println("Response: " + response.toString());
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}