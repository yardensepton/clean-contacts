package com.example.contactdeleter;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
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

        return queryDuplicatedContacts(contentResolver, duplicateIds, Type.NAME.toString());
    }

    public static ArrayList<Contact> getDuplicatedContactsByPhoneNumber(ContentResolver contentResolver) {
        HashMap<String, List<String>> numberToIdsMap = collectIdsForPhoneNumbers(contentResolver, null);
        List<String> duplicateNumberIds = filterDuplicateIds(numberToIdsMap);

        if (duplicateNumberIds.isEmpty()) {
            return new ArrayList<>(); // No duplicates found
        }

        return queryDuplicatedContacts(contentResolver, duplicateNumberIds, Type.NUMBER.toString());
    }

    private static ArrayList<Contact> queryDuplicatedContacts(ContentResolver contentResolver, List<String> ids, String type) {
        String selection = ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID + " IN (" + TextUtils.join(",", Collections.nCopies(ids.size(), "?")) + ")";
        String[] selectionArgs = ids.toArray(new String[0]);


        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER}, selection, selectionArgs, Objects.equals(type, Type.NAME.toString()) ? ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME : ContactsContract.CommonDataKinds.Phone.NUMBER + " ASC");

        ArrayList<Contact> duplicatedContacts = new ArrayList<>();
        if (cursor != null) {
            int idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idIndex);
                String name = cursor.getString(nameIndex);
                String number = cursor.getString(numberIndex);
                duplicatedContacts.add(new Contact(id, name, number));
            }
            cursor.close();
        }

        return duplicatedContacts;
    }

    public static Cursor getContactCursor(ContentResolver contactHelper, String startsWith) {
        String[] projection = {ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID
                , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cur = null;

        try {
            if (startsWith != null && !startsWith.isEmpty()) {
                cur = contactHelper.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like \"" + startsWith + "%\"", null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            } else {
                cur = contactHelper.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            }
            assert cur != null;
            cur.moveToFirst();
        } catch (Exception e) {
            Log.e("ContactHelper", String.valueOf(e));
        }
        return cur;
    }

    public static int getDuplicatedCount(ContentResolver contentResolver, String type) {
        ArrayList<Contact> duplicatedContacts = new ArrayList<>();
        if ("name".equals(type)) {
            duplicatedContacts = getDuplicatedContactsByName(contentResolver, null);
        } else if ("phone".equals(type)) {
            duplicatedContacts = getDuplicatedContactsByPhoneNumber(contentResolver);
        }

        return duplicatedContacts.size()/2;
    }

    public static ArrayList<Contact> getDuplicatedList(ContentResolver contentResolver, String type) {
        ArrayList<Contact> duplicatedContacts = new ArrayList<>();
        if ("name".equals(type)) {
            duplicatedContacts = getDuplicatedContactsByName(contentResolver, null);
        } else if ("phone".equals(type)) {
            duplicatedContacts = getDuplicatedContactsByPhoneNumber(contentResolver);
        }

        return duplicatedContacts;
    }



    private static HashMap<String, List<String>> collectIdsForNames(ContentResolver contentResolver, String startsWith) {
        HashMap<String, List<String>> nameToIdsMap = new HashMap<>();
        Cursor cursor = getContactCursor(contentResolver, startsWith);

        if (cursor == null) {
            return nameToIdsMap; // No contacts found
        }

        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID);

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
        int idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID);

        while (cursor.moveToNext()) {
            String number = cursor.getString(numberIndex);
            String id = cursor.getString(idIndex);

            numberToIdsMap.computeIfAbsent(number, k -> new ArrayList<>()).add(id);
        }
        cursor.close();

        return numberToIdsMap;
    }


    public static void deleteContact(ContentResolver contentResolver, long contactId) {

        Uri rawContactUri = ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, contactId);

        int rowsDeleted = contentResolver.delete(rawContactUri, null, null);

        if (rowsDeleted > 0) {
            Log.d("ContactHelper", "Contact deleted successfully");
            contentResolver.notifyChange(ContactsContract.Contacts.CONTENT_URI, null);
       } else {
            Log.d("ContactHelper", "Contact not deleted ");
        }
    }
}
