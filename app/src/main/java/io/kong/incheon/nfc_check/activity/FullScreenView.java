package io.kong.incheon.nfc_check.activity;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

public class FullScreenView {
    public void screenView(Context context) {
        ((Activity)context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
