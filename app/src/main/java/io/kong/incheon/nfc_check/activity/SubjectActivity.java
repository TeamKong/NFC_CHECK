package io.kong.incheon.nfc_check.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import io.kong.incheon.nfc_check.R;
import io.kong.incheon.nfc_check.adapter.ListViewAdapter;

public class SubjectActivity extends AppCompatActivity implements ListViewAdapter.ListBtnClickListener {

    static final String TAG = SubjectActivity.class.getCanonicalName();
    static final String TAG_JSON = "subject_table";
    static final String TAG_NAME = "sbj_name";
    static final String TAG_DAY = "sbj_day";
    static final String TAG_PROFESSOR = "sbj_professor";

    ArrayList<HashMap<String, String>> mArrayList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        listView = (ListView) findViewById(R.id.subject_listView);
        mArrayList = new ArrayList<>();

        GetData task = new GetData();
        task.execute("http://13.209.75.255:3000/subject_table");
    }

    private class GetData extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SubjectActivity.this,
                    "Please Wait", null, true, true);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - \n" + result);

            if (result == null) {
                Log.d(TAG, "response Error - \n" + errorString);
            } else {
                showResult(result);
            }
        }

        @Override
        protected  String doInBackground(String... params) {
            String serverURL = params[0];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - \n" + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuffer sb = new StringBuffer();
                String line;

                while((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error : ", e);
                errorString = e.toString();

                return null;
            }
        }

    }

    private void showResult(String result) {
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
    }

    @Override
    public void onListBtnClick(int position) {
        Toast.makeText(this, Integer.toString(position + 1) + "아이템 선택", Toast.LENGTH_SHORT).show();
    }
}