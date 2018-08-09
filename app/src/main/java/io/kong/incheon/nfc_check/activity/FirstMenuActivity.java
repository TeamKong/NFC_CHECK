package io.kong.incheon.nfc_check.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.kong.incheon.nfc_check.R;

public class FirstMenuActivity extends AppCompatActivity{
    Button btnCheck;
    Button btnCheckList;
    Button btnTimetable;
    Button btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstmenu);

        btnCheck = (Button) findViewById(R.id.btnCheck);
        btnCheckList = (Button) findViewById(R.id.btnCheckList);
        btnTimetable = (Button) findViewById(R.id.btnTimetable);
        btnSetting = (Button) findViewById(R.id.btnSetting);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()){
                    case R.id.btnCheck:
                        Intent intent_check = new Intent(FirstMenuActivity.this, NfcActivity.class);
                        startActivity(intent_check);
                        break;
                    case R.id.btnCheckList:
                        Intent intent_list = new Intent(FirstMenuActivity.this, ListActivity.class);
                        startActivity(intent_list);
                        break;

                    case R.id.btnTimetable:
                        Intent intent_timetable = new Intent(FirstMenuActivity.this, TimeTableActivity.class);
                        startActivity(intent_timetable);
                        break;
                }
            }
        };

        btnCheck.setOnClickListener(listener);
        btnCheckList.setOnClickListener(listener);
        btnTimetable.setOnClickListener(listener);
    }
}
