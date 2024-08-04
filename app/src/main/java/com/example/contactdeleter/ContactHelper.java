package com.example.contactdeleter;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

public class ContactHelper {
    public static Cursor getContactCursor(ContentResolver contactHelper, String startsWith) {
        String[] projection = { ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER };
        Cursor cur = null;

        try {
            if (startsWith != null && !startsWith.isEmpty()) {
                cur = contactHelper.query (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like \"" + startsWith + "%\"", null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            } else {
                cur = contactHelper.query (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            }
            assert cur != null;
            cur.moveToFirst();
        }
        catch (Exception e) {
            Log.e("ContactHelper", String.valueOf(e));
        }
        return cur;
    }


    public static void deleteContact(ContentResolver contentResolver, String number) {
        long contactId = getContactID(contentResolver, number);
        if (contactId != -1) {
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();
            ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                    .withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", new String[]{String.valueOf(contactId)})
                    .build());
            try {
                contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                Log.e("ContactHelper", String.valueOf(e));
            }
        } else {
            // Handle the case where the contact ID was not found
            Log.e("ContactHelper", "Contact ID not found for number: " + number);
        }
    }

    private static long getContactID(ContentResolver contentResolver, String number) {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = { ContactsContract.PhoneLookup._ID };

        try (Cursor cursor = contentResolver.query(contactUri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int personIDIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup._ID);
                if (personIDIndex != -1) {
                    return cursor.getLong(personIDIndex);
                }
            }
        } catch (Exception e) {
            Log.e("ContactHelper", String.valueOf(e));
        }
        return -1;
    }

}
