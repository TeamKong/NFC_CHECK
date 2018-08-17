package io.kong.incheon.nfc_check.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import io.kong.incheon.nfc_check.service.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubjectActivity extends AppCompatActivity implements ListViewAdapter.ListBtnClickListener {

    static final String TAG = SubjectActivity.class.getCanonicalName();
    static final String TAG_URL = "http://13.209.207.99:3000";
    static final String TAG_JSON = "subject_table";
    static final String TAG_NAME = "sbj_name";
    static final String TAG_DAY = "sbj_day";
    static final String TAG_PROFESSOR = "sbj_professor";

    ArrayList<HashMap<String, String>> mArrayList;
    ListView listView;

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

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

                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject item = jsonArray.getJSONObject(i);

                                String stName = item.getString(TAG_NAME);
                                String stDay = item.getString(TAG_DAY);
                                String stProfessor = item.getString(TAG_PROFESSOR);

                                HashMap<String, String> hashMap = new HashMap<>();

                                hashMap.put(TAG_NAME, stName);
                                hashMap.put(TAG_DAY, stDay);
                                hashMap.put(TAG_PROFESSOR, stProfessor);

                                mArrayList.add(hashMap);
                            }

                            ListAdapter adapter = new SimpleAdapter(SubjectActivity.this, mArrayList, R.layout.listview_item,
                                    new String[]{TAG_NAME, TAG_DAY, TAG_PROFESSOR} , new int[]{R.id.txtTitle, R.id.txtDate, R.id.txtProfessor} );
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

            }
        });

       // GetData task = new GetData();
       // task.execute("http://13.209.75.255:3000/subject_table");
    }

    @Override
    public void onListBtnClick(int position) {
        Toast.makeText(SubjectActivity.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
    }
}