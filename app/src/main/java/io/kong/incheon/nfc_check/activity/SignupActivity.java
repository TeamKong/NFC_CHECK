package io.kong.incheon.nfc_check.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import io.kong.incheon.nfc_check.R;

public class SignupActivity extends AppCompatActivity {

    Spinner spinner;
    ArrayAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        spinner = (Spinner) findViewById(R.id.box_school);
        sAdapter = ArrayAdapter.createFromResource(this, R.array.question, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), sAdapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

    }
}
