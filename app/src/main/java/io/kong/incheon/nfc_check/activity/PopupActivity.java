package io.kong.incheon.nfc_check.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.kong.incheon.nfc_check.R;
import io.kong.incheon.nfc_check.item.UserItem;
import io.kong.incheon.nfc_check.service.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.kong.incheon.nfc_check.activity.NfcActivity.ATTENDANCE_DAY;
import static io.kong.incheon.nfc_check.activity.NfcActivity.ATTENDANCE_INDEX;
import static io.kong.incheon.nfc_check.activity.NfcActivity.ATTENDANCE_TAG;
import static io.kong.incheon.nfc_check.activity.NfcActivity.ATTENDANCE_TIME;
import static io.kong.incheon.nfc_check.activity.NfcActivity.timeItem;
import static io.kong.incheon.nfc_check.service.RetrofitService.TAG_URL;

public class PopupActivity extends Activity {

    long CheckTime;
    static long TimeRiver = 0; //누적시간
    TextView txtText;
    TextView day_txt;
    TextView time_txt;

    // 현재시간을 msec 으로 구한다.
    long now = System.currentTimeMillis();
    // 현재시간을 date 변수에 저장한다.
    Date date = new Date(now);
    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat sdfNowTime = new SimpleDateFormat("HH:mm:ss");

    SimpleDateFormat minute = new SimpleDateFormat("mm"); //45분을 측정하는 변수
    SimpleDateFormat day = new SimpleDateFormat("E"); //오늘이 무슨 요일인지 판단

    // nowDate 변수에 값을 저장한다.
    String formatDate = sdfNow.format(date);
    String formatTime = sdfNowTime.format(date);

    String formatMinute = minute.format(date);
    String formatDay= day.format(date);

    long first = System.currentTimeMillis();

    String attendance;


    private Retrofit retrofit;
    JSONArray jsonArray;
    UserItem userItem;

    String user_id;
    String attendance_day;
    String attendance_time;
    String sbj_index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        init();

        RetrofitService service = retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = service.attendance_check(user_id, sbj_index, attendance_day, attendance_time, attendance);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    txtText.setText(attendance + "으로 완료되었습니다.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


        CheckTime = (first - timeItem.getSecondTime()) / 1000;
        TimeRiver = TimeRiver + CheckTime; //누적시간 확인

        Log.i("minute: ",formatMinute+", day : "+formatDay);

    }

    private void init() {

        retrofit = new Retrofit.Builder()
                .baseUrl(TAG_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        user_id = userItem.getStid();

        Intent intent = getIntent();
        attendance = intent.getStringExtra(ATTENDANCE_TAG);
        attendance_day = intent.getStringExtra(ATTENDANCE_DAY);
        attendance_time = intent.getStringExtra(ATTENDANCE_TIME);
        sbj_index = intent.getStringExtra(ATTENDANCE_INDEX);


        Toast.makeText(PopupActivity.this, attendance, Toast.LENGTH_SHORT).show();

        txtText = (TextView) findViewById(R.id.txtText);

        //시간 출력하기
        day_txt = (TextView) findViewById(R.id.day_txt);
        time_txt = (TextView) findViewById(R.id.time_txt);

        day_txt.setText(formatDate);
        time_txt.setText(formatTime);

    }

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    //확인 버튼 클릭
    public void mOnClose(View v) {
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}