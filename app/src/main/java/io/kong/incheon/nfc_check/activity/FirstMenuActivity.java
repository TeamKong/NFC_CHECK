package io.kong.incheon.nfc_check.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.kong.incheon.nfc_check.R;

public class FirstMenuActivity extends AppCompatActivity{
    public static Activity firstMenuActivity;

    Button btnCheck;
    Button btnCheckList;
    Button btnTimetable;
    Button btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstmenu);

        firstMenuActivity = FirstMenuActivity.this;

        btnCheck = (Button) findViewById(R.id.btnCheck);
        btnSetting = (Button) findViewById(R.id.btnSetting);
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

                    case R.id.btnSetting:
                        Intent intent_setting = new Intent(FirstMenuActivity.this, SettingActivity.class);
                        startActivity(intent_setting);
                        break;
                }
            }
        };

        btnCheck.setOnClickListener(listener);
        btnCheckList.setOnClickListener(listener);
        btnTimetable.setOnClickListener(listener);
        btnSetting.setOnClickListener(listener);
    }
}
