package com.example.contactdeleter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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





}


