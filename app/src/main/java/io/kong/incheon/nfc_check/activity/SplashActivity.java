package io.kong.incheon.nfc_check.activity;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.WindowManager;

public class SplashActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(3000);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }

        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

}
