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

    int monResID[] = new int[15];
    int tueResID[] = new int[15];
    int wedResID[] = new int[15];
    int thuResID[] = new int[15];
    int friResID[] = new int[15];
    int satResID[] = new int[15];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        for (int i = 1; i <= 14; i++) {
            monResID[i] = getResources().getIdentifier("mon" + i, "id", "io.kong.incheon.nfc_check");
            tueResID[i] = getResources().getIdentifier("tue" + i, "id", "io.kong.incheon.nfc_check");
            wedResID[i] = getResources().getIdentifier("wed" + i, "id", "io.kong.incheon.nfc_check");
            thuResID[i] = getResources().getIdentifier("thu" + i, "id", "io.kong.incheon.nfc_check");
            friResID[i] = getResources().getIdentifier("fri" + i, "id", "io.kong.incheon.nfc_check");
            satResID[i] = getResources().getIdentifier("sat" + i, "id", "io.kong.incheon.nfc_check");
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