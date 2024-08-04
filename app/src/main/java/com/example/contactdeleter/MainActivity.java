//package com.example.contactdeleter;
//
//import android.Manifest;
//import android.content.ContentResolver;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.view.View;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.example.greatdialogs.ButtonNames;
//import com.example.greatdialogs.DialogType;
//import com.example.greatdialogs.GreatDialog;
//
//public class MainActivity extends AppCompatActivity {
//    private View listView;
//    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 101;
//    private ContentResolver contentResolver;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//        contentResolver = getContentResolver();
//// Check if permission is already granted
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            // Request the permission
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
//        } else {
//            // Permission already granted, load contacts
//            loadContacts();
//        }
//
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        // Check if the request code matches the READ_CONTACTS permission request
//        if (requestCode == 101) {
//
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // If permission is granted, retrieve contacts
//                ContactHelper.getContactCursor(contentResolver, "");
//            } else {
//                GreatDialog dialog = new GreatDialog(MainActivity.this, DialogType.ERROR);
//                dialog.setTitle("ERROR").setVibrate(true).setButtonVisibility(ButtonNames.OK, false).setButtonVisibility(ButtonNames.CANCEL, true).setMessage("In order to use this app you need to allow contacts permission!").setCancelButtonText("OK").setTitleColor(Color.BLACK).setMsgColor(Color.BLACK).show();
//            }
//        }
//    }
//
//    // Method to retrieve contacts and populate the provided lists with names and numbers
//
//}

package com.example.contactdeleter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
     private ListView listView;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 101;
    private ContentResolver contentResolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        contentResolver = getContentResolver();
//        fetchContactsButton = findViewById(R.id.fetchContactsButton);
//        deleteContactButton = findViewById(R.id.deleteContactButton);

        // Set up click listeners
//        fetchContactsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkPermissions()) {
//                    fetchContacts();
//                }
//            }
//        });
//
//        deleteContactButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkPermissions()) {
//                    deleteContact("1234567890"); // Replace with the actual number you want to delete
//                }
//            }
//        });
        listView = findViewById(R.id.list); // Initialize the ListView

        // Check permissions on start
        checkPermissions();
    }

    private void setAdapter(List<String> name, List<String> number) {
        ContactsAdapter contactsAdapter = new ContactsAdapter(this, name, number);
        listView.setAdapter(contactsAdapter);
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            return false;
        }
        fetchContacts();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchContacts();
            } else {
                showErrorDialog();
            }
        }
    }

    private void fetchContacts() {
        Cursor cursor = ContactHelper.getContactCursor(contentResolver, null);
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> numbers = new ArrayList<>();
        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            while (cursor.moveToNext()) {
                names.add(cursor.getString(nameIndex));
                numbers.add(cursor.getString(numberIndex));
            }
            cursor.close();
            setAdapter(names,numbers);
        }
    }

    private void deleteContact(String number) {
        ContactHelper.deleteContact(contentResolver, number);
        Toast.makeText(this, "Contact with number " + number + " deleted.", Toast.LENGTH_SHORT).show();
    }

    private void showErrorDialog() {
        GreatDialog dialog = new GreatDialog(MainActivity.this, DialogType.ERROR);
        dialog.setTitle("ERROR")
                .setVibrate(true)
                .setButtonVisibility(ButtonNames.OK, false)
                .setButtonVisibility(ButtonNames.CANCEL, true)
                .setMessage("In order to use this app you need to allow contacts permission!")
                .setCancelButtonText("OK")
                .setTitleColor(Color.BLACK)
                .setMsgColor(Color.BLACK)
                .show();
    }
}
