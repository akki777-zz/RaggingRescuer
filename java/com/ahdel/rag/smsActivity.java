package com.ahdel.rag;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;

import java.io.IOException;

/**
 * Created by admin on 21-06-15.
 */
public class smsActivity extends Activity {

    // Declaring a Location Manager
    protected LocationManager locationManager;

    String help_msg = "I am being ragged at ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        v.vibrate(200);
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        try {
            help();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void help() throws IOException {

        //this string contains location

        //Lat and Lan of person
        GPSTracker gps = new GPSTracker(smsActivity.this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();


        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(HowtoUse.sbPhone.toString(), null, help_msg + "Lat: " + latitude + "\nLong: " + longitude , null, null);
            Log.d("sms","Emergency SMS sent.");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                        startService(new Intent(smsActivity.this, Alarm.class));

                }
            }, 5000);


        } catch (Exception e) {
            Log.d("sms","SMS failed, please try again.");
            e.printStackTrace();
        }
    }
}
