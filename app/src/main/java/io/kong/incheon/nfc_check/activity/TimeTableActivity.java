package io.kong.incheon.nfc_check.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.kong.incheon.nfc_check.R;

public class TimeTableActivity extends AppCompatActivity {

    FloatingActionButton btnFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        for (int i = 1; i <= 14; i++) {
            int monResID = getResources().getIdentifier("mon" + i, "id", "io.kong.incheon.nfc_check");
            int tueResID = getResources().getIdentifier("tue" + i, "id", "io.kong.incheon.nfc_check");
            int wedResID = getResources().getIdentifier("wed" + i, "id", "io.kong.incheon.nfc_check");
            int thuResID = getResources().getIdentifier("thu" + i, "id", "io.kong.incheon.nfc_check");
            int friResID = getResources().getIdentifier("fri" + i, "id", "io.kong.incheon.nfc_check");
            int satResID = getResources().getIdentifier("sat" + i, "id", "io.kong.incheon.nfc_check");

        }



        btnFAB = (FloatingActionButton) findViewById(R.id.btnFAB);
        btnFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeTableActivity.this, SubjectActivity.class);
                startActivity(intent);
            }
        });

    }
}