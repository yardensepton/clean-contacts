package com.example.contactdeleter;

import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

public class ContactsObserver extends ContentObserver {

    private final Runnable onContactsChanged;

    public ContactsObserver(Handler handler, Runnable onContactsChanged) {
        super(handler);
        this.onContactsChanged = onContactsChanged;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.d("ContactsObserver", "Contacts data has changed.");
        onContactsChanged.run(); // Notify the app of the change
    }
}
