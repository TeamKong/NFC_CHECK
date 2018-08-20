package io.kong.incheon.nfc_check.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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

import static io.kong.incheon.nfc_check.service.RetrofitService.TAG_URL;

public class TimeTableActivity extends AppCompatActivity {


    static final String TAG = TimeTableActivity.class.getCanonicalName();
    static final String TAG_JSON = "person_subject";
    private Retrofit retrofit;
    JSONObject item;

    UserItem userItem;

    FloatingActionButton btnFAB;

    String user_id;

    int sub[][] = new int[14][5];
    TextView txtSub[][] = new TextView[14][5];
    String[] dayArr = new String[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        userItem = new UserItem();

        for (int i = 1; i < 14; i++) {
            for (int j = 0; j < 5; j++) {
                sub[i][j] = getResources().getIdentifier("sub" + i + "_" + j, "id", "io.kong.incheon.nfc_check");
                txtSub[i][j] = (TextView) findViewById(sub[i][j]);
            }
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
                        String result = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                item = jsonArray.getJSONObject(i);

                                String stName = item.getString("sbj_name");
                                String stDay = item.getString("sbj_day");

                                int pos = stDay.indexOf(",");
                                dayArr = stDay.split(",");

                                if (Integer.toString(pos) != "-1") {
                                    String[] firDay = dayArr[0].split(" ");
                                    String[] secDay = dayArr[1].split(" ");
                                    gridTable(firDay, stName);
                                    gridTable(secDay, stName);

                                } else {
                                    String[] singleDay = stDay.split(" ");
                                    gridTable(singleDay, stName);
                                }
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
                Toast.makeText(TimeTableActivity.this, R.string.db_failure, Toast.LENGTH_SHORT).show();
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

    public void gridTable(String[] dayArr, String stName) {
        int i;
        switch (dayArr[0]) {
            case "월":
                i = 0;
                gridTable2(dayArr, stName, i);
                break;
            case "화":
                i = 1;
                gridTable2(dayArr, stName, i);
                break;
            case "수":
                i = 2;
                gridTable2(dayArr, stName, i);
                break;
            case "목":
                i = 3;
                gridTable2(dayArr, stName, i);
                break;
            case "금":
                i = 4;
                gridTable2(dayArr, stName, i);
                break;
        }
    }

    public void gridTable2(String[] dayArr, String stName, int i) {
        for (int x = 1; x < dayArr.length; x++) {
            for (int y = 1; y < 14; y++) {
                if (dayArr[x].equals(Integer.toString(y))) {
                    txtSub[y][i].setText(stName);
                }
            }
        }
    }
}