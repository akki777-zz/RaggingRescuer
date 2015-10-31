package com.ahdel.rag;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by admin on 20-06-15.
 */
public class HowtoUse extends ActionBarActivity {

    private static int PICK_CONTACT = 0;
    String phone, msg;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "Pref";
    public static final String Phone = "phoneKey", Name = "nameKey", StringAll = "allKey";
    StringBuilder allContactNames, sbName, recDataString;
    public static StringBuilder sbPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how);

        allContactNames = new StringBuilder(500);
        // `Declaring List Variable`
        sbPhone = new StringBuilder();
        sbName = new StringBuilder();
        recDataString = new StringBuilder();

        Button b = (Button) findViewById(R.id.pick);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickContact();
            }
        });

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Phone)) {
            Log.d("Phone", sharedpreferences.getString(Phone, ""));
            sbPhone.append(sharedpreferences.getString(Phone, ""));
        }
        if (sharedpreferences.contains(Name)) {

            Log.d("Name", sharedpreferences.getString(Name, ""));
            sbName.append(sharedpreferences.getString(Name, ""));
        }
        if (sharedpreferences.contains(StringAll)) {

            Log.d("StringAll", sharedpreferences.getString(StringAll, ""));
        }


    }

    public  void pickContact() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HowtoUse.this);

        // set title
        alertDialogBuilder.setTitle("Emergency Contacts");

        if (sharedpreferences.contains(Name)) {

            msg = sharedpreferences.getString(Name, "");
        }

        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("Add More", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Start activity to get contact
                        final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                        Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                        startActivityForResult(intentPickContact, PICK_CONTACT);
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_CONTACT) {
                Uri returnUri = data.getData();
                Cursor cursor = getContentResolver().query(returnUri, null, null, null, null);

                if (cursor.moveToNext()) {
                    int columnIndex_ID = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                    String contactID = cursor.getString(columnIndex_ID);

                    int columnIndex_HASPHONENUMBER = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                    String stringHasPhoneNumber = cursor.getString(columnIndex_HASPHONENUMBER);

                    if (stringHasPhoneNumber.equalsIgnoreCase("1")) {
                        Cursor cursorNum = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactID,
                                null,
                                null);

                        //Get the first phone number
                        if (cursorNum.moveToNext()) {

                            int columnIndex_number = cursorNum.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            String stringNumber = cursorNum.getString(columnIndex_number);
                            int columnIndex_name = cursorNum.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                            String stringName = cursorNum.getString(columnIndex_name);
                            editor = sharedpreferences.edit();
                            //editor.putString(Phone, stringNumber);

                            sbPhone.append(stringNumber);
                            sbPhone.append(";");
                            sbName.append(stringName).append("\n");
                            editor.putString(Phone, sbPhone.toString());
                            editor.putString(Name, sbName.toString());
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Added " + stringName + " to list", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "No Phone Number!", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "No data!", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.d("TAG", "OnActivityResult Error");
            }

        }
    }


}
