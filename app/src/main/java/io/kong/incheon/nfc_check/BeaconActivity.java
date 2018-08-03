package io.kong.incheon.nfc_check;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;


import java.util.List;
import java.util.UUID;

public class BeaconActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private Region region;
    private TextView test;

    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        test = (TextView) findViewById(R.id.test);
        beaconManager = new BeaconManager(this);

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    Log.d("Airport", "Nearest Places: " + nearestBeacon.getRssi());
                    test.setText(nearestBeacon.getRssi() + "");


                    if(!isConnected && nearestBeacon.getRssi() >- 70) {
                        isConnected=true;

                        AlertDialog.Builder dialog = new AlertDialog.Builder(BeaconActivity.this);
                        dialog.setTitle("알림")
                                .setMessage("비콘연결")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).create().show();
                        }
                        else{
                        Toast.makeText(BeaconActivity.this,"연결종료", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        region = new Region("Renged region", UUID.fromString(""), null, null);
        //비콘 아이디 쓰기

    }

    @Override
    protected void onResume(){
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
    }
}
