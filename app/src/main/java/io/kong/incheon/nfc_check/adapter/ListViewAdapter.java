package io.kong.incheon.nfc_check.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.kong.incheon.nfc_check.R;
import io.kong.incheon.nfc_check.activity.SubjectActivity;
import io.kong.incheon.nfc_check.activity.TimeTableActivity;
import io.kong.incheon.nfc_check.item.ListViewBtnItem;
import io.kong.incheon.nfc_check.item.UserItem;
import io.kong.incheon.nfc_check.service.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.kong.incheon.nfc_check.service.RetrofitService.TAG_URL;

public class ListViewAdapter extends ArrayAdapter implements View.OnClickListener {

    static final String TAG = SubjectActivity.class.getCanonicalName();

    ListViewBtnItem listViewItem;
    int resourceId;
    Context context;

    private Retrofit retrofit;
    JSONObject item;
    JSONArray jsonArray;

    String user_id;
    String stName;
    String stDay;
    String stIndex;
    String stProfessor;

    boolean doubleCheck = true;

    String[] dbGetDayArr = new String[1];
    String[] stGetDayArr = new String[1];
    String[] getDoubleCheck = new String[20];

    public ListViewAdapter(Context context, int resource, ArrayList<ListViewBtnItem> list) {
        super(context, resource, list);

        this.context = context;
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId, parent, false);
        }

        final TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        final TextView txtDate = convertView.findViewById(R.id.txtDate);
        final TextView txtProfessor = convertView.findViewById(R.id.txtProfessor);
        final TextView txtIndex = convertView.findViewById(R.id.txtIndex);

        listViewItem = (ListViewBtnItem) getItem(position);

        txtTitle.setText(listViewItem.getTextTitle());
        txtDate.setText(listViewItem.getTxtDate());
        txtProfessor.setText(listViewItem.getTxtProfessor());
        txtIndex.setText(listViewItem.getTxtIndex());


        Button btnSubject = convertView.findViewById(R.id.btnSubject);
        btnSubject.setTag(position);
        btnSubject.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View view) {
        doubleCheck = true;
        View v = (View) view.getParent();
        final UserItem userItem = new UserItem();

        final TextView txtName = v.findViewById(R.id.txtTitle);
        final TextView txtDay = v.findViewById(R.id.txtDate);
        final TextView txtProfessor = v.findViewById(R.id.txtProfessor);
        final TextView txtIndex = v.findViewById(R.id.txtIndex);

        user_id = userItem.getStid();
        stName = txtName.getText().toString();
        stDay = txtDay.getText().toString();
        stIndex = txtIndex.getText().toString();
        stProfessor = txtProfessor.getText().toString();
        retrofit = new Retrofit.Builder()
                .baseUrl(TAG_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AlertDialog.Builder oDialog = new AlertDialog.Builder(context);
        oDialog.setMessage(stName + "과목을 추가하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RetrofitService searchService = retrofit.create(RetrofitService.class);
                        Call<ResponseBody> searchCall = searchService.person_subjectTable(user_id);
                        searchCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    String result = response.body().string();
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        jsonArray = jsonObject.getJSONArray("person_subject");
                                        if (jsonArray.length() == 0) {
                                            insertPersonSubject();
                                        }
                                        for (int j = 0; j < jsonArray.length(); j++) {

                                            item = jsonArray.getJSONObject(j);
                                            getDoubleCheck[j] = item.getString("sbj_name");
                                            if (getDoubleCheck[j].equals(stName)) {
                                                Toast.makeText(context.getApplicationContext(), "같은 과목은 시간표에 입력이 불가합니다.", Toast.LENGTH_SHORT).show();
                                                doubleCheck = false;
                                            }

                                        }

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            if (doubleCheck) {
                                                item = jsonArray.getJSONObject(i);
                                                String dbGetDay = item.getString("sbj_day");

                                                int dbPos = dbGetDay.indexOf(",");
                                                dbGetDayArr = dbGetDay.split(",");

                                                int stPos = stDay.indexOf(",");
                                                stGetDayArr = stDay.split(",");

                                                if (stDay.equals(dbGetDay)) {
                                                    Toast.makeText(context.getApplicationContext(), "시간이 중복됩니다.", Toast.LENGTH_SHORT).show();
                                                    doubleCheck = false;
                                                } else if (!(Integer.toString(dbPos).equals("-1")) && !(Integer.toString(stPos).equals("-1"))) {
                                                    String[] DBfirDay = dbGetDayArr[0].split(" ");
                                                    String[] DBsecDay = dbGetDayArr[1].split(" ");

                                                    String[] stFirDay = stGetDayArr[0].split(" ");
                                                    String[] stSecDay = stGetDayArr[1].split(" ");

                                                    weekConfirmTest(DBfirDay, DBsecDay, stFirDay, stSecDay, i);

                                                } else if (!(Integer.toString(dbPos).equals("-1")) && (Integer.toString(stPos).equals("-1"))) {
                                                    String[] DBfirDay = dbGetDayArr[0].split(" ");
                                                    String[] DBsecDay = dbGetDayArr[1].split(" ");

                                                    String[] singleDay = stDay.split(" ");

                                                    weekConfirmTest(DBfirDay, DBsecDay, singleDay, null, i);

                                                } else if ((Integer.toString(dbPos).equals("-1")) && !(Integer.toString(stPos).equals("-1"))) {
                                                    String[] DBsingleDay = dbGetDay.split(" ");

                                                    String[] stFirDay = stGetDayArr[0].split(" ");
                                                    String[] stSecDay = stGetDayArr[0].split(" ");

                                                    weekConfirmTest(DBsingleDay, null, stFirDay, stSecDay, i);
                                                } else {
                                                    String[] DBsingleDay = dbGetDay.split(" ");

                                                    String[] singleDay = stDay.split(" ");

                                                    weekConfirmTest(DBsingleDay, null, singleDay, null, i);

                                                }
                                            }
                                        }


                                    } catch (JSONException e) {
                                        Log.d(TAG, "showResult : ", e);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(context.getApplicationContext(), R.string.db_failure, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog dialog = oDialog.create();
        dialog.show();

    }

    public void weekConfirmTest(String[] DBdayArr1, String[] DBdayArr2, String[] STdayArr1, String[] STdayArr2, int i) {
        if (DBdayArr2 == null && STdayArr2 == null) {
            if (DBdayArr1[0].equals(STdayArr1[0])) {
                dayConfirmTest(DBdayArr1, null, STdayArr1, null, i);
            } else {
                insertPersonSubject();
            }
        } else if (STdayArr2 == null) {
            if (DBdayArr1[0].equals(STdayArr1[0]) || DBdayArr2[0].equals(STdayArr1[0])) {
                dayConfirmTest(DBdayArr1, DBdayArr2, STdayArr1, null, i);
            } else {
                insertPersonSubject();
            }
        } else if (DBdayArr2 == null) {
            if (DBdayArr1[0].equals(STdayArr1[0]) || DBdayArr1[0].equals(STdayArr2[0])) {
                dayConfirmTest(DBdayArr1, null, STdayArr1, STdayArr2, i);
            } else {
                insertPersonSubject();
            }
        } else {
            if (DBdayArr1[0].equals(STdayArr1[0]) || DBdayArr1[0].equals(STdayArr2[0]) || DBdayArr2[0].equals(STdayArr1[0]) || DBdayArr2[0].equals(STdayArr2[0])) {
                dayConfirmTest(DBdayArr1, DBdayArr2, STdayArr1, STdayArr2, i);
            } else {
                insertPersonSubject();
            }
        }
    }

    public void dayConfirmTest(String[] DBdayArr1, String[] DBdayArr2, String[] STdayArr1, String[] STdayArr2, int i) {
        if (DBdayArr2 == null && STdayArr2 == null) {
            overlapSearchTest(DBdayArr1, STdayArr1, i);
        } else if (STdayArr2 == null) {
            if (DBdayArr1[0].equals(STdayArr1[0])) {
                overlapSearchTest(DBdayArr1, STdayArr1, i);
            } else {
                overlapSearchTest(DBdayArr2, STdayArr1, i);
            }
        } else if (DBdayArr2 == null) {
            if (DBdayArr1[0].equals(STdayArr1[0])) {
                overlapSearchTest(DBdayArr1, STdayArr1, i);
            } else {
                overlapSearchTest(DBdayArr1, STdayArr2, i);
            }
        } else {
            if (DBdayArr1[0].equals(STdayArr1[0])) {
                overlapSearchTest(DBdayArr1, STdayArr1, i);
            } else if (DBdayArr1[0].equals(STdayArr2[0])) {
                overlapSearchTest(DBdayArr1, STdayArr2, i);
            } else if (DBdayArr2[0].equals(STdayArr1[0])) {
                overlapSearchTest(DBdayArr2, STdayArr1, i);
            } else {
                overlapSearchTest(DBdayArr2, STdayArr2, i);
            }
        }

    }

    public void overlapSearchTest(String[] DBdayArr, String[] STdayArr, int i) {
        Loop:
        for (int x = 1; x < DBdayArr.length; x++) {
            for (int y = 1; y < STdayArr.length; y++) {
                if (i == jsonArray.length()) {
                    if (DBdayArr[x].equals(STdayArr[y])) {
                        doubleCheck = false;
                        Toast.makeText(context.getApplicationContext(), "시간이 중복됩니다.", Toast.LENGTH_SHORT).show();
                        break Loop;
                    } else {
                        doubleCheck = false;
                        insertPersonSubject();
                        break Loop;
                    }
                } else {
                    if (DBdayArr[x].equals(STdayArr[y])) {
                        doubleCheck = false;
                        Toast.makeText(context.getApplicationContext(), "시간이 중복됩니다.", Toast.LENGTH_SHORT).show();
                        break Loop;
                    }
                }
            }

        }
    }


    private void insertPersonSubject() {
        RetrofitService insertService = retrofit.create(RetrofitService.class);
        Call<ResponseBody> insertCall = insertService.person_subject(user_id, stName, stIndex, stProfessor, stDay);

        insertCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    SubjectActivity subjectActivity = (SubjectActivity) SubjectActivity.subjectActivity;
                    doubleCheck = false;
                    Intent intent = new Intent(subjectActivity, TimeTableActivity.class);
                    intent.putExtra("input", "insert");
                    subjectActivity.startActivity(intent);
                    subjectActivity.finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), R.string.db_failure, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
