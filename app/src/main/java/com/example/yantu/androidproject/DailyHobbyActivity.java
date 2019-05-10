package com.example.yantu.androidproject;
/*侯禹驰*/
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yantu.androidproject.Adapter.DailyRecycleAdapter;
import com.example.yantu.androidproject.DBHelper.MyDatabaseHelper;
import com.example.yantu.androidproject.Entity.Hobby;
import com.example.yantu.androidproject.Util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyHobbyActivity extends AppCompatActivity implements DailyRecycleAdapter.DailyOnItemClickListener{

    private RecyclerView rvDaily;
    private MyDatabaseHelper dbHelper;
    private List<Hobby> hobbyList;
    Boolean up = false;//默认false不刷新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_hobby_activity);
        Utils.setStatusBar(this, false, false);
        init();

    }

    public void init(){

        // 初始化控件
        rvDaily = findViewById(R.id.rvDaily);
        hobbyList = new ArrayList<>();
        ImageView addHobby = findViewById(R.id.addHobby);

        // 底部导航栏注册监听事件（需合并，拆分为fragment）
        BottomNavigationView navigation = findViewById(R.id.navigationDailly);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);

        addHobby.setOnClickListener(addHobbyListener);

        createDatabase();
        queryAllFromHobby();
        addToList();
    }

    // 创建数据库（lysuzy）
    public void createDatabase() {
        dbHelper = new MyDatabaseHelper(DailyHobbyActivity.this, "yantu.db", null, 1);
        dbHelper.getWritableDatabase();
    }

    // 查询所有hobby，存入hobbyList
    public void queryAllFromHobby(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Hobby", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Hobby hobby = new Hobby();
                hobby.setHbId(cursor.getInt(cursor.getColumnIndex("hbId")));
                hobby.setHbName(cursor.getString(cursor.getColumnIndex("hbName")));
                hobby.setHbImg(cursor.getString(cursor.getColumnIndex("hbIcon")));
                hobby.setHbTime(cursor.getString(cursor.getColumnIndex("hbTime")));
                hobby.setHbCycle(cursor.getInt(cursor.getColumnIndex("hbCycle")));
                hobbyList.add(hobby);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void addToList(){
        // 设置线性布局管理器
        rvDaily.addItemDecoration(new MyDecoration());
        //添加分割线
        rvDaily.setLayoutManager(new LinearLayoutManager(DailyHobbyActivity.this));
        // 设置Adapter
        DailyRecycleAdapter adapter = new DailyRecycleAdapter(DailyHobbyActivity.this, hobbyList);
        // 添加监听事件
        adapter.setOnItemClickListener(this);
        rvDaily.setAdapter(adapter);


    }

    class MyDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(getResources().getDimensionPixelOffset(R.dimen.dimenForDaily),getResources().getDimensionPixelOffset(R.dimen.dimenForDaily),getResources().getDimensionPixelOffset(R.dimen.dimenForDaily),getResources().getDimensionPixelOffset(R.dimen.dimenForDaily));
        }
    }

    // 底部状态栏监听时间（lysuzy）
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(DailyHobbyActivity.this, TodayHobbyActivity.class);
                    startActivity(intent);
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    Intent intent1 = new Intent(DailyHobbyActivity.this, SettingActivity.class);
                    startActivity(intent1);
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };


    // ② 实现接口
    @Override
    public void recycleViewOnClick(View parent, int position) {
        Toast.makeText(this, "点击了第" + (position + 1) + "项", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void recycleViewOnClick(View parent, int position, boolean translate) {
        Intent intent = new Intent(DailyHobbyActivity.this, HobbyDetailAvtivity.class);
        Hobby hobby = hobbyList.get(position);
        intent.putExtra("Hobby", hobby);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        up = true;//不可见的时候将刷新开启
    }


    @Override
    public void onResume() {
        super.onResume();
        if (up) {
            //（方法）;//向服务器发送请求
            up = false;//刷新一次即可，不需要一直刷新
        }
    }

    public View.OnClickListener addHobbyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DailyHobbyActivity.this, EditHobbyActivity.class);
            intent.putExtra("choice","insert");
            startActivity(intent);
        }
    };
}
