package io.kong.incheon.nfc_check.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.kong.incheon.nfc_check.R;
import io.kong.incheon.nfc_check.activity.SubjectActivity;
import io.kong.incheon.nfc_check.item.ListViewBtnItem;
import io.kong.incheon.nfc_check.item.UserItem;
import io.kong.incheon.nfc_check.service.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListViewAdapter extends ArrayAdapter implements View.OnClickListener {

    static final String TAG_URL = "http://13.209.75.255:3000";
    static final String TAG_JSON = "person_subject";

    ListViewBtnItem listViewItem;
    int resourceId;
    Context context;

    private Retrofit retrofit;

    String user_id;
    String stName;
    String stDay;
    String stIndex;
    String stProfessor;

    public ListViewAdapter(Context context, int resource, ArrayList<ListViewBtnItem> list) {
        super(context, resource, list);

        this.context = context;
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId, parent, false);
        }

        final TextView txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
        final TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);
        final TextView txtProfessor = (TextView) convertView.findViewById(R.id.txtProfessor);
        final TextView txtIndex = (TextView) convertView.findViewById(R.id.txtIndex);

        listViewItem = (ListViewBtnItem) getItem(position);

        txtTitle.setText(listViewItem.getTextTitle());
        txtDate.setText(listViewItem.getTxtDate());
        txtProfessor.setText(listViewItem.getTxtProfessor());
        txtIndex.setText(listViewItem.getTxtIndex());


        Button btnSubject = (Button) convertView.findViewById(R.id.btnSubject);
        btnSubject.setTag(position);
        btnSubject.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View view) {
        View v = (View) view.getParent();
        final UserItem userItem = new UserItem();

        final TextView txtName = (TextView) v.findViewById(R.id.txtTitle);
        final TextView txtDay = (TextView) v.findViewById(R.id.txtDate);
        final TextView txtProfessor = (TextView) v.findViewById(R.id.txtProfessor);
        final TextView txtIndex = (TextView) v.findViewById(R.id.txtIndex);

        user_id = userItem.getStid();
        stName = txtName.getText().toString();
        stDay = txtDay.getText().toString();
        stIndex = txtIndex.getText().toString();
        stProfessor = txtProfessor.getText().toString();

        AlertDialog.Builder oDialog = new AlertDialog.Builder(context);
        oDialog.setMessage(stName + "과목을 추가하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        insertPersonSubject();
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

    private void insertPersonSubject() {
        retrofit = new Retrofit.Builder()
                .baseUrl(TAG_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = service.person_subject(user_id, stName, stIndex, stProfessor, stDay);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.message() == "success") {
                        Toast.makeText(context.getApplicationContext(), "추가완료", Toast.LENGTH_SHORT).show();
                        SubjectActivity subjectActivity = (SubjectActivity) SubjectActivity.subjectActivity;
                        subjectActivity.finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), "DB failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
