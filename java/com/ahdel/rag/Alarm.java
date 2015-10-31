package com.ahdel.rag;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class Alarm extends Service {

    private MediaRecorder myAudioRecorder;
    private String outputFile = null, filePath;
    private int i = 0;

    SharedPreferences prefs;
    public static final String INDEX = "index_key";
    public static final String MyPREFERENCES = "MyPrefs";

    private long startTime, difference;
    private Handler hand; // for maximum few minutes recording limit

    @Override
    public void onCreate() {
        super.onCreate();

        //Setting Preferences
        prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (prefs.contains(INDEX)) {
            i = prefs.getInt(INDEX, i);
        }

        filePath = Environment.getExternalStorageDirectory().getPath();

        startTime = System.currentTimeMillis();
        //start Recording
        record();
        //adding elapsed timer and maximum record limit = 30sec
        hand = new Handler();
        hand.postDelayed(run, 0);
    }

    //making sound .mp3 file
    private void prepareFile() {

        ++i;
        String j = String.valueOf(i);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(INDEX, i); //changing preferences to create a new file each time
        editor.apply();
        File file = new File(filePath, "AHDEL");
        if (!file.exists())
            file.mkdirs();
        outputFile = file.getAbsolutePath() + "/AHDEL" + j + ".mp3";    //created a file in internal storage

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            hand.postDelayed(this, 1000);
            long elapsed = System.currentTimeMillis() - startTime;
            int seconds = (int) elapsed / 1000;
            Log.d("elapsed", Integer.toString(seconds));

            //setting maximum recording limit = 2 minutes
            if (seconds >= 10) {
                //stop Recording
                myAudioRecorder.stop();
                Log.d("elapsed", "Recording Stopped");
                if (myAudioRecorder != null) {
                    myAudioRecorder.reset();
                    myAudioRecorder.release();
                    myAudioRecorder = null;
                }
                //stopping elapsed timer
                hand.removeCallbacks(run);

                stopSelf();

            }
        }
    };

    //record sound function
    private void record() {
        prepareFile();
        try {
            myAudioRecorder.prepare();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        myAudioRecorder.start();
        Toast.makeText(this, "Recording started", Toast.LENGTH_LONG).show();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Log.d("hello","Rescue Recording Started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("hello","Rescue Recording Stopped");
    }
}
