package com.example.contactdeleter;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ContactHelper {


    public static ArrayList<Contact> getDuplicatedContactsByName(ContentResolver contentResolver, String startsWith) {
        HashMap<String, List<String>> nameToIdsMap = collectIdsForNames(contentResolver, startsWith);
        List<String> duplicateIds = filterDuplicateIds(nameToIdsMap);

        if (duplicateIds.isEmpty()) {
            return new ArrayList<>(); // No duplicates found
        }

        return queryDuplicatedContacts(contentResolver, duplicateIds);
    }

    public static ArrayList<Contact> getDuplicatedContactsByPhoneNumber(ContentResolver contentResolver) {
        HashMap<String, List<String>> numberToIdsMap = collectIdsForPhoneNumbers(contentResolver, null);
        List<String> duplicateNumberIds = filterDuplicateIds(numberToIdsMap);

        if (duplicateNumberIds.isEmpty()) {
            return new ArrayList<>(); // No duplicates found
        }

        return queryDuplicatedContacts(contentResolver, duplicateNumberIds);
    }

    private static ArrayList<Contact> queryDuplicatedContacts(ContentResolver contentResolver, List<String> ids) {
        String selection = ContactsContract.CommonDataKinds.Phone._ID + " IN (" +
                TextUtils.join(",", Collections.nCopies(ids.size(), "?")) + ")";
        String[] selectionArgs = ids.toArray(new String[0]);

        Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                },
                selection,
                selectionArgs,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );

        ArrayList<Contact> duplicatedContacts = new ArrayList<>();
        if (cursor != null) {
            int idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            while (cursor.moveToNext()) {
                String id = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);
                String number = cursor.getString(numberIndex);
                duplicatedContacts.add(new Contact(id, name, number));
            }
            cursor.close();
        }

        return duplicatedContacts;
    }

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


    private static HashMap<String, List<String>> collectIdsForNames(ContentResolver contentResolver, String startsWith) {
        HashMap<String, List<String>> nameToIdsMap = new HashMap<>();
        Cursor cursor = getContactCursor(contentResolver, startsWith);

        if (cursor == null) {
            return nameToIdsMap; // No contacts found
        }

        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);

        while (cursor.moveToNext()) {
            String name = cursor.getString(nameIndex);
            String id = cursor.getString(idIndex);

            nameToIdsMap.computeIfAbsent(name, k -> new ArrayList<>()).add(id);
        }
        cursor.close();

        return nameToIdsMap;
    }

    private static List<String> filterDuplicateIds(HashMap<String, List<String>> nameToIdsMap) {
        List<String> duplicateIds = new ArrayList<>();

        for (List<String> idList : nameToIdsMap.values()) {
            if (idList.size() > 1) { // Only consider names with duplicates
                duplicateIds.addAll(idList);
            }
        }

        return duplicateIds;
    }



    private static HashMap<String, List<String>> collectIdsForPhoneNumbers(ContentResolver contentResolver, String startsWith) {
        HashMap<String, List<String>> numberToIdsMap = new HashMap<>();
        Cursor cursor = getContactCursor(contentResolver, startsWith);

        if (cursor == null) {
            return numberToIdsMap; // No contacts found
        }

        int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);

        while (cursor.moveToNext()) {
            String number = cursor.getString(numberIndex);
            String id = cursor.getString(idIndex);

            numberToIdsMap.computeIfAbsent(number, k -> new ArrayList<>()).add(id);
        }
        cursor.close();

        return numberToIdsMap;
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
