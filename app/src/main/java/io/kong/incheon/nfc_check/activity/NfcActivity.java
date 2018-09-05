package io.kong.incheon.nfc_check.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import io.kong.incheon.nfc_check.R;
import io.kong.incheon.nfc_check.item.NFCItem;
import io.kong.incheon.nfc_check.item.TimeItem;
import io.kong.incheon.nfc_check.item.UserItem;
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

    static TimeItem timeItem = new TimeItem();
    static long curSecond;


    private Retrofit retrofit;

    NFCItem nfcItem = new NFCItem();

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
        final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        final Ndef ndefTag = Ndef.get(tag);


        if (tag != null) {
            curSecond = System.currentTimeMillis();
            timeItem.setSecondTime(curSecond); //찍은 직후의 시간 밀리초를 넣엇당..
            Log.i("curSecond", "" + curSecond);

            byte[] tagId = tag.getId();
            tagDesc.setText("Tag ID: " + toHexString(tagId));
            tagNum = toHexString(tagId);
            nfcItem.setTagNum(toHexString(tagId));

            oCalendar = Calendar.getInstance();
            final String[] week = {"월", "화", "수", "목", "금", "토", "일"};
            todayWeek = week[oCalendar.get(Calendar.DAY_OF_WEEK) - 2];


            RetrofitService service = retrofit.create(RetrofitService.class);
            Call<ResponseBody> call = service.NFC_check(toHexString(tag.getId()), user_id);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String result = response.body().string();
                            if (!result.equals("[]")) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    JSONArray jsonArray = jsonObject.getJSONArray("NFC_check");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject item = jsonArray.getJSONObject(i);

                                        dbDay = item.getString("sbj_day");

                                        int pos = dbDay.indexOf(",");
                                        dbArrDay = dbDay.split(",");

                                        if (!Integer.toString(pos).equals("-1")) {
                                            String[] firDay = dbArrDay[0].split(" ");
                                            String[] secDay = dbArrDay[1].split(" ");

                                            if (firDay[0].equals(todayWeek)) {
                                                CallPopUpActivity(intent, ndefTag);
                                            } else if (secDay[0].equals(todayWeek)) {
                                                CallPopUpActivity(intent, ndefTag);
                                            } else {
                                                Toast.makeText(NfcActivity.this, "오늘 수업 없음", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            String[] singleDay = dbDay.split(" ");
                                            if (singleDay.equals(todayWeek)) {
                                                CallPopUpActivity(intent, ndefTag);
                                            } else {
                                                Toast.makeText(NfcActivity.this, "오늘 수업 없음", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(NfcActivity.this, "없음", Toast.LENGTH_SHORT).show();
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

    private void CallPopUpActivity(Intent intent, Ndef ndefTag) {
        try {
            ndefTag.connect();


            while (ndefTag.isConnected()) {
            }

            ndefTag.close();
            Log.i("Second", ": connecting exit.");

            Intent intent2 = new Intent(NfcActivity.this, PopupActivity.class);
            intent.putExtra("NFC Data", "NFC DISCONNECTED");
            startActivityForResult(intent2, 2);

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }
}
