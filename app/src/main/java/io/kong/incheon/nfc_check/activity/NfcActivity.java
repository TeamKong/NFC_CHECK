package io.kong.incheon.nfc_check.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import io.kong.incheon.nfc_check.R;
import io.kong.incheon.nfc_check.item.NFCItem;
import io.kong.incheon.nfc_check.item.TimeItem;
import io.kong.incheon.nfc_check.item.UserItem;

//NFC 태그 값을 출력하는 코드입니다.
//학생이 태그에 핸드폰을 접촉하면 출석이 되는 형식

public class NfcActivity extends Activity {

    static final String TAG = TimeTableActivity.class.getCanonicalName();

    static TimeItem timeItem = new TimeItem();
    static long curSecond;

    NFCItem nfcItem = new NFCItem();

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    public static String tagNum = null;
    private TextView tagUserName;
    private TextView tagUserId;
    private TextView tagUserMajor;
    private TextView tagDesc;
    private TextView noticetxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        tagUserName = findViewById(R.id.student_name);
        tagUserId = findViewById(R.id.student_number);
        noticetxt = findViewById(R.id.notice_txt);
        tagDesc = findViewById(R.id.tagDesc);
        tagUserMajor = findViewById(R.id.student_major);

        tagUserName.setText(UserItem.getUser_name());
        tagUserId.setText(UserItem.getStid());
        tagUserMajor.setText(UserItem.getUser_major());


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        final Ndef ndefTag = Ndef.get(tag);


        if (tag != null) {
            curSecond = System.currentTimeMillis();
            timeItem.setSecondTime(curSecond); //찍은 직후의 시간 밀리초를 넣엇당..
            Log.i("curSecond", ""+curSecond);


            byte[] tagId = tag.getId();
            tagDesc.setText("Tag ID: " + toHexString(tagId));
            tagNum = toHexString(tagId);
            nfcItem.setTagNum(toHexString(tagId));
        }

        try {
            ndefTag.connect();
            NoticePrint(noticetxt);

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

    public static final String CHARS = "0123456789ABCDEF";

    public static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            sb.append(CHARS.charAt((data[i] >> 4) & 0x0F)).append(
                    CHARS.charAt(data[i] & 0x0F));
        }
        return sb.toString();
    }

    public void NoticePrint(TextView text) {
        text.setText("출석이 완료 되었습니다. \n누적을 위해 태그에 계속 접촉해주세요.");
    }

}
