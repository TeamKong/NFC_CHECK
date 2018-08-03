package io.kong.incheon.nfc_check;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class FirstMenuActivity extends AppCompatActivity{
    Button student_check;
    Button schedule_list;
    Button setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstmenu);

        student_check = findViewById(R.id.student_check);
        schedule_list = findViewById(R.id.schedule_list);
        setting = findViewById(R.id.setting);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()){
                    case R.id.student_check:
                        Intent intent = new Intent(FirstMenuActivity.this, NfcActivity.class);
                        startActivity(intent);
                        break;
//                    case R.id.schedule_list:
//                        Intent intent2 = new Intent(FirstMenuActivity.this, NfcActivity.class);
//                        startActivity(intent2);
//                        break;
                }
            }
        };

        student_check.setOnClickListener(listener);

    }
}
