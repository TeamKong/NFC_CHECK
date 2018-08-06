package io.kong.incheon.nfc_check.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.kong.incheon.nfc_check.R;

public class MainActivity extends AppCompatActivity {

    EditText input_id, input_pass;
    Button signin_btn, signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_id = (EditText) findViewById(R.id.input_id);
        input_pass = (EditText) findViewById(R.id.input_pass);
        signin_btn = (Button) findViewById(R.id.signin_btn);
        signup_btn = (Button) findViewById(R.id.signup_btn);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()){
                    case R.id.signin_btn:
                        Intent intent = new Intent(MainActivity.this, FirstMenuActivity.class);
                        startActivity(intent);
                        // 로그인
                        break;
                    case R.id.signup_btn:
                        // 회원가입
                        break;
                }
            }
        };

        signup_btn.setOnClickListener(listener);
        signin_btn.setOnClickListener(listener);
    }

    public static class FirstMenuActivity extends AppCompatActivity{
        Button student_check;
        Button schedule_list;
        Button setting;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_firstmenu);

            student_check = (Button) findViewById(R.id.student_check);
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
}
