////package com.example.contactdeleter;
////
////import android.Manifest;
////import android.content.ContentResolver;
////import android.content.pm.PackageManager;
////import android.database.Cursor;
////import android.graphics.Color;
////import android.os.Bundle;
////import android.provider.ContactsContract;
////import android.view.View;
////
////import androidx.activity.EdgeToEdge;
////import androidx.annotation.NonNull;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.core.app.ActivityCompat;
////import androidx.core.content.ContextCompat;
////
////import java.util.ArrayList;
////import java.util.List;
////
////import com.example.greatdialogs.ButtonNames;
////import com.example.greatdialogs.DialogType;
////import com.example.greatdialogs.GreatDialog;
////
////public class MainActivity extends AppCompatActivity {
////    private View listView;
////    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 101;
////    private ContentResolver contentResolver;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        EdgeToEdge.enable(this);
////        setContentView(R.layout.activity_main);
////        contentResolver = getContentResolver();
////// Check if permission is already granted
////        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
////            // Request the permission
////            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
////        } else {
////            // Permission already granted, load contacts
////            loadContacts();
////        }
////
////    }
////
////
////    @Override
////    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
////
////        // Check if the request code matches the READ_CONTACTS permission request
////        if (requestCode == 101) {
////
////            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                // If permission is granted, retrieve contacts
////                ContactHelper.getContactCursor(contentResolver, "");
////            } else {
////                GreatDialog dialog = new GreatDialog(MainActivity.this, DialogType.ERROR);
////                dialog.setTitle("ERROR").setVibrate(true).setButtonVisibility(ButtonNames.OK, false).setButtonVisibility(ButtonNames.CANCEL, true).setMessage("In order to use this app you need to allow contacts permission!").setCancelButtonText("OK").setTitleColor(Color.BLACK).setMsgColor(Color.BLACK).show();
////            }
////        }
////    }
////
////    // Method to retrieve contacts and populate the provided lists with names and numbers
////
////}
//
//package com.example.contactdeleter;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.FragmentTransaction;
//
//import com.example.greatdialogs.ButtonNames;
//import com.example.greatdialogs.DialogType;
//import com.example.greatdialogs.GreatDialog;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity {
//
//
//    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 101;
//    private ContentResolver contentResolver;
//    private final String[] options = {"Names Duplications", "Numbers Duplications"};
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//
//        contentResolver = getContentResolver();
//       boolean res = checkPermissions();
//        if (res ){
//            int namesDuplicationCount = getDuplicatedCount(contentResolver, "name");
//            int numbersDuplicationCount = getDuplicatedCount(contentResolver, "phone");
//            ListView listView = findViewById(R.id.listViewOptions);
//            List<String> displayOptions = new ArrayList<>();
//            displayOptions.add("Names Duplications: " + namesDuplicationCount);
//            displayOptions.add("Numbers Duplications: " + numbersDuplicationCount);
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayOptions);
//            listView.setAdapter(adapter);
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    String selectedOption = options[position];
//                    String type = "name";
//                    if ("Numbers Duplications".equals(selectedOption)) {
//                        type = "phone";
//                    }
//                    Intent intent = new Intent(MainActivity.this, DuplicationsActivity.class);
//                    intent.putExtra("type", type);
//                    intent.putExtra("list", getDuplicatedList(contentResolver, type));
//                    startActivity(intent);
//                }
//            });
//        }
//
//
//
//
//
//
////
////        deleteContactButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if (checkPermissions()) {
////                    deleteContact("1234567890"); // Replace with the actual number you want to delete
////                }
////            }
////        });
//      // Initialize the ListView
//
//        // Check permissions on start
//
//    }
//
////    private void setAdapter(List<String> name, List<String> number) {
////        ContactsAdapter contactsAdapter = new ContactsAdapter(this, name, number);
////        listView.setAdapter(contactsAdapter);
////    }
//
//    private boolean checkPermissions() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_CONTACTS},
//                    PERMISSIONS_REQUEST_READ_CONTACTS);
//            return false;
//        }
//        return true;
//    }
//
//
//    private ArrayList<String> getDuplicatedList(ContentResolver contentResolver, String type) {
//        ArrayList<String> duplicatedItems = new ArrayList<>();
//        Cursor cursor = null;
//        if ("name".equals(type)) {
//            cursor = ContactHelper.getDuplicatedContactsByNameCursor(contentResolver,null);
//        } else if ("phone".equals(type)) {
//            cursor = ContactHelper.getDuplicatedContactsByPhoneNumberCursor(contentResolver);
//        }
//
//        if (cursor != null) {
//            int index = "name".equals(type) ?
//                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME) :
//                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//            while (cursor.moveToNext()) {
//                duplicatedItems.add(cursor.getString(index));
//            }
//            cursor.close();
//        }
//        return duplicatedItems;
//    }
//
//    private int getDuplicatedCount(ContentResolver contentResolver, String type) {
//        Cursor cursor = null;
//        if ("name".equals(type)) {
//            cursor = ContactHelper.getDuplicatedContactsByNameCursor(contentResolver,null);
//        } else if ("phone".equals(type)) {
//            cursor = ContactHelper.getDuplicatedContactsByPhoneNumberCursor(contentResolver);
//        }
//
//        int count = 0;
//        if (cursor != null) {
//            count = cursor.getCount();
//            cursor.close();
//        }
//        return count;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                fetchContacts();
//            } else {
//                showErrorDialog();
//            }
//        }
//    }
//
//
//
//    private void fetchContacts() {
//        Cursor cursor = null;
//        cursor = ContactHelper.getDuplicatedContactsByNameCursor(contentResolver,null);
////        ArrayList<String> names = new ArrayList<>();
////        ArrayList<String> numbers = new ArrayList<>();
////        if (cursor != null) {
////            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
////            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
////            while (cursor.moveToNext()) {
////                names.add(cursor.getString(nameIndex));
////                numbers.add(cursor.getString(numberIndex));
////            }
////            cursor.close();
//
//        }
//
////        ContactsFragment contactsFragment = ContactsFragment.newInstance(names, numbers);
////        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
////        transaction.replace(R.id.fragment_container, contactsFragment); // Assume you have a FrameLayout with id fragment_container
////        transaction.commit();
//
//
//
//    private void showErrorDialog() {
//        GreatDialog dialog = new GreatDialog(MainActivity.this, DialogType.ERROR);
//        dialog.setTitle("ERROR")
//                .setVibrate(true)
//                .setButtonVisibility(ButtonNames.OK, false)
//                .setButtonVisibility(ButtonNames.CANCEL, true)
//                .setMessage("In order to use this app you need to allow contacts permission!")
//                .setCancelButtonText("OK")
//                .setTitleColor(Color.BLACK)
//                .setMsgColor(Color.BLACK)
//                .show();
//    }
//}
package com.example.contactdeleter;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 101;
    private ContentResolver contentResolver;
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
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            return false;
        }
        return true;
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = options[position];
                String type = "name";
                if ("Numbers Duplications".equals(selectedOption)) {
                    type = "phone";
                }
                Intent intent = new Intent(MainActivity.this, DuplicationsActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("list", getDuplicatedList(contentResolver, type));
                startActivity(intent);
            }
        });
    }

    private ArrayList<String> getDuplicatedList(ContentResolver contentResolver, String type) {
        ArrayList<String> duplicatedItems = new ArrayList<>();
        Cursor cursor = null;
        if ("name".equals(type)) {
            cursor = ContactHelper.getDuplicatedContactsByNameCursor(contentResolver, null);
        } else if ("phone".equals(type)) {
            cursor = ContactHelper.getDuplicatedContactsByPhoneNumberCursor(contentResolver);
        }

        if (cursor != null) {
            int index = "name".equals(type) ?
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME) :
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            while (cursor.moveToNext()) {
                duplicatedItems.add(cursor.getString(index));
            }
            cursor.close();
        }
        return duplicatedItems;
    }

    private int getDuplicatedCount(ContentResolver contentResolver, String type) {
        Cursor cursor = null;
        if ("name".equals(type)) {
            cursor = ContactHelper.getDuplicatedContactsByNameCursor(contentResolver, null);
        } else if ("phone".equals(type)) {
            cursor = ContactHelper.getDuplicatedContactsByPhoneNumberCursor(contentResolver);
        }

        int count = 0;
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        return count/2;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupListView();
            } else {
                showErrorDialog();
            }
        }
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
