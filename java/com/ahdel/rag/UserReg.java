package com.ahdel.rag;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 21-06-15.
 */
public class UserReg extends ActionBarActivity {

    private ProgressDialog progress;
    String name,email,place;
    public static String phone;
    int statusCode = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);

        // Retrieved Values from User Registration Activity
        Bundle b = getIntent().getExtras();
        name = b.getString("name");
        email = b.getString("email");
        phone = b.getString("phone");
        place = b.getString("place");
        place = place.replace(" ","%20");


        class MyAsyncTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                // Progress Bar
                progress = ProgressDialog.show(UserReg.this, "Wait",
                        "Registering...");
            }

            @Override
            protected Void doInBackground(Void... voids) {
                // TODO Auto-generated method stub
                postData();
                Log.d("hello", "done");
                return null;
            }

            protected void onPostExecute(Void voids) {
                progress.dismiss();
                Signup.changePref();

                // Showing Successful Registration Dialog
                AlertDialog.Builder registered = new AlertDialog.Builder(
                        UserReg.this);
                registered.setMessage("You are Successfully registered")
                        .setTitle("Welcome").create().show();

                new Timer().schedule(new TimerTask() {
                    public void run() {
                        Intent callHome = new Intent(UserReg.this,
                                HowtoUse.class);
                        callHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        callHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(callHome);
                    }
                }, 2000);
                return;
            }
        }

        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }


    public void postData() {
        String content = null;
        HttpClient httpclient = new DefaultHttpClient();
        // specify the URL you want to post to
        HttpPost httppost = new HttpPost("http://thedivineprincess.ml/login_ahdel.php?name="+name+"&phone="+phone+"&email="+email+"&place="+place);
        try {

            HttpResponse response = httpclient.execute(httppost);
            Log.d("tag", "response");
            HttpEntity EntityGet = response.getEntity();

            StatusLine statusLine = response.getStatusLine();

            statusCode = statusLine.getStatusCode();


            if (EntityGet != null) {
                //getting the http response and deleting file
                content = EntityUtils.toString(EntityGet);
                Log.d("hello", content);

            }
        } catch (ClientProtocolException e) {
            // process execption
        } catch (IOException e) {
            // process execption
        }
    }

}

