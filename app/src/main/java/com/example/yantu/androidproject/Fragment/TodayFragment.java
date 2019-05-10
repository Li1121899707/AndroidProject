package com.example.yantu.androidproject.Fragment;
/*姚越*/
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yantu.androidproject.Adapter.HorLinearAdapter;
import com.example.yantu.androidproject.DBHelper.MyDatabaseHelper;
import com.example.yantu.androidproject.R;
import com.example.yantu.androidproject.Util.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodayFragment extends Fragment {

    private RecyclerView mhorRV1;
    private RecyclerView mhorRV2;
    private RecyclerView mhorRV3;
    private RecyclerView mhorRV4;
    //private TextView mTextMessage;
    TextView lastDay;
    MyDatabaseHelper dbHelper;
    Boolean up = false;//默认false不刷新


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Utils.setStatusBar(getActivity(), false, false);
        init();
    }

    public long dateDiff(String startTime, String endTime, String format) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数

        long diff;
        long day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天

            return day;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                //System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                //System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public void init(){
        ///////////////////////////////////////////////////////////////////////////////////////////////////获取倒计时
        Calendar calendar = Calendar.getInstance();                   //获取系统的日期
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH) + 1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int goalYear = 2019;
        String nowDate = "";

        nowDate = nowDate + String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
        while (goalYear < year) {
            goalYear++;
        }
        String goalDate = String.valueOf(goalYear) + "-12-21";
        long daynumber;
        int dateCompare = compare_date(nowDate, goalDate);
        if (dateCompare == 1) {
            goalYear++;
        }
        goalDate = String.valueOf(goalYear) + "-12-21";
        daynumber = dateDiff(nowDate, goalDate, "yyyy-MM-dd");
        lastDay = getActivity().findViewById(R.id.lastDay);
        lastDay.setText("考研倒计时 " + daynumber + " 天");
        //Toast.makeText(TodayHobbyActivity.this, "" + daynumber, Toast.LENGTH_LONG).show();

        //////////////////////////////////////////////////////////////////////////////////////////////////读取功能
        final List<Map<String, Object>> funclist1 = new ArrayList<Map<String, Object>>();
        final List<Map<String, Object>> funclist2 = new ArrayList<Map<String, Object>>();
        final List<Map<String, Object>> funclist3 = new ArrayList<Map<String, Object>>();
        final List<Map<String, Object>> funclist4 = new ArrayList<Map<String, Object>>();
        dbHelper = new MyDatabaseHelper(getActivity(), "yantu.db", null, 1);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Hobby", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String hbName = cursor.getString(cursor.getColumnIndex("hbName"));
                String hbIcon = cursor.getString(cursor.getColumnIndex("hbIcon"));
                String hbTime = cursor.getString(cursor.getColumnIndex("hbTime"));
                Map<String, Object> map = new HashMap<String, Object>();
                switch (hbTime) {
                    case "1":
                        map.put("img", hbIcon);
                        map.put("name", hbName);
                        funclist1.add(map);
                        break;
                    case "2":
                        map.put("img", hbIcon);
                        map.put("name", hbName);
                        funclist2.add(map);
                        break;
                    case "3":
                        map.put("img", hbIcon);
                        map.put("name", hbName);
                        funclist3.add(map);
                        break;
                    case "0":
                        map.put("img", hbIcon);
                        map.put("name", hbName);
                        funclist4.add(map);
                        break;
                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        mhorRV1 = getActivity().findViewById(R.id.morningList);
        mhorRV2 = getActivity().findViewById(R.id.noonList);
        mhorRV3 = getActivity().findViewById(R.id.eveningList);
        mhorRV4 = getActivity().findViewById(R.id.otherList);
        //设置一个线性布局管理器,因为要设置方向，就不采用匿名内部类的方式了
        //生成一个LinearLayoutManager的对象
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getActivity());
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(getActivity());
        //设置这个线性布局管理器的方向,为水平方向
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);

        //设置mHorRV的线性布局管理器
        mhorRV1.setLayoutManager(linearLayoutManager1);
        mhorRV2.setLayoutManager(linearLayoutManager2);
        mhorRV3.setLayoutManager(linearLayoutManager3);
        mhorRV4.setLayoutManager(linearLayoutManager4);
        //设置适配器：Adapter
        //mhorRV1.setAdapter(new HorLinearAdapter(getActivity()));
        mhorRV1.setAdapter(new HorLinearAdapter(getActivity(), funclist1));
        mhorRV2.setAdapter(new HorLinearAdapter(getActivity(), funclist2));
        mhorRV3.setAdapter(new HorLinearAdapter(getActivity(), funclist3));
        mhorRV4.setAdapter(new HorLinearAdapter(getActivity(), funclist4));
        android.util.Log.i("result1", "finish");
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
}