package io.kong.incheon.nfc_check;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText input_id, input_pass;
    Button signin_btn, signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_id = findViewById(R.id.input_id);
        input_pass = findViewById(R.id.input_pass);
        signin_btn = findViewById(R.id.signin_btn);
        signup_btn = findViewById(R.id.signup_btn);

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
}
