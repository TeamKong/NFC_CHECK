package io.kong.incheon.nfc_check.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import io.kong.incheon.nfc_check.R;
import io.kong.incheon.nfc_check.item.UserItem;
import io.kong.incheon.nfc_check.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import static io.kong.incheon.nfc_check.service.RetrofitService.TAG_URL;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    public static SharedPreferences appData;
    boolean saveLoginData;
    EditText input_id, input_pass;
    Button signin_btn, signup_btn;
    String sId;
    String sPw;
    CheckBox autologin;

    Spinner spinner;
    ArrayAdapter sAdapter;

    static UserItem userItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appData = getSharedPreferences("APPDATA", MODE_PRIVATE);
        init();
        load();


        //이전에 로그인을 저장한 이력이 있다면
        if (saveLoginData) {

            RetrofitService service = retrofit.create(RetrofitService.class);
            Call<List<UserItem>> call = service.login(sId, sPw);

            call.enqueue(new Callback<List<UserItem>>() {
                @Override
                public void onResponse(Call<List<UserItem>> call, Response<List<UserItem>> response) {

                    if (response.isSuccessful()) {
                        if (response.body().toString() != "[]") {
                            userItem.setStid(sId);
                            userItem.setStPass(sPw);
                            Toast.makeText(MainActivity.this, sId + "님 자동로그인", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, FirstMenuActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<UserItem>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        sAdapter = ArrayAdapter.createFromResource(this, R.array.iden, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.signin_btn:

                        sId = input_id.getText().toString();
                        sPw = input_pass.getText().toString();

                        RetrofitService service = retrofit.create(RetrofitService.class);
                        Call<List<UserItem>> call = service.login(sId, sPw);


                        call.enqueue(new Callback<List<UserItem>>() {
                            @Override
                            public void onResponse(Call<List<UserItem>> call, Response<List<UserItem>> response) {

                                if (response.isSuccessful()) {
                                    if (response.body().toString() != "[]") {
                                        userItem.setStid(sId);
                                        userItem.setStPass(sPw);

                                        Toast.makeText(MainActivity.this, userItem.getStid() + "님 환영합니다.", Toast.LENGTH_SHORT).show();

                                        save();
                                        Toast.makeText(MainActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(MainActivity.this, FirstMenuActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(MainActivity.this, "아이디 및 패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<UserItem>> call, Throwable t) {
                                Toast.makeText(MainActivity.this, R.string.db_failure, Toast.LENGTH_SHORT).show();
                            }
                        });
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

    public void init() {
        retrofit = new Retrofit.Builder()
                .baseUrl(TAG_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userItem = new UserItem();

        input_id = (EditText) findViewById(R.id.input_id);
        input_pass = (EditText) findViewById(R.id.input_pass);
        signin_btn = (Button) findViewById(R.id.signin_btn);
        signup_btn = (Button) findViewById(R.id.signup_btn);
        autologin = (CheckBox) findViewById(R.id.checkbox);
        sId = input_id.getText().toString();
        sPw = input_pass.getText().toString();

        spinner = (Spinner) findViewById(R.id.box_iden);

    }

    private void save() {
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("SAVE_LOGIN_DATA", autologin.isChecked());

        editor.putString("ID", input_id.getText().toString().trim());
        editor.putString("PWD", input_pass.getText().toString().trim());
        editor.apply();
    }

    private void load() {
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        sId = appData.getString("ID", "");
        sPw = appData.getString("PWD", "");
    }
}
