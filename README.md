# CleanContacts :broom:	
CleanContacts is an Android application designed to help users manage their contacts by identifying and deleting duplicates.
This app provides an interface to view, select, and remove unwanted contact entries from your device's contact list.

## Features
1. Duplicates Detection: Identifies duplicate contacts based on criteria such as name and phone number.
2. Delete Contacts: Allows users to delete contacts by long-clicking on them.
3. Contacts Observer: Monitors changes in the contacts list and updates the app's data accordingly.

## Permissions
The following permissions are required for the app to function properly:
1. READ_CONTACTS: Allows the app to read the user's contacts. This permission is necessary for displaying and detecting duplicates in the contact list.
2. WRITE_CONTACTS: Allows the app to modify the user's contacts. This permission is necessary for deleting selected contacts from the list.

## Usage
View Contacts: Upon launching the app and accepting the necessary permissions, the number of duplicate contacts based on phone number and contact name will be displayed.
By clicking on each criterion, the list of duplicate contacts will be shown.
Delete Contact: Long-click on any contact in the list to delete it. A confirmation will be shown, and the contact will be removed.
Monitor Changes: The app automatically updates the list of contacts whenever changes are made, thanks to the integrated ContactsObserver.

## Code Overview
### MainActivity
Handles the main user interface and initializes the ContactsAdapter to display the list of contacts.

### ContactsAdapter
Custom adapter for displaying contacts in a ListView. Handles the UI rendering for each contact and manages the long-click action to delete contacts.

### ContactHelper
Utility class containing methods for manipulating contact data, including deleting contacts.

### ContactsObserver
A custom observer class that monitors changes in the contacts database.


## Preview 🎥
https://github.com/user-attachments/assets/c1a2eb60-f6e2-46de-b8d6-2d9c7b188ed1


