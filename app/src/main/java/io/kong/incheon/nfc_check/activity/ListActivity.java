package io.kong.incheon.nfc_check.activity;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.kong.incheon.nfc_check.R;

//학생의 출석을 관리하는 액티비티 구현

public class ListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        Log.i("알림:","값:" );
    }
}
