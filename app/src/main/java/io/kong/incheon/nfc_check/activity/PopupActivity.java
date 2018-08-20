package io.kong.incheon.nfc_check.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import io.kong.incheon.nfc_check.R;
import static io.kong.incheon.nfc_check.activity.NfcActivity.timeItem;

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
    // nowDate 변수에 값을 저장한다.
    String formatDate = sdfNow.format(date);
    String formatTime = sdfNowTime.format(date);

    long first = System.currentTimeMillis();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);
        txtText = (TextView) findViewById(R.id.txtText);

        //시간 출력하기
        day_txt = (TextView) findViewById(R.id.day_txt);
        time_txt = (TextView) findViewById(R.id.time_txt);

        day_txt.setText(formatDate);
        time_txt.setText(formatTime);

        CheckTime = (first-timeItem.getSecondTime())/1000;
        TimeRiver= TimeRiver + CheckTime; //누적시간 확인

        Log.e("first And Second : ",""+first+","+timeItem.getSecondTime()+", TimeRiver: "+ TimeRiver+", Minus:"+String.valueOf(first-timeItem.getSecondTime()));
        if(TimeRiver <60) {
            txtText.setText("누적시간 "+TimeRiver + "초가 지난 뒤 NFC TAG 접촉을 해제");
        }
        else if(TimeRiver %60 == 0){
            txtText.setText("누적시간 "+TimeRiver/60+"분이 지난 뒤 NFC TAG 접촉을 해제");
        }
        else if(TimeRiver >= 60) {
            txtText.setText("누적시간 "+TimeRiver/60 +"분 "+ TimeRiver%60+"초가 지난 뒤 NFC TAG 접촉을 해제");
        }

    }

    //확인 버튼 클릭
    public void mOnClose(View v) {
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup_2");
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