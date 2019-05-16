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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yantu.androidproject.Adapter.EditRecycleAdapter;
import com.example.yantu.androidproject.DBHelper.MyDB;
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

    private MyDB myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_hobby_activity);
        // 标题栏沉浸
        Utils.setStatusBar(this, false, false);

        // 接收参数，添加功能只包含choice参数，修改还包含回显数据
        Intent intent = getIntent();
        try {
            choice = intent.getStringExtra("choice");
            Log.i("result", choice);
            if (choice.equals("update")){
                hobby = (Hobby) intent.getSerializableExtra("Hobby");
            }
        } catch (Exception e) {
            choice = "insert";
        }

        init();
    }

    public void init() {
        // 初始化控件
        Spinner spTime = findViewById(R.id.spTime);
        TextView tvHobbyTitle = findViewById(R.id.tvHobbyTitle);

        etHobbyName = findViewById(R.id.etHobbyName);
        etHobbyCycle = findViewById(R.id.etHobbyCycle);
        recyclerView = findViewById(R.id.editIconList);
        selectedIcon = findViewById(R.id.selectedIcon);
        Button btnEditHobby = findViewById(R.id.btnEditHobby);

        // button
        btnEditHobby.setOnClickListener(editListener);
        findViewById(R.id.btnResetHobby).setOnClickListener(resetListener);
        findViewById(R.id.btnEditBack).setOnClickListener(backListener);

        // Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTime.setAdapter(spinnerAdapter);
        spTime.setOnItemSelectedListener(spinnerListener);

        // 创建图标列表
        createIconList();

        // 动态改变标题
        if (choice.equals("update")) {
            tvHobbyTitle.setText("修改习惯");
            if(hobby.getHbName() != null)
                etHobbyName.setText(hobby.getHbName());
            if(hobby.getHbCycle() != null)
                etHobbyCycle.setText(String.valueOf(hobby.getHbCycle()));
            btnEditHobby.setText("修改");
            if(hobby.getHbTime() != null){
                int hbTime = Integer.valueOf(hobby.getHbTime());
                spTime.setSelection(hbTime, true);
                spinnerPosition = hbTime;
            }

        } else if (choice.equals("add")) {
            tvHobbyTitle.setText("添加习惯");
            btnEditHobby.setText("添加");
        }

        // 创建数据库
        myDB = new MyDB();
        myDB.createDatabase(EditHobbyActivity.this);
    }

    // 编辑按钮监听事件
    public View.OnClickListener editListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Integer id = 0;
            if(choice.equals("update"))
                id = hobby.getHbId();
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
                String newString = new String(name.getBytes("GB2312"), "ISO-8859-1");
                Log.i("result", newString.length() + ""); // 真实长度，一个汉字是两个字符
                if(newString.length() > 20){
                    Toast.makeText(EditHobbyActivity.this, "您输入的名称过长，请精简名称！", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                hbCycle = Integer.valueOf(hbCycleStr);
            } catch (Exception e) {
                Toast.makeText(EditHobbyActivity.this, "您输入的番茄钟周期不是数字！", Toast.LENGTH_SHORT).show();
                return;
            }

            if(hbCycle<0 || hbCycle>12){
                Toast.makeText(EditHobbyActivity.this, "您输入的番茄钟周期不合理，建议在1-6个周期内！", Toast.LENGTH_SHORT).show();
                return;
            }

            android.util.Log.i("result", "name:" + name + "; icId:" + icId + "; hbTime:" +
                    hbTime + "; hbCycle" + hbCycleStr);

            int resultCode = 1;
            if (choice.equals("insert")) {
                int hobbyId = myDB.editHobby(id, name, icId, hbTime, hbCycle, "insert");
                if (-1 == hobbyId){
                    resultCode = -1;
                    Toast.makeText(EditHobbyActivity.this, "添加习惯失败！", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(EditHobbyActivity.this, "添加习惯成功！", Toast.LENGTH_SHORT).show();

                int logid = myDB.editLog(hobbyId, 0, 0);
                if (-1 == logid){
                    resultCode = -1;
                    Toast.makeText(EditHobbyActivity.this, "添加打卡日志失败！", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(EditHobbyActivity.this, "添加打卡日志成功！", Toast.LENGTH_SHORT).show();
            } else if (choice.equals("update")) {
                int updateRes = myDB.editHobby(id, name, icId, hbTime, hbCycle, "update");
                if (-1 == updateRes){
                    resultCode = -1;
                    Toast.makeText(EditHobbyActivity.this, "修改习惯信息失败！", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(EditHobbyActivity.this, "修改习惯信息成功！", Toast.LENGTH_SHORT).show();
            }

            if(-1 != resultCode){
                returnMainActivity();
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
            //Toast.makeText(EditHobbyActivity.this, spinnerItems[position], Toast.LENGTH_SHORT).show();
            spinnerPosition = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public void createIconList() {
        iconList = new ArrayList<>();
        for (int i = 1; i <= 14; i++) {
            iconList.add("icon" + String.valueOf(i));
        }

        //生成一个LinearLayoutManager的对象
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EditHobbyActivity.this);
        //设置这个线性布局管理器的方向,为水平方向
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        EditRecycleAdapter editRecycleAdapter = new EditRecycleAdapter(this, iconList);
        // 设置监听事件。this指 OnItemClickListener对象，由于该Activity实现了该接口，因此将该类传入即可。(new EditHobbyActivity())
        editRecycleAdapter.setOnItemClickListener(this);
        //设置mHorRV的线性布局管理器
        recyclerView.setLayoutManager(linearLayoutManager);
        // 设置适配器
        recyclerView.setAdapter(editRecycleAdapter);
        //动画效果(仅限于添加删除)
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    // 返回按钮监听事件
    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            returnMainActivity();
        }
    };

    // EditRecycleAdapter（图片列表）点击事件，实现EditRecycleAdapter类中的抽象方法
    @Override
    public void editImageViewOnClick(ImageView parent, int position) {
        //Toast.makeText(this, "点击了第" + (position + 1) + "项", Toast.LENGTH_SHORT).show();
        iconPosition = position;
        String uriStr = "android.resource://com.example.yantu.androidproject/drawable/" + iconList.get(position);
        selectedIcon.setImageURI(Uri.parse(uriStr));
    }

    // 返回主界面
    public void returnMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("id",1);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //点击完返回键，执行的动作
            returnMainActivity();
        }
        return true;
    }
}
