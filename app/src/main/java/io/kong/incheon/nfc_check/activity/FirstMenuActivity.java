package io.kong.incheon.nfc_check.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.kong.incheon.nfc_check.R;

public class FirstMenuActivity extends AppCompatActivity{
    Button student_check;
    Button student_list;
    Button schedule_list;
    Button setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstmenu);

        student_check = (Button) findViewById(R.id.student_check);
        student_list = (Button) findViewById(R.id.student_list);
        schedule_list = (Button) findViewById(R.id.schedule_list);
        setting = (Button) findViewById(R.id.setting);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()){
                    case R.id.student_check:
                        Intent intent = new Intent(FirstMenuActivity.this, NfcActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.student_list:
                        Intent intent1 = new Intent(FirstMenuActivity.this, ListActivity.class);
                        startActivity(intent1);
                        break;

//                    case R.id.schedule_list:
//                        Intent intent2 = new Intent(FirstMenuActivity.this, NfcActivity.class);
//                        startActivity(intent2);
//                        break;
                }
            }
        };

        student_check.setOnClickListener(listener);
        student_list.setOnClickListener(listener);
    }
}
