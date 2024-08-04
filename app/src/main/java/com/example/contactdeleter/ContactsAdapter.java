package com.example.contactdeleter;
import static com.example.contactdeleter.ContactHelper.deleteContact;
import static com.example.contactdeleter.MainActivity.contentResolver;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

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
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.contact_ui, parent, false);
        }

        Contact contact = contacts.get(position);

        TextView nameTextView = rowView.findViewById(R.id.name);
        TextView phoneTextView = rowView.findViewById(R.id.ph);

        nameTextView.setText(contact.getName());
        phoneTextView.setText(contact.getNumber());

        rowView.setOnLongClickListener(v -> {
            Toast.makeText(context, "Deleting contact: " + contact.getName(), Toast.LENGTH_SHORT).show();
            deleteContact(contentResolver,contact.getId());
            return true;
        });

        return rowView;
    }
}
