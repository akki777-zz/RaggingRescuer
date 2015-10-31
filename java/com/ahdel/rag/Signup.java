package com.ahdel.rag;

/**
 * Created by admin on 20-06-15.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Signup extends ActionBarActivity {

    private AutoCompleteTextView actv;
    String[] univ = {"Amity University, Gurgaon", "BITS Pilani", "Delhi Technical University", "Manipal University", "VIT University , Vellore"};
    static SharedPreferences sharedPrefs;
    AutoCompleteTextView act;
    static int number;
    EditText etName, email, phone;
    int[] err = {0, 0, 0,0}, verr = {1, 2, 3,4};
    boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);

        actv = (AutoCompleteTextView) findViewById(R.id.act);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, univ);
        actv.setAdapter(adapter);

        // one time reg.
        sharedPrefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        number = sharedPrefs.getInt("isLogged", 2);
        if (number == 1) {
            Intent callHome = new Intent(this, Signup.class);
            startActivity(callHome);
        }

        email = (EditText) findViewById(R.id.etEmail);
        phone = (EditText) findViewById(R.id.etPhone);
        etName = (EditText) findViewById(R.id.etName);
        act = (AutoCompleteTextView) findViewById(R.id.act);

        // at the moment validation of field
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (etName.getText().length() > 0) {

                    }
                } else {
                    if (etName.getText().length() == 0) {
                        etName.setError("Field cannot be left blank.");

                    }
                    if (etName.getText().length() > 0) {

                        err[0] = 1;
                    }
                }
            }
        });
    }

    // preference function for one time screen
    public static void changePref() {
        SharedPreferences.Editor prefEditor = sharedPrefs.edit();
        prefEditor.putInt("isLogged", 1);
        prefEditor.commit();
    }


    // Email field Validator
    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Show errors if Invalidated fields
    public void setErrors() {
        // verification of username block
        {

            if (etName.getText().length() == 0) {
                etName.setError("Field cannot be left blank.");
            }
            if (etName.getText().length() > 0) {

                err[1] = 2;
            }
        }// end of block

        // verification of phone number block
        {

            if (phone.getText().length() == 0) {
                phone.setError("Field cannot be left blank.");
            }
            if (phone.getText().length() != 10 && phone.getText().length() != 0) {
                phone.setError("Enter valid Mobile Number");
            }
            if (phone.getText().length() > 0 && phone.getText().length() == 10) {
                err[2] = 3;
            }
        }// end of block

        // verification of email block
        {

            if (email.getText().length() == 0) {
                email.setError("Field cannot be left blank.");
            }
            if (email.getText().length() > 0) {
                if (emailValidator(email.getText().toString()) == false) {
                    email.setError("Enter a valid Email address");
                } else {

                    err[3] = 4;
                }
            }
        }// end of block


    }

    // verify all-fields function
    public boolean verify() {
        setErrors();
        if (Arrays.equals(err, verr)) {
            return true;// no Errors
        } else {
            return false;// Errors
        }
    }

    // Register button click function
    public void Submit(View v) {
        verify();
        if (verify() == true) {
            Intent callRegister = new Intent(this, UserReg.class);
            // Create a bundle object
            Bundle b = new Bundle();
            b.putString("name",
                    etName.getText().toString().replaceAll("\\s+", ""));
            b.putString("email", email.getText().toString());
            b.putString("phone", phone.getText().toString());
            b.putString("place",act.getText().toString());
            callRegister.putExtras(b);
            startActivity(callRegister);

        } else {
        }
    }

    // following code is to prevent the user from accidently pressing back
    // button
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again", Toast.LENGTH_SHORT)
                .show();
        // This handler helps to reset the variable after 2 second.
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }
}