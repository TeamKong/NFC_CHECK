package io.kong.incheon.nfc_check.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import io.kong.incheon.nfc_check.R;

//NFC 태그 값을 출력하는 코드입니다.
//학생이 태그에 핸드폰을 접촉하면 출석이 되는 형식

public class NfcActivity extends Activity{

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    public static String tagNum=null;
    private TextView tagDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        tagDesc = findViewById(R.id.tagDesc);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


    }

   @Override
   protected void onResume(){
        super.onResume();
        if(nfcAdapter != null){
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null,null);
        }
   }

   @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        Tag tag= intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(tag != null){
            byte[] tagId= tag.getId();
            tagDesc.setText("Tag ID: "+toHexString(tagId));
            tagNum = toHexString(tagId);
        }

       if(tagNum != null){
            Intent intent1 = new Intent(this, PopUpActivity.class);
            intent.putExtra("NFC Data", "NFC CONTACT SUCCESS");
            startActivityForResult(intent1, 1);
        }

   }
   public static final String CHARS = "0123456789ABCDEF";

    public static String toHexString(byte[] data){
        StringBuilder sb= new StringBuilder();
        for (int i=0; i<data.length; i++){
            sb.append(CHARS.charAt((data[i]>>4)& 0x0F)).append(
                    CHARS.charAt(data[i] & 0x0F));
        }
        return sb.toString();
    }


}
