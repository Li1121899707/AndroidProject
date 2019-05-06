package com.example.yantu.androidproject;
/*侯禹驰*/
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.example.yantu.androidproject.Adapter.DailyRecycleAdapter;

public class DailyHobbyActivity extends AppCompatActivity {
    private RecyclerView rvDaily;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_hobby_activity);
        rvDaily = findViewById(R.id.rvDaily);
        // 设置线性布局管理器
        rvDaily.setLayoutManager(new LinearLayoutManager(DailyHobbyActivity.this));
        // 设置Adapter
        rvDaily.setAdapter(new DailyRecycleAdapter(DailyHobbyActivity.this));
    }
}
