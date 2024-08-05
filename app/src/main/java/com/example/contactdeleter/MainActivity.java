package com.example.contactdeleter;

import static com.example.contactdeleter.ContactHelper.getDuplicatedCount;
import static com.example.contactdeleter.ContactHelper.getDuplicatedList;
import android.os.Handler;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.greatdialogs.ButtonNames;
import com.example.greatdialogs.DialogType;
import com.example.greatdialogs.GreatDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ContactsObserver contactsObserver;

    private static final int PERMISSIONS_REQUEST_CODE = 101;
    public static ContentResolver contentResolver;
    private final String[] options = {"Names Duplications", "Numbers Duplications"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        contentResolver = getContentResolver();

        if (checkPermissions()) {
            setupListView();
        }
        Runnable onContactsChanged = this::refreshContacts;
        contactsObserver = new ContactsObserver( new Handler(Looper.getMainLooper()), onContactsChanged);
        contentResolver.registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, contactsObserver);


    }

    private boolean checkPermissions() {
        boolean readPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        boolean writePermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED;

        if (!readPermissionGranted || !writePermissionGranted) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},
                    PERMISSIONS_REQUEST_CODE);
            return false;
        }
        return true;
    }

    private void refreshContacts() {
        // Reload contacts data and update the UI accordingly
        setupListView();
    }


    private void setupListView() {
        int namesDuplicationCount = getDuplicatedCount(contentResolver, "name");
        int numbersDuplicationCount = getDuplicatedCount(contentResolver, "phone");
        ListView listView = findViewById(R.id.listViewOptions);
        List<String> displayOptions = new ArrayList<>();
        displayOptions.add("Names Duplications: " + namesDuplicationCount);
        displayOptions.add("Numbers Duplications: " + numbersDuplicationCount);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayOptions);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedOption = options[position];
            String type = "name";
            if ("Numbers Duplications".equals(selectedOption)) {
                type = "phone";
            }
            Intent intent = new Intent(MainActivity.this, DuplicationsActivity.class);
            intent.putExtra("type", type);
            intent.putParcelableArrayListExtra("contacts", getDuplicatedList(contentResolver, type));
            startActivity(intent);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                setupListView();
            } else {
                showErrorDialog();
            }
        }
    }

    private void showErrorDialog() {
        GreatDialog dialog = new GreatDialog(MainActivity.this, DialogType.ERROR);
        dialog.setTitle("ERROR").setVibrate(true).setButtonVisibility(ButtonNames.OK, true).setButtonVisibility(ButtonNames.CANCEL, false)
                .setMessage("In order to use this app you need to allow contacts permission!").setOKButtonText("OK")
                .setTitleColor(Color.BLACK).setMsgColor(Color.BLACK).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (contactsObserver != null) {
            contentResolver.unregisterContentObserver(contactsObserver);
        }
    }
}
