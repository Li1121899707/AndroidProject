package com.example.yantu.androidproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yantu.androidproject.Entity.Log;
import com.example.yantu.androidproject.Fragment.DailyFragment;
import com.example.yantu.androidproject.Fragment.SettingFragment;
import com.example.yantu.androidproject.Fragment.TodayFragment;
import com.example.yantu.androidproject.Util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final int ACTIVITY_ID = 951753;

    private BottomNavigationView mBottomNavigationView;
    private TextView mainTitle;
    private ImageView ivAddHobby;
    private int lastIndex;
    List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.setStatusBar(this, false, false);
        initBottomNavigation();
        init();

        try {
            int id = getIntent().getIntExtra("id", 0);
            if (id == 1) {
                setFragmentPosition(1);
            }
        }catch (Exception e){
            Toast.makeText(this, "不需要跳转", Toast.LENGTH_SHORT).show();
        }
    }

    public void init() {
        mainTitle = findViewById(R.id.mainTitle);
        ivAddHobby = findViewById(R.id.ivAddHobby);
        mFragments = new ArrayList<>();
        mFragments.add(new TodayFragment());
        mFragments.add(new DailyFragment());
        mFragments.add(new SettingFragment());
        // 初始化展示MessageFragment
        setFragmentPosition(0);

        ivAddHobby.setOnClickListener(imgListener);
    }

    public void initBottomNavigation() {
        mBottomNavigationView = findViewById(R.id.bottomNavigationMain);
        // 解决当item大于三个时，非平均布局问题
        setListener();
    }


    private void setFragmentPosition(int position) {
        // 先取消，否则无限循环。不取消执行setSelectedItemId相当于执行了一遍监听事件
        changeTopBar(position);
        dropListener();
        if(position == 0)
            mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
        else if(position == 1)
            mBottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
        else if(position == 2)
            mBottomNavigationView.setSelectedItemId(R.id.navigation_notifications);
        setListener();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = mFragments.get(position);
        Fragment lastFragment = mFragments.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.frameLayout, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }

    public View.OnClickListener imgListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, EditHobbyActivity.class);
            intent.putExtra("choice", "insert");
            startActivity(intent);
        }
    };

    public void setListener(){
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        setFragmentPosition(0);
                        break;
                    case R.id.navigation_dashboard:
                        setFragmentPosition(1);
                        break;
                    case R.id.navigation_notifications:
                        setFragmentPosition(2);
                        break;
                    default:
                        break;
                }
                // 这里注意返回true,否则点击失效
                return true;
            }
        });
    }

    public void dropListener(){
        mBottomNavigationView.setOnNavigationItemSelectedListener(null);
    }

    public void changeTopBar(int position){
        if(position == 0){
            mainTitle.setText("Today");
            ivAddHobby.setVisibility(View.INVISIBLE);
        }else if(position == 1){
            mainTitle.setText("Daily");
            ivAddHobby.setVisibility(View.VISIBLE);
        }else if(position == 2){
            mainTitle.setText("Settings");
            ivAddHobby.setVisibility(View.INVISIBLE);
        }
    }

    public void getTime() throws ParseException {
        SimpleDateFormat sj = new SimpleDateFormat("yyyy-MM-dd");
        String today = "2015-11-30";
        Date d = sj.parse(today);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.DATE, 1);
        android.util.Log.i("result", sj.format(calendar.getTime()));
        //此时日期变为2015-12-01 ，所以下面的-2，
        //理论上讲应该是2015-11-29
        calendar.add(calendar.DATE, -2);
        android.util.Log.i("result",sj.format(calendar.getTime()));

    }




}
