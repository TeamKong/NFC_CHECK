package io.kong.incheon.nfc_check.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.kong.incheon.nfc_check.R;
import io.kong.incheon.nfc_check.item.UserItem;
import io.kong.incheon.nfc_check.service.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TimeTableActivity extends AppCompatActivity {


    static final String TAG = SubjectActivity.class.getCanonicalName();
    static final String TAG_URL = "http://13.209.75.255:3000";
    static final String TAG_JSON = "person_subject";
    private Retrofit retrofit;
    JSONObject item;

    UserItem userItem;

    FloatingActionButton btnFAB;

    String user_id;

    int monResID[] = new int[15];
    int tueResID[] = new int[15];
    int wedResID[] = new int[15];
    int thuResID[] = new int[15];
    int friResID[] = new int[15];
    int satResID[] = new int[15];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        userItem = new UserItem();

        for (int i = 1; i <= 14; i++) {
            monResID[i] = getResources().getIdentifier("mon" + i, "id", "io.kong.incheon.nfc_check");
            tueResID[i] = getResources().getIdentifier("tue" + i, "id", "io.kong.incheon.nfc_check");
            wedResID[i] = getResources().getIdentifier("wed" + i, "id", "io.kong.incheon.nfc_check");
            thuResID[i] = getResources().getIdentifier("thu" + i, "id", "io.kong.incheon.nfc_check");
            friResID[i] = getResources().getIdentifier("fri" + i, "id", "io.kong.incheon.nfc_check");
            satResID[i] = getResources().getIdentifier("sat" + i, "id", "io.kong.incheon.nfc_check");
        }

        user_id = userItem.getStid();

        retrofit = new Retrofit.Builder()
                .baseUrl(TAG_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        RetrofitService service = retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = service.person_subjectTable(user_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Toast.makeText(TimeTableActivity.this, "test", Toast.LENGTH_SHORT).show();
                        String result = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                            for(int i = 0; i < jsonArray.length(); i++) {

                                item = jsonArray.getJSONObject(i);

                                String stName = item.getString("sbj_name");
                                String stDay = item.getString("sbj_day");
                                String stProfessor = item.getString("sbj_professor");
                                String stIndex = item.getString("sbj_index");

                            }

                        } catch (JSONException e) {
                            Log.d(TAG, "showResult : ", e);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        btnFAB = (FloatingActionButton) findViewById(R.id.btnFAB);
        btnFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeTableActivity.this, SubjectActivity.class);
                startActivity(intent);
            }
        });

    }
}