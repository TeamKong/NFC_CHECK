package io.kong.incheon.nfc_check.activity;

import android.app.Activity;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.kong.incheon.nfc_check.item.UserItem;
import io.kong.incheon.nfc_check.service.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.kong.incheon.nfc_check.service.RetrofitService.TAG_URL;

public class SplashActivity extends Activity {

    private Retrofit retrofit;
    public static SharedPreferences appData;
    boolean saveLoginData;

    String sId;
    String sPw;

    static UserItem userItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appData = getSharedPreferences("APPDATA", MODE_PRIVATE);
        init();
        load();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (saveLoginData) {
            RetrofitService service = retrofit.create(RetrofitService.class);
            Call<ResponseBody> call = service.login(sId, sPw);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        if (response.body().toString() != "[]") {
                            try {
                                String result = response.body().string();
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    JSONArray jsonArray = jsonObject.getJSONArray("user_table");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject item = jsonArray.getJSONObject(i);

                                        userItem.setUser_name(item.getString("user_name"));
                                        userItem.setUser_major(item.getString("user_major"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            userItem.setStid(sId);
                            userItem.setStPass(sPw);
                            Toast.makeText(SplashActivity.this, userItem.getUser_name() + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SplashActivity.this, FirstMenuActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SplashActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(SplashActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void init() {
        retrofit = new Retrofit.Builder()
                .baseUrl(TAG_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userItem = new UserItem();
    }

    private void load() {
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        sId = appData.getString("ID", "");
        sPw = appData.getString("PWD", "");
    }

}
