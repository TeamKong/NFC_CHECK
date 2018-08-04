package io.kong.incheon.nfc_check;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText input_id, input_pass;
    Button signin_btn, signup_btn;
    private static String sId="";
    private static String sPw="";
    CheckBox autologin;

    Spinner spinner;
    ArrayAdapter sAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_id = findViewById(R.id.input_id);
        input_pass = findViewById(R.id.input_pass);
        signin_btn = findViewById(R.id.signin_btn);
        signup_btn = findViewById(R.id.signup_btn);
        autologin = findViewById(R.id.checkbox);
        sId= input_id.getText().toString();
        sPw=input_pass.getText().toString();

        spinner = (Spinner) findViewById(R.id.box_iden);
        sAdapter = ArrayAdapter.createFromResource(this, R.array.iden, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), sAdapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

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
                        Intent intent1 = new Intent(MainActivity.this, SignupActivity.class);
                        startActivity(intent1);
                        // 회원가입
                        break;
                }
            }
        };

        signup_btn.setOnClickListener(listener);
        signin_btn.setOnClickListener(listener);
    }
}
