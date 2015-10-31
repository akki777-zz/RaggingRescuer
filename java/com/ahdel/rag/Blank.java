package com.ahdel.rag;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by admin on 21-06-15.
 */
public class Blank extends Activity {

    static SharedPreferences sharedPrefs;
    private static int number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        sharedPrefs =getSharedPreferences("sharedprefs",MODE_PRIVATE);
        number = sharedPrefs.getInt("isLogged",2);

        if(number==2){number=0;}


        if(number==0){
            Intent callReg=new Intent(this,Signup.class);
            callReg.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            callReg.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(callReg);

        }
        else if(number==1){
            changePref();
            Intent callHome=new Intent(this,MainActivity.class);
            callHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            callHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(callHome);


        }
    }

    //preference function for one time screen
    public static void changePref()
    {   SharedPreferences.Editor prefEditor = sharedPrefs.edit();
        prefEditor.putInt("isLogged",1);
        prefEditor.commit();}


}