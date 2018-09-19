package io.kong.incheon.nfc_check.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import io.kong.incheon.nfc_check.R;
import io.kong.incheon.nfc_check.item.NFCItem;
import io.kong.incheon.nfc_check.item.TimeItem;
import io.kong.incheon.nfc_check.item.UserItem;
import io.kong.incheon.nfc_check.service.FullScreenView;
import io.kong.incheon.nfc_check.service.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.kong.incheon.nfc_check.service.RetrofitService.TAG_URL;

//NFC 태그 값을 출력하는 코드입니다.
//학생이 태그에 핸드폰을 접촉하면 출석이 되는 형식

public class NfcActivity extends Activity {

    static final String TAG = TimeTableActivity.class.getCanonicalName();
    static final String ATTENDANCE_TAG = "attendance";
    static final String ATTENDANCE_DAY = "day";
    static final String ATTENDANCE_TIME = "time";
    static final String ATTENDANCE_INDEX = "sbj_index";


    static TimeItem timeItem;
    static long curSecond;

    private Retrofit retrofit;
    JSONArray jsonArray;

    NFCItem nfcItem = new NFCItem();
    Ndef ndefTag;
    Tag tag;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    public static String tagNum = null;
    private TextView tagUserName;
    private TextView tagUserId;
    private TextView tagUserMajor;
    private TextView tagDesc;

    String user_id;
    String user_name;
    String user_major;
    String dbDay;

    String[] dbArrDay;
    String todayWeek;
    Calendar oCalendar;
    String nowHour;
    String nowMinute;
    String period;
    String attendance;
    String sbj_index;

    int nowhour;
    int nowminute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullScreenView fullScreenView = new FullScreenView();

        fullScreenView.screenView(this);
        setContentView(R.layout.activity_nfc);
        init();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    }

    private void init() {

        timeItem = new TimeItem();

        retrofit = new Retrofit.Builder()
                .baseUrl(TAG_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        tagUserName = findViewById(R.id.student_name);
        tagUserId = findViewById(R.id.student_number);
        tagDesc = findViewById(R.id.tagDesc);
        tagUserMajor = findViewById(R.id.student_major);

        user_id = UserItem.getStid();
        user_name = UserItem.getUser_name();
        user_major = UserItem.getUser_major();

        tagUserName.setText(user_id);
        tagUserId.setText(user_name);
        tagUserMajor.setText(user_major);


        oCalendar = Calendar.getInstance();
        final String[] week = {"월", "화", "수", "목", "금", "토", "일"};
        todayWeek = week[oCalendar.get(Calendar.DAY_OF_WEEK) - 2];
        nowHour = new SimpleDateFormat("HH").format(new Date());
        nowMinute = new SimpleDateFormat("mm").format(new Date());

        nowhour = Integer.parseInt(nowHour);
        nowminute = Integer.parseInt(nowMinute);

        if (nowminute >= 50) {
            nowhour = nowhour + 1;
        }

        switch (nowhour) {
            case 9:
                period = "1";
                break;
            case 10:
                period = "2";
                break;
            case 11:
                period = "3";
                break;
            case 12:
                period = "4";
                break;
            case 13:
                period = "5";
                break;
            case 14:
                period = "6";
                break;
            case 15:
                period = "7";
                break;
            case 16:
                period = "8";
                break;
            case 17:
                period = "9";
                break;
            case 18:
                period = "10";
                break;
            case 19:
                period = "11";
                break;
            case 20:
                period = "12";
                break;
            case 21:
                period = "13";
                break;
            case 22:
                period = "14";
                break;
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);



        if (tag != null) {
            curSecond = System.currentTimeMillis();
            timeItem.setSecondTime(curSecond); //찍은 직후의 시간 밀리초를 넣엇당..
            Log.i("curSecond", "" + curSecond);

            byte[] tagId = tag.getId();
            tagNum = toHexString(tagId);
            nfcItem.setTagNum(toHexString(tagId));
            tagDesc.setText("출석체크 중에 스마트폰 사용시 출석에 불이익이 생길 수 있습니다.");

            RetrofitService service = retrofit.create(RetrofitService.class);
            Call<ResponseBody> call = service.NFC_check(toHexString(tag.getId()), user_id);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String result = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                jsonArray = jsonObject.getJSONArray("NFC_check");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject item = jsonArray.getJSONObject(i);

                                        dbDay = item.getString("sbj_day");
                                        sbj_index = item.getString("sbj_index");

                                        int pos = dbDay.indexOf(",");
                                        dbArrDay = dbDay.split(",");

                                        if (!Integer.toString(pos).equals("-1")) {

                                            String[] firDay = dbArrDay[0].split(" ");
                                            String[] secDay = dbArrDay[1].split(" ");

                                            if (firDay[0].equals(todayWeek)) {
                                                CallPopUpActivity(intent, tag, firDay);
                                            } else if (secDay[0].equals(todayWeek)) {
                                                CallPopUpActivity(intent, tag, secDay);
                                            } else {
                                                tagDesc.setText("금일 해당강의실에서의 수업이 없습니다.");
                                            }

                                        } else {

                                            String[] singleDay = dbDay.split(" ");

                                            if (singleDay[0].equals(todayWeek)) {
                                                CallPopUpActivity(intent, tag, singleDay);
                                            } else {
                                                Toast.makeText(NfcActivity.this, "금일 해당강의실에서의 수업이 없습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }
                                } else {
                                    Toast.makeText(NfcActivity.this, "해당 강의실에서 수강중인 과목이 없습니다.", Toast.LENGTH_SHORT).show();
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

                }
            });

        }
    }

    public static final String CHARS = "0123456789ABCDEF";

    public static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            sb.append(CHARS.charAt((data[i] >> 4) & 0x0F)).append(
                    CHARS.charAt(data[i] & 0x0F));
        }
        return sb.toString();
    }

    private void CallPopUpActivity(final Intent intent, final Tag tag, String[] dbArrDay) {
        boolean periodCheck = true;

        ndefTag = Ndef.get(tag);
        for (int x = 1; x < dbArrDay.length; x++) {
            if (periodCheck) {
                if (dbArrDay[x].equals(period) && nowminute <= 10) {
                    attendance = "지각";


                    try {
                        ndefTag.connect();

                        if (ndefTag.isConnected()) {
                            Log.i("Second", ": connecting exit.");
                            Intent intent2 = new Intent(NfcActivity.this, PopupActivity.class);
                            intent.putExtra("NFC Data", "NFC DISCONNECTED");
                            intent2.putExtra(ATTENDANCE_DAY, dbArrDay[0]);
                            intent2.putExtra(ATTENDANCE_TIME, new SimpleDateFormat("HH:mm").format(new Date()));
                            intent2.putExtra(ATTENDANCE_TAG, attendance);
                            intent2.putExtra(ATTENDANCE_INDEX, sbj_index);
                            startActivityForResult(intent2, 2);

                        }

                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }

                } else if (dbArrDay[x].equals(period)) {
                    attendance = "출석";
                    try {
                        ndefTag.connect();

                        if (ndefTag.isConnected()) {
                            Log.i("Second", ": connecting exit.");
                            Intent intent2 = new Intent(NfcActivity.this, PopupActivity.class);
                            intent.putExtra("NFC Data", "NFC DISCONNECTED");
                            intent2.putExtra(ATTENDANCE_TAG, attendance);
                            startActivityForResult(intent2, 2);
                        }
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                } else {
                    if (dbArrDay.length == x) {
                        periodCheck = false;
                    } else {
                        Toast.makeText(NfcActivity.this, "현재시간에 수강중인 과목이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}

