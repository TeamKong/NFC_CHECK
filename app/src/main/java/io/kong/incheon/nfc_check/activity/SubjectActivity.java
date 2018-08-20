package io.kong.incheon.nfc_check.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import io.kong.incheon.nfc_check.R;
import io.kong.incheon.nfc_check.adapter.ListViewAdapter;
import io.kong.incheon.nfc_check.item.ListViewBtnItem;
import io.kong.incheon.nfc_check.service.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.kong.incheon.nfc_check.service.RetrofitService.TAG_URL;

public class SubjectActivity extends AppCompatActivity{

    public static Activity subjectActivity;

    static final String TAG = SubjectActivity.class.getCanonicalName();
    static final String TAG_JSON = "subject_table";
    static final String TAG_NAME = "sbj_name";
    static final String TAG_DAY = "sbj_day";
    static final String TAG_PROFESSOR = "sbj_professor";
    static final String TAG_INDEX = "sbj_index";

    ArrayList<ListViewBtnItem> mArrayList;
    ListViewAdapter adapter;
    ListView listView;

    private Retrofit retrofit;
    JSONObject item;
    ListViewBtnItem items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        subjectActivity = SubjectActivity.this;
        listView = (ListView) findViewById(R.id.subject_listView);
        mArrayList = new ArrayList<>();


        retrofit = new Retrofit.Builder()
                .baseUrl(TAG_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = service.subject_table(TAG_JSON);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                            if(mArrayList == null) {
                                mArrayList = new ArrayList<ListViewBtnItem>();
                            }

                            for(int i = 0; i < jsonArray.length(); i++) {

                                item = jsonArray.getJSONObject(i);
                                items = new ListViewBtnItem();

                                String stName = item.getString(TAG_NAME);
                                String stDay = item.getString(TAG_DAY);
                                String stProfessor = item.getString(TAG_PROFESSOR);
                                String stIndex = item.getString(TAG_INDEX);

                                items.setTextTitle(stName);
                                items.setTxtDate(stDay);
                                items.setTxtProfessor(stProfessor);
                                items.setTxtIndex(stIndex);

                                mArrayList.add(items);
                            }

                            adapter = new ListViewAdapter(SubjectActivity.this, R.layout.listview_item, mArrayList);
                            listView.setAdapter(adapter);

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
                Toast.makeText(SubjectActivity.this, R.string.db_failure, Toast.LENGTH_SHORT).show();
            }
        });
    }

}