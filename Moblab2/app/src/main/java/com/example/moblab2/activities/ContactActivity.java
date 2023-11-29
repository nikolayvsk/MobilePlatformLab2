package com.example.moblab2.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.moblab2.ContactModel;
import com.example.moblab2.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactActivity extends AppCompatActivity {
    Button allContactsBtn, selectedContactsBtn;
    ListView contactsListView;
    ArrayAdapter contactsArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        allContactsBtn = findViewById(R.id.allContactsBtn);
        selectedContactsBtn = findViewById(R.id.selectedContactsBtn);
        contactsListView = findViewById(R.id.contactsListView);

        allContactsBtn.setOnClickListener(view -> {
            contactsArrayAdapter = new ArrayAdapter<>(ContactActivity.this, android.R.layout.simple_list_item_1, getPhoneContactsList());
            contactsListView.setAdapter(contactsArrayAdapter);
        });

        selectedContactsBtn.setOnClickListener(view -> {
            contactsArrayAdapter = new ArrayAdapter<>(ContactActivity.this, android.R.layout.simple_list_item_1, getSortedContactsList());
            contactsListView.setAdapter(contactsArrayAdapter);
        });
    }

    private List<ContactModel> getPhoneContactsList() {
        List<ContactModel> returnList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
        }

        ContentResolver contentResolver = getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        while (cursor.moveToNext()) {
            String contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String contactNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

            ContactModel contact = new ContactModel(contactName, contactNumber);
            returnList.add(contact);

            Log.i("CONTACT_PROVIDER", "Contact Name: " + contactName + " Contact Phone: " + contactNumber);
        }

        cursor.close();
        return returnList;
    }

    private List<ContactModel> getSortedContactsList() {
        List<ContactModel> returnList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
        }

        ContentResolver contentResolver = getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        while (cursor.moveToNext()) {
            String contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String contactNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Pattern pattern = Pattern.compile(".*7$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(contactNumber);
            if (matcher.find()) {
                ContactModel contact = new ContactModel(contactName, contactNumber);
                returnList.add(contact);

                Log.i("CONTACT_PROVIDER", "Contact Name: " + contactName + " Contact Phone: " + contactNumber);
            }
        }

        cursor.close();
        return returnList;
    }
}
