package com.example.yantu.androidproject;
/*李洋*/

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yantu.androidproject.Adapter.EditRecycleAdapter;
import com.example.yantu.androidproject.DBHelper.MyDatabaseHelper;
import com.example.yantu.androidproject.Entity.Hobby;
import com.example.yantu.androidproject.Util.Utils;


import java.util.ArrayList;
import java.util.List;

public class EditHobbyActivity extends AppCompatActivity implements
        EditRecycleAdapter.OnItemClickListener {

    private EditText etHobbyName;
    private EditText etHobbyCycle;
    private ImageView selectedIcon;
    private RecyclerView recyclerView;

    private Integer spinnerPosition = 0;
    private Integer iconPosition = 0;
    private String choice;
    private Hobby hobby = null;
    private String[] spinnerItems = {"任意时间", "早上习惯", "下午习惯", "晚间习惯"};
    private List<String> iconList;

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_hobby_activity);
        Utils.setStatusBar(this, false, false);

        // 接收参数，添加只包含choice参数，修改还包含回显数据
        Intent intent = new Intent();
        try {
            choice = intent.getStringExtra("choice");
            if (choice.equals("update"))
                hobby = (Hobby) intent.getSerializableExtra("Hobby");
        } catch (Exception e) {
            choice = "insert";
        }

        init();
        createDatabase();
    }

    public void init() {
        // 初始化控件
        Spinner spTime = findViewById(R.id.spTime);
        TextView tvHobbyTitle = findViewById(R.id.tvHobbyTitle);

        etHobbyName = findViewById(R.id.etHobbyName);
        etHobbyCycle = findViewById(R.id.etHobbyCycle);
        recyclerView = findViewById(R.id.editIconList);
        selectedIcon = findViewById(R.id.selectedIcon);

        // 动态改变标题
        if (choice.equals("edit")) {
            tvHobbyTitle.setText("修改习惯");
            etHobbyName.setText(hobby.getHbName());
            etHobbyCycle.setText(hobby.getHbCycle());
        } else if (choice.equals("add")) {
            tvHobbyTitle.setText("添加习惯");
        }

        // button
        findViewById(R.id.btnEditHobby).setOnClickListener(editListener);
        findViewById(R.id.btnResetHobby).setOnClickListener(resetListener);
        findViewById(R.id.btnEditBack).setOnClickListener(backListener);

        // Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTime.setAdapter(spinnerAdapter);
        spTime.setOnItemSelectedListener(spinnerListener);

        // 创建图标列表
        createIconList();
    }

    // 创建数据库
    public void createDatabase() {
        dbHelper = new MyDatabaseHelper(EditHobbyActivity.this, "yantu.db", null, 1);
        dbHelper.getWritableDatabase();
    }

    // 添加/修改习惯数据库表
    public int editHobby(String hbName, String hbIcon, String hbTime, Integer hbCycle, String choice) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hbName", hbName);
        values.put("hbIcon", hbIcon);
        values.put("hbTime", hbTime);
        values.put("hbCycle", hbCycle);
        int result = 0;
        if (choice.equals("insert"))
            result = (int) db.insert("Hobby", null, values);
        else if (choice.equals("update"))
            result = db.update("Hobby", values, "hbId=?", new String[]{String.valueOf(hobby.getHbId())});
        android.util.Log.i("result", "editHobby: " + String.valueOf(result));
        values.clear();
        return result;
    }

    // 添加/修改日志数据库表
    public int editLog(Integer hbId, Integer lgTotal, Integer lgContinue) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hbId", hbId);
        values.put("lgTotal", lgTotal);
        values.put("lgContinue", lgContinue);
        long logid = db.insert("Log", null, values);
        android.util.Log.i("result", "editLog: " + String.valueOf(logid));
        values.clear();
        return (int) logid;
    }

    // 编辑按钮监听事件
    public View.OnClickListener editListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String name = etHobbyName.getText().toString();
            String icId = iconList.get(iconPosition);
            String hbTime = String.valueOf(spinnerPosition);
            String hbCycleStr = etHobbyCycle.getText().toString();
            Integer hbCycle;

            if (name.equals("") || icId.equals("") || hbCycleStr.equals("")) {
                Toast.makeText(EditHobbyActivity.this, "您还有未填写字段。", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                hbCycle = Integer.valueOf(hbCycleStr);
            } catch (Exception e) {
                Toast.makeText(EditHobbyActivity.this, "您输入的番茄钟周期不是数字！", Toast.LENGTH_SHORT).show();
                return;
            }

            android.util.Log.i("result", "name:" + name + "; icId:" + icId + "; hbTime:" +
                    hbTime + "; hbCycle" + hbCycleStr);

            if (choice.equals("insert")) {
                int hobbyId = editHobby(name, icId, hbTime, hbCycle, "insert");
                if (-1 == hobbyId)
                    Toast.makeText(EditHobbyActivity.this, "添加习惯失败！", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(EditHobbyActivity.this, "添加习惯成功！", Toast.LENGTH_SHORT).show();

                int logid = editLog(hobbyId, 1, 2);
                if (-1 == logid)
                    Toast.makeText(EditHobbyActivity.this, "添加打卡日志失败！", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(EditHobbyActivity.this, "添加打卡日志成功！", Toast.LENGTH_SHORT).show();
            } else if (choice.equals("update")) {
                int updateRes = editHobby(name, icId, hbTime, hbCycle, "update");
                if (-1 == updateRes)
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

    public void createIconList() {
        iconList = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            iconList.add("icon" + String.valueOf(i));
        }

        //生成一个LinearLayoutManager的对象
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EditHobbyActivity.this);
        //设置这个线性布局管理器的方向,为水平方向
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        EditRecycleAdapter editRecycleAdapter = new EditRecycleAdapter(this, iconList);
        editRecycleAdapter.setOnItemClickListener(this);
        //设置mHorRV的线性布局管理器
        recyclerView.setLayoutManager(linearLayoutManager);
        // 设置适配器
        recyclerView.setAdapter(editRecycleAdapter);
        //动画效果(仅限于添加删除)
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(EditHobbyActivity.this, TodayHobbyActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void onClick(View parent, int position) {
        Toast.makeText(this, "点击了第" + (position + 1) + "项", Toast.LENGTH_SHORT).show();
        iconPosition = position;
        String uriStr = "android.resource://com.example.yantu.androidproject/drawable/" + iconList.get(position);
        selectedIcon.setImageURI(Uri.parse(uriStr));
    }
}
