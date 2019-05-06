package com.example.yantu.androidproject;
/*李洋*/
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
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
import com.example.yantu.androidproject.Entity.Log;

import java.util.List;

public class EditHobbyActivity extends AppCompatActivity {

    private EditText etHobbyName;
    private ListView listView;
    private Spinner spTime;
    private EditText etHobbyCycle;
    private Button btnEditHobby;
    private Button btnResetHobby;
    private int spinnerPosition = 0;
    private TextView tvHobbyTitle;
    private MyDatabaseHelper dbHelper;
    private String choice;
    private Hobby hobby = null;
    private String[] spinnerItems = {"任意时间","早上习惯","下午习惯","晚间习惯"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_hobby_activity);

        //隐藏系统标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        // 接收参数，添加只包含choice参数，修改还包含回显数据
        Intent intent = new Intent();
        try {
            choice = intent.getStringExtra("choice");
            if(choice.equals("update"))
                hobby = (Hobby)intent.getSerializableExtra("Hobby");

        }catch (Exception e){
            choice = "insert";
        }

        init();
        createDatabase();
    }

    public void init(){
        // 初始化控件
        etHobbyName = findViewById(R.id.etHobbyName);
        etHobbyCycle = findViewById(R.id.etHobbyCycle);
        spTime = findViewById(R.id.spTime);
        btnEditHobby = findViewById(R.id.btnEditHobby);
        btnResetHobby = findViewById(R.id.btnResetHobby);
        tvHobbyTitle = findViewById(R.id.tvHobbyTitle);

        // 动态改变标题
        if(choice.equals("edit")){
            tvHobbyTitle.setText("修改习惯");
            etHobbyName.setText(hobby.getHbName());
            etHobbyCycle.setText(hobby.getHbCycle());
        }else if(choice.equals("add")){
            tvHobbyTitle.setText("添加习惯");
        }

        // button
        btnEditHobby.setOnClickListener(editListener);
        btnResetHobby.setOnClickListener(resetListener);

        // Spinner
        ArrayAdapter<String > spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTime.setAdapter(spinnerAdapter);
        spTime.setOnItemSelectedListener(spinnerListener);
    }

    // 创建数据库
    public void createDatabase(){
        dbHelper = new MyDatabaseHelper(this, "yantu.db", null, 1);
        dbHelper.getWritableDatabase();
    }

    // 添加/修改习惯数据库表
    public int editHobby(String hbName, Integer icId, String hbTime, Integer hbCycle, String choice){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hbName", hbName);
        values.put("icId", icId);
        values.put("hbTime", hbTime);
        values.put("hbCycle", hbCycle);
        int result = 0;
        if(choice.equals("insert"))
            result = (int)db.insert("Hobby", null, values);
        else if(choice.equals("update"))
            result = db.update("Hobby",values, "hbId=?", new String[]{String.valueOf(hobby.getHbId())});
        android.util.Log.i("result", "editHobby: " + String.valueOf(result));
        values.clear();
        return result;
    }

    // 添加/修改日志数据库表
    public int editLog(Integer hbId, Integer lgTotal, Integer lgContinue){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hbId", hbId);
        values.put("lgTotal", lgTotal);
        values.put("lgContinue", lgContinue);
        long logid = db.insert("Log", null, values);
        android.util.Log.i("result", "editLog: " + String.valueOf(logid));
        values.clear();
        return (int)logid;
    }

    // 编辑按钮监听事件
    public View.OnClickListener editListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(String.valueOf(etHobbyName.getText()).equals("")||String.valueOf(etHobbyCycle.getText()).equals("")){
                Toast.makeText(EditHobbyActivity.this, "您还有未填写字段。", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = String.valueOf(etHobbyName.getText());
            Integer icId = 1; // iconid 需要修改
            String hbTime = String.valueOf(spinnerPosition);
            Integer hbCycle = Integer.getInteger(String.valueOf(etHobbyCycle.getText()));

            if(choice.equals("insert")){
                int hobbyid = editHobby(name, icId, hbTime, hbCycle, "insert");
                if(-1 == hobbyid)
                    Toast.makeText(EditHobbyActivity.this, "添加习惯失败！", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(EditHobbyActivity.this, "添加习惯成功！", Toast.LENGTH_SHORT).show();

                int logid =  editLog(hobbyid, 1,2);
                if(-1 == logid)
                    Toast.makeText(EditHobbyActivity.this, "添加打卡日志失败！", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(EditHobbyActivity.this, "添加打卡日志成功！", Toast.LENGTH_SHORT).show();
            }

            else if(choice.equals("update")){
                int updateRes = editHobby(name, icId, hbTime, hbCycle, "update");
                if(-1 == updateRes)
                    Toast.makeText(EditHobbyActivity.this, "修改习惯信息失败！", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(EditHobbyActivity.this, "修改习惯信息成功！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // 重置按钮监听事件
    public View.OnClickListener resetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            etHobbyName.setText("");
            etHobbyCycle.setText("");
        }
    };

    // Spinner监听事件
    public AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(EditHobbyActivity.this, spinnerItems[position], Toast.LENGTH_SHORT).show();
            spinnerPosition = position;
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
