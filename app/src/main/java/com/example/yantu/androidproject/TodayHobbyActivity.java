package com.example.yantu.androidproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodayHobbyActivity extends AppCompatActivity {

    //private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_hobby_activity);

        //隐藏系统标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        ///////////////////////////////////////////add func
        final List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("image",R.drawable.ic_launcher_foreground);
            map.put("text","功能一");
            list.add(map);
        }
        {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("image",R.drawable.ic_launcher_foreground);
            map.put("text","功能二");
            list.add(map);
        }
        {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("image",R.drawable.ic_launcher_foreground);
            map.put("text","功能三");
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.funclist,
                new String[]{"image","text"},
                new int[]{R.id.funcImg,R.id.funcName});
        ListView lv = (ListView)findViewById(R.id.morningList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String,Object> map = list.get(i);
                Toast.makeText(TodayHobbyActivity.this,"使用 "+map.get("text").toString(),Toast.LENGTH_LONG).show();
            }
        });
        //////////////////////////////////////////
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
