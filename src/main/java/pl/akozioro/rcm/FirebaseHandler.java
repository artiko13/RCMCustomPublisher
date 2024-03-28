package pl.akozioro.rcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.akozioro.rcm.model.ClockModel;
import pl.akozioro.rcm.model.GridRowModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FirebaseHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseHandler.class);

    private FirebaseDatabase database;
    private DatabaseReference clockRef;
    private DatabaseReference gridRef;
    private DatabaseReference messagesRef;

    public FirebaseHandler() throws IOException {
        initialize();
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        new FirebaseHandler();
    }

    private void initialize() throws IOException {
        InputStream serviceAccount = getClass().getResourceAsStream("/serviceAccountKey.json");
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://rcmcustompublisher-default-rtdb.europe-west1.firebasedatabase.app")
//                .setDatabaseUrl("https://rcmapp-4137c-default-rtdb.europe-west1.firebasedatabase.app")
                .build();
        FirebaseApp.initializeApp(options);
        database = FirebaseDatabase.getInstance();
        clockRef = database.getReference("clock");
        gridRef = database.getReference("grid");
        messagesRef = database.getReference("messages");
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
        gridRef.setValue(gridRows, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                LOGGER.warn(error.getMessage());
            }
        });
    }
}