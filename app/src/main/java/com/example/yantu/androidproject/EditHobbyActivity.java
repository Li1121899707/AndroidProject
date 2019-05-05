package com.example.yantu.androidproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yantu.androidproject.DBHelper.MyDatabaseHelper;
import com.example.yantu.androidproject.Entity.Hobby;

import java.util.List;

public class EditHobbyActivity extends AppCompatActivity {

    private EditText etHobbyName;
    private ListView listView;
    private Spinner spTime;
    private EditText etHobbyCycle;
    private Button btnEditHobby;
    private Button btnResetHobby;
    private int spinnerString;
    private TextView tvHobbyTitle;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_hobby_activity);

        Intent intent = new Intent();
        Hobby hobby = (Hobby)intent.getSerializableExtra("Hobby");
        String choice = intent.getStringExtra("choice");
        init(choice);

    }

    public void init(String choice){
        etHobbyName = findViewById(R.id.etHobbyName);
        etHobbyCycle = findViewById(R.id.etHobbyCycle);
        spTime = findViewById(R.id.spTime);
        btnEditHobby = findViewById(R.id.btnEditHobby);
        btnResetHobby = findViewById(R.id.btnResetHobby);
        tvHobbyTitle = findViewById(R.id.tvHobbyTitle);
        if(choice.equals("edit")){
            tvHobbyTitle.setText("修改习惯");
        }else if(choice.equals("add")){
            tvHobbyTitle.setText("添加习惯");
        }
        
        btnEditHobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEditHobbyClick();
            }
        });

        btnResetHobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnResetHobbyClick();
            }
        });

        // Spinner
        final String[] spinnerItems = {"任意时间","早上习惯","下午习惯","晚间习惯"};
        ArrayAdapter<String > spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spTime.setAdapter(spinnerAdapter);
        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(EditHobbyActivity.this, spinnerItems[position], Toast.LENGTH_SHORT).show();
                spinnerString = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void createDatabase(){
        dbHelper = new MyDatabaseHelper(this, "yantu.db", null, 1);
        dbHelper.getWritableDatabase();
    }

    public void btnEditHobbyClick() {
        if(etHobbyName.getText().equals("")||etHobbyCycle.getText().equals("")){
            Toast.makeText(EditHobbyActivity.this, "您还有未填写字段。", Toast.LENGTH_SHORT);
            return;
        }
        insertToHobby(String.valueOf(etHobbyName.getText()), 1, "2019-05-06", 5);

    }


    public void btnResetHobbyClick() {
        etHobbyName.setText("");
        etHobbyCycle.setText("");
    }

    public void insertToHobby(String hbName, Integer icId, String hbTime, Integer hbCycle){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hbName", hbName);
        values.put("icId", icId);
        values.put("hbTime", hbTime);
        values.put("hbCycle", hbCycle);
        db.insert("Hobby", null, values);
        values.clear();
    }

    public void insertToLog(Integer hbId, Integer lgTotal, Integer lgContinue){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hbId", hbId);
        values.put("lgTotal", lgTotal);
        values.put("lgContinue", lgContinue);
        db.insert("Log", null, values);
        values.clear();
    }
}
