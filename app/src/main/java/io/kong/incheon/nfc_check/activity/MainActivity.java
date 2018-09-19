package io.kong.incheon.nfc_check.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import io.kong.incheon.nfc_check.R;
import io.kong.incheon.nfc_check.item.UserItem;
import io.kong.incheon.nfc_check.service.FullScreenView;
import io.kong.incheon.nfc_check.service.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static io.kong.incheon.nfc_check.service.RetrofitService.TAG_URL;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    public static SharedPreferences appData;
    EditText input_id, input_pass;
    Button signin_btn, signup_btn;
    String sId;
    String sPw;
    CheckBox autologin;

    static UserItem userItem;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullScreenView fullScreenView = new FullScreenView();
        fullScreenView.screenView(this);
        setContentView(R.layout.activity_main);

        appData = getSharedPreferences("APPDATA", MODE_PRIVATE);
        init();


        //이전에 로그인을 저장한 이력이 있다면

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.signin_btn:

                        sId = input_id.getText().toString();
                        sPw = input_pass.getText().toString();

                        RetrofitService service = retrofit.create(RetrofitService.class);
                        Call<ResponseBody> call = service.login(sId, sPw);


                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                if (response.isSuccessful()) {
                                    try {
                                        String result = response.body().string();
                                        try {
                                            JSONObject jsonObject = new JSONObject(result);
                                            JSONArray jsonArray = jsonObject.getJSONArray("user_table");

                                            if (jsonArray.length() != 0) {
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject item = jsonArray.getJSONObject(i);

                                                    userItem.setUser_name(item.getString("user_name"));
                                                    userItem.setUser_major(item.getString("user_major"));
                                                }
                                                userItem.setStid(sId);
                                                userItem.setStPass(sPw);
                                                Toast.makeText(MainActivity.this, userItem.getUser_name() + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                                                if (autologin.isChecked()) {
                                                    save();
                                                }
                                                Intent intent = new Intent(MainActivity.this, FirstMenuActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(MainActivity.this, "아이디 및 패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
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

    }

    private void save() {
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("SAVE_LOGIN_DATA", autologin.isChecked());

        editor.putString("ID", input_id.getText().toString().trim());
        editor.putString("PWD", input_pass.getText().toString().trim());
        editor.apply();
    }
}
