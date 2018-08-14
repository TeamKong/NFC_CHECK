package io.kong.incheon.nfc_check.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import io.kong.incheon.nfc_check.R;
import io.kong.incheon.nfc_check.adapter.SettingAdapter;
import static io.kong.incheon.nfc_check.activity.MainActivity.appData;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ListView listview;
        SettingAdapter adapter;

        // Adapter 생성
        adapter = new SettingAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_launcher_background),
                "내정보");
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground),
                "로그아웃");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        break;
                    case 1:
                        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                        FirstMenuActivity firstMenuActivity = (FirstMenuActivity)FirstMenuActivity.firstMenuActivity;
                        
                        startActivity(intent);
                        SharedPreferences.Editor editor = appData.edit();
                        editor.clear();
                        editor.commit();
                        firstMenuActivity.finish();
                        Toast.makeText(SettingActivity.this,"로그아웃",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }

            }
        });
    }
}
