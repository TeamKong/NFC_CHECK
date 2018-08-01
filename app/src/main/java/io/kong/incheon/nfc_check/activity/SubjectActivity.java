package io.kong.incheon.nfc_check.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import io.kong.incheon.nfc_check.R;
import io.kong.incheon.nfc_check.adapter.ListViewAdapter;
import io.kong.incheon.nfc_check.item.ListViewBtnItem;

public class SubjectActivity extends AppCompatActivity implements ListViewAdapter.ListBtnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        ListView listView;
        ListViewAdapter adapter;
        ArrayList<ListViewBtnItem> items = new ArrayList<ListViewBtnItem>();

        loadItemsFromDB(items);

        adapter = new ListViewAdapter(this, R.layout.listview_item, items, this);

        listView = (ListView) findViewById(R.id.subject_listView);
        listView.setAdapter(adapter);
    }

    @Override
    public void onListBtnClick(int position) {
        Toast.makeText(this, Integer.toString(position + 1) + "아이템 선택", Toast.LENGTH_SHORT).show();
    }

    public boolean loadItemsFromDB(ArrayList<ListViewBtnItem> list) {
        ListViewBtnItem item;
        int i;

        if (list == null) {
            list = new ArrayList<ListViewBtnItem>();
        }

        i = 1;

        item = new ListViewBtnItem();
        item.setTextTitle(Integer.toString(i) + "번 아이템입니다.");
        item.setTxtDate(Integer.toString(i) + "월");
        list.add(item) ;
        i++ ;


        item = new ListViewBtnItem();
        item.setTextTitle(Integer.toString(i) + "번 아이템입니다.");
        item.setTxtDate(Integer.toString(i) + "월");
        list.add(item) ;
        i++ ;


        item = new ListViewBtnItem();
        item.setTextTitle(Integer.toString(i) + "번 아이템입니다.");
        item.setTxtDate(Integer.toString(i) + "월");
        list.add(item) ;
        i++ ;

        return true;
    }
}