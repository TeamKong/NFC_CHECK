package io.kong.incheon.nfc_check.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import io.kong.incheon.nfc_check.R;
import io.kong.incheon.nfc_check.service.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.kong.incheon.nfc_check.service.RetrofitService.TAG_URL;

public class SignupActivity extends AppCompatActivity {

    Spinner sUniversity;
//    Spinner sGrade;
    Button btnSummit;
    EditText edID;
    EditText edPW;
    EditText edName;
    EditText edMajor;

    ArrayAdapter saUniversity;
//    ArrayAdapter saGrade;

    Retrofit retrofit;

    String user_id;
    String user_pw;
    String user_name;
    String user_university;
    String user_major;
//    String user_grade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        init();

        saUniversity = ArrayAdapter.createFromResource(this, R.array.question, android.R.layout.simple_spinner_dropdown_item);
        sUniversity.setAdapter(saUniversity);

//        saGrade = ArrayAdapter.createFromResource(this,R.array.grade, android.R.layout.simple_spinner_dropdown_item);
//        sGrade.setAdapter(saGrade);

        sUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                user_university = saUniversity.getItem(position).toString();
            }
            public void onNothingSelected(AdapterView<?>  parent) {

            }
        });

//        sGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                user_grade = saGrade.getItem(position).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        btnSummit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertUser();
            }
        });
    }

    private void init() {

        retrofit = new Retrofit.Builder()
                .baseUrl(TAG_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        btnSummit = (Button) findViewById(R.id.btnSummit);
        edID = (EditText) findViewById(R.id.edID);
        edPW = (EditText) findViewById(R.id.edPW);
        edName = (EditText) findViewById(R.id.edName);
        edMajor = (EditText) findViewById(R.id.edMajor);
        sUniversity = (Spinner) findViewById(R.id.box_school);
//        sGrade = (Spinner) findViewById(R.id.spiner_Grade);

    }

    private void insertUser() {

        user_id = edID.getText().toString();
        user_pw = edPW.getText().toString();
        user_name = edName.getText().toString();
        user_major = edMajor.getText().toString();

        RetrofitService service = retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = service.join(user_id, user_pw, user_university, user_name, user_major);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, user_id + "님 회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SignupActivity.this, R.string.db_failure, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
