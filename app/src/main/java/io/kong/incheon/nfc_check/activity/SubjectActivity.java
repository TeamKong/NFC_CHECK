package io.kong.incheon.nfc_check.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

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

    static final String TAG_NAME = "sbj_name";
    static final String TAG_DAY = "sbj_day";
    static final String TAG_PROFESSOR = "sbj_professor";
    static final String TAG_INDEX = "sbj_index";

    ArrayList<ListViewBtnItem> mArrayList;
    ListViewAdapter adapter;
    ListView listView;

    Spinner spnSbjDivision;
    Spinner spnSbjCategory;
    EditText edSbjName;
    Button btnSbjSearch;

    String stSbjDivision = "전체";
    String stSbjCategory;
    String stSbjName;
    String[] stArrDivision;
    String[] stArrCategory;
    ArrayAdapter<String> spnCatAdapter;
    ArrayAdapter<String> spnDvAdapter;


    private Retrofit retrofit;
    RetrofitService service;
    JSONObject item;
    ListViewBtnItem items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullScreenView fullScreenView = new FullScreenView();
        fullScreenView.screenView(this);
        setContentView(R.layout.activity_subject);

        init();


        stArrDivision = getResources().getStringArray(R.array.division);
        spnDvAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stArrDivision);
        spnSbjDivision.setAdapter(spnDvAdapter);
        spnSbjDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stSbjDivision = adapterView.getItemAtPosition(i).toString();
                if (spnDvAdapter.getItem(i).equals("전체")) {
                    stArrCategory = getResources().getStringArray(R.array.category);
                } else if (spnDvAdapter.getItem(i).equals("교양")) {
                    stArrCategory = getResources().getStringArray(R.array.category_culture);
                } else {
                    stArrCategory = getResources().getStringArray(R.array.category_major);
                }

                spnCatAdapter = new ArrayAdapter<String>(SubjectActivity.this, android.R.layout.simple_spinner_dropdown_item, stArrCategory);
                spnSbjCategory.setAdapter(spnCatAdapter);
                spnSbjCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        stSbjCategory = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        stSbjCategory = "전체";
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                stSbjDivision = "전체";
            }
        });

        btnSbjSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (stSbjDivision.equals("전체")) {
                    stSbjDivision = "%%";
                }

                if (stSbjCategory.equals("전체")) {
                    stSbjCategory = "%%";
                }

                stSbjName = "%" + edSbjName.getText().toString()+ "%";
                stSbjDivision = "%" + stSbjDivision + "%";

                Call<ResponseBody> call = service.subject_table(stSbjDivision,stSbjCategory,stSbjName);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {

                                String result = response.body().string();

                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    JSONArray jsonArray = jsonObject.getJSONArray("subject_table");
                                    mArrayList = new ArrayList<ListViewBtnItem>();

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
        });
    }

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    protected void init() {

        subjectActivity = SubjectActivity.this;
        listView = (ListView) findViewById(R.id.subject_listView);
        mArrayList = new ArrayList<>();

        spnSbjDivision = (Spinner) findViewById(R.id.spnSbjDivision);
        spnSbjCategory = (Spinner) findViewById(R.id.spnSbjCategory);
        edSbjName = (EditText) findViewById(R.id.edSbjName);

        btnSbjSearch = (Button) findViewById(R.id.btnSbjSearch);

        retrofit = new Retrofit.Builder()
                .baseUrl(TAG_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(RetrofitService.class);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SubjectActivity.this, TimeTableActivity.class);
        startActivity(intent);
        finish();
    }
}