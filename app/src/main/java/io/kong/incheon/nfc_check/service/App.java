package io.kong.incheon.nfc_check.service;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/Binggrae.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/Binggrae-Bold.ttf"));
    }
}
