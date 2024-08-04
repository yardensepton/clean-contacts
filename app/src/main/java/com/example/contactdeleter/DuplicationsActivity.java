package com.example.contactdeleter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DuplicationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duplications);

        ListView listView = findViewById(R.id.listViewDuplications);

        Intent intent = getIntent();
        ArrayList<Contact> contacts = getIntent().getParcelableArrayListExtra("contacts");
        Log.e("err", String.valueOf(contacts));

        ContactsAdapter contactsAdapter = new ContactsAdapter(this, contacts);
        listView.setAdapter(contactsAdapter);

    }

//        if (duplicatedItems == null) {
//            duplicatedItems = new ArrayList<>();
//        }
//
//        String title = "Duplications: ";
//        if ("name".equals(type)) {
//            title += "Names";
//        } else if ("phone".equals(type)) {
//            title += "Numbers";
//        }
//        setTitle(title);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, duplicatedItems);
//        listView.setAdapter(adapter);
//    }



}


