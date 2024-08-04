package com.example.contactdeleter;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

//public class ContactsAdapter extends ArrayAdapter<String> {
//    private final Activity context;
//    private final ArrayList<Contact> contacts;
//    public ContactsAdapter(Activity context, ArrayList<Contact>contacts) {
//        super(context, R.layout.contact_ui);
//        this.context=context;
//        this.contacts=contacts;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        LayoutInflater inflater=context.getLayoutInflater();
//        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView=inflater.inflate(R.layout.contact_ui, null,true);
//        TextView Textname = rowView.findViewById(R.id.name);
//        TextView Textph = rowView.findViewById(R.id.ph);
//        Textname.setText(contacts.get(position).getName());
//        Textph.setText(contacts.get(position).getNumber());
//        return rowView;
//    }
//}
public class ContactsAdapter extends ArrayAdapter<Contact> {
    private final Activity context;
    private final ArrayList<Contact> contacts;

    public ContactsAdapter(Activity context, ArrayList<Contact> contacts) {
        super(context, R.layout.contact_ui, contacts);
        this.context = context;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Reuse the view if possible, otherwise inflate a new one from the layout file
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.contact_ui, parent, false);
        }

        // Get the contact for this position
        Contact contact = contacts.get(position);

        // Find the TextViews in the layout
        TextView nameTextView = rowView.findViewById(R.id.name);
        TextView phoneTextView = rowView.findViewById(R.id.ph);

        // Set the contact's name and phone number in the TextViews
        nameTextView.setText(contact.getName());
        phoneTextView.setText(contact.getNumber());

        return rowView;
    }
}
