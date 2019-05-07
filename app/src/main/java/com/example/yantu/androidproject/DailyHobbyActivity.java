package com.example.yantu.androidproject;
/*侯禹驰*/
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.yantu.androidproject.Adapter.DailyRecycleAdapter;
import com.example.yantu.androidproject.DBHelper.MyDatabaseHelper;

public class DailyHobbyActivity extends AppCompatActivity {
    private RecyclerView rvDaily;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_hobby_activity);
        rvDaily = findViewById(R.id.rvDaily);
        // 设置线性布局管理器
        rvDaily.addItemDecoration(new MyDecoration());
        //添加分割线
        rvDaily.setLayoutManager(new LinearLayoutManager(DailyHobbyActivity.this));
        // 设置Adapter
        rvDaily.setAdapter(new DailyRecycleAdapter(DailyHobbyActivity.this));

    }

    class MyDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(getResources().getDimensionPixelOffset(R.dimen.dimenForDaily),getResources().getDimensionPixelOffset(R.dimen.dimenForDaily),getResources().getDimensionPixelOffset(R.dimen.dimenForDaily),getResources().getDimensionPixelOffset(R.dimen.dimenForDaily));
        }
    }
}
