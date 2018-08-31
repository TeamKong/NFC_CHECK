package io.kong.incheon.nfc_check.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

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
    RetrofitService service;
    Call<ResponseBody> call;
    JSONObject item;

    UserItem userItem;

    FloatingActionButton btnFAB;

    String user_id;

    int sub[][] = new int[15][6];
    TextView txtSub[][] = new TextView[15][6];
    TextView txtMON;
    TextView txtTUE;
    TextView txtWED;
    TextView txtTHU;
    TextView txtFRI;
    TextView txtSAT;
    Calendar oCalendar;
    String todayWeek;
    int weekNumber;
    String[] dayArr = new String[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullScreenView fullScreenView = new FullScreenView();
        fullScreenView.screenView(this);
        setContentView(R.layout.activity_timetable);

        init();

        Bundle getIntent = getIntent().getExtras();

        if(getIntent != null) {
            String intentInput = getIntent.getString("input");
            if (intentInput.equals("insert")) {
                Toast.makeText(TimeTableActivity.this, "추가완료", Toast.LENGTH_SHORT).show();
            } else if (intentInput.equals("delete")) {
                Toast.makeText(TimeTableActivity.this, "삭제완료", Toast.LENGTH_SHORT).show();
            }
        }

        btnFAB = (FloatingActionButton) findViewById(R.id.btnFAB);
        btnFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeTableActivity.this, SubjectActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TimeTableActivity.this, FirstMenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void init() {

        oCalendar = Calendar.getInstance();

        userItem = new UserItem();

        for (int i = 1; i < 15; i++) {
            for (int j = 0; j < 6; j++) {
                sub[i][j] = getResources().getIdentifier("sub" + i + "_" + j, "id", "io.kong.incheon.nfc_check");
                txtSub[i][j] = (TextView) findViewById(sub[i][j]);
            }
        }

        user_id = userItem.getStid();

        retrofit = new Retrofit.Builder()
                .baseUrl(TAG_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RetrofitService.class);
        call = service.person_subjectTable(user_id);

        txtMON = (TextView) findViewById(R.id.MON);
        txtTUE = (TextView) findViewById(R.id.TUE);
        txtWED = (TextView) findViewById(R.id.WED);
        txtTHU = (TextView) findViewById(R.id.THU);
        txtFRI = (TextView) findViewById(R.id.FRI);
        txtSAT = (TextView) findViewById(R.id.SAT);

        final String[] week = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        todayWeek = week[oCalendar.get(Calendar.DAY_OF_WEEK) - 2];


        switch (todayWeek) {
            case "MON":
                weekNumber = 0;
                setTextView(txtMON, weekNumber);
                break;
            case "TUE":
                weekNumber = 1;
                setTextView(txtTUE, weekNumber);
                break;
            case "WED":
                weekNumber = 2;
                setTextView(txtWED, weekNumber);
                break;
            case "THU":
                weekNumber = 3;
                setTextView(txtTHU, weekNumber);
                break;
            case "FRI":
                weekNumber = 4;
                setTextView(txtFRI, weekNumber);
                break;
            case "SAT":
                weekNumber = 5;
                setTextView(txtSAT, weekNumber);
                break;
            case "SUN":
                weekNumber = 6;

        }


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

                                if (!Integer.toString(pos).equals("-1")) {
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
            case "토":
                i = 5;
                gridTable2(dayArr, stName, i);

        }
    }

    public void gridTable2(String[] dayArr, final String stName, int i) {
        for (int x = 1; x < dayArr.length; x++) {
            for (int y = 1; y < 15; y++) {
                if (dayArr[x].equals(Integer.toString(y))) {
                    txtSub[y][i].setText(stName);
                    txtSub[y][i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder oDialog = new AlertDialog.Builder(TimeTableActivity.this);
                            oDialog.setMessage(stName + "과목을 삭제하시겠습니까?")
                                    .setCancelable(false)
                                    .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            call = service.person_delete(user_id, stName);
                                            call.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.isSuccessful()) {
                                                        if (!response.body().toString().equals("[]")) {
                                                            Intent intent = new Intent(TimeTableActivity.this, TimeTableActivity.class);
                                                            intent.putExtra("input", "delete");
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });

                            AlertDialog dialog = oDialog.create();
                            dialog.show();
                        }
                    });
                }
            }
        }
    }

    private void setTextView(TextView txtView, int weekNumber) {
        txtView.setTextSize(20);
        txtView.setTypeface(null, Typeface.BOLD);
        txtView.setTextColor(getResources().getColor(R.color.colorWeek));
        txtView.setBackgroundColor(getResources().getColor(R.color.textColor));


        for (int i = 1; i < 15; i++) {
                sub[i][weekNumber] = getResources().getIdentifier("sub" + i + "_" + weekNumber, "id", "io.kong.incheon.nfc_check");
                txtSub[i][weekNumber] = (TextView) findViewById(sub[i][weekNumber]);
                txtSub[i][weekNumber].setBackgroundColor(getResources().getColor(R.color.colorSubWeek));
        }

    }
}