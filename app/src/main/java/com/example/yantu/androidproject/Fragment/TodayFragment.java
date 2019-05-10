package com.example.yantu.androidproject.Fragment;
/*姚越*/
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yantu.androidproject.Adapter.HorLinearAdapter;
import com.example.yantu.androidproject.DBHelper.MyDatabaseHelper;
import com.example.yantu.androidproject.Entity.Hobby;
import com.example.yantu.androidproject.R;
import com.example.yantu.androidproject.Util.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TodayFragment extends Fragment implements HorLinearAdapter.OnItemClickListener{

    private RecyclerView mhorRV1;
    private RecyclerView mhorRV2;
    private RecyclerView mhorRV3;
    private RecyclerView mhorRV4;
    private ImageView funcImg;
    private String newImg;
    private Integer funcID;
    //private TextView mTextMessage;
    TextView lastDay;
    MyDatabaseHelper dbHelper;
    Boolean up = false;//默认false不刷新
    private Set<Hobby> hbSet;
    private List<Hobby> funclist1 ;
    private List<Hobby> funclist2 ;
    private List<Hobby> funclist3 ;
    private List<Hobby> funclist4 ;


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

        hbSet = new HashSet<>();
        funclist1 = new ArrayList<>();
        funclist2 = new ArrayList<>();
        funclist3 = new ArrayList<>();
        funclist4 = new ArrayList<>();
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

        dbHelper = new MyDatabaseHelper(getActivity(), "yantu.db", null, 1);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Hobby", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Hobby hobby = new Hobby();
                int hbID = cursor.getInt(cursor.getColumnIndex("hbId"));
                String hbName = cursor.getString(cursor.getColumnIndex("hbName"));
                String hbIcon = cursor.getString(cursor.getColumnIndex("hbIcon"));
                String hbTime = cursor.getString(cursor.getColumnIndex("hbTime"));
                hobby.setHbId(hbID);
                hobby.setHbCycle(0);
                hobby.setHbImg(hbIcon);
                hobby.setHbName(hbName);
                hobby.setHbTime(hbTime);
                if(hbTime.equals("1")){
                    funclist1.add(hobby);
                }else if(hbTime.equals("2")){
                    funclist2.add(hobby);
                }
                else if(hbTime.equals("3")){
                    funclist3.add(hobby);
                }
                else if(hbTime.equals("0")){
                    funclist4.add(hobby);
                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        querySignedItem();
        updateList(funclist1);
        updateList(funclist2);
        updateList(funclist3);
        updateList(funclist4);


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
        HorLinearAdapter adapter1 = new HorLinearAdapter(getActivity(), funclist1);
        adapter1.setOnItemClickListener(this);
        mhorRV1.setAdapter(adapter1);
        HorLinearAdapter adapter2 = new HorLinearAdapter(getActivity(), funclist2);
        adapter2.setOnItemClickListener(this);
        mhorRV2.setAdapter(adapter2);
        HorLinearAdapter adapter3 = new HorLinearAdapter(getActivity(), funclist3);
        adapter3.setOnItemClickListener(this);
        mhorRV3.setAdapter(adapter3);
        HorLinearAdapter adapter4 = new HorLinearAdapter(getActivity(), funclist4);
        adapter4.setOnItemClickListener(this);
        mhorRV4.setAdapter(adapter4);
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

    @Override
    public void onClick1(final ImageView imageView, final Hobby hobby, int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_pop_add, null);
        // 设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view);
        //这个位置十分重要，只有位于这个位置逻辑才是正确的
        final AlertDialog dialog = builder.show();
        /////////////////////////////////////////////////////////////////////////////////////////////打卡操作
        newImg =hobby.getHbImg();
        funcID = hobby.getHbId();

        //java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        final String nowDate = sdf.format(new Date());
        Log.i("result",nowDate);

        Date date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        final String lastDate = sdf.format(date);
        Log.i("result",lastDate);

        // final String lastDate = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
        //Toast.makeText(getActivity(),nowDate+lastDate,Toast.LENGTH_SHORT).show();
        Button btn1 = (Button)view.findViewById(R.id.btn_1);
        final Button btn2 = (Button)view.findViewById(R.id.btn_2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper = new MyDatabaseHelper(getActivity(), "yantu.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                Cursor cursor = db.query("Clockin", null, "hbId=? and ciDate=?", new String[]{String.valueOf(funcID),nowDate}, null, null, null);
                boolean alreadyClockIn = false;
                if(cursor.moveToFirst()){
                    alreadyClockIn = true;
                    Toast.makeText(getActivity(),"请勿重复打卡",Toast.LENGTH_SHORT).show();
                    Log.i("result", cursor.getCount() + "");
                }
                cursor.close();
                if(alreadyClockIn){
                    dialog.dismiss();
                    return;
                }

                ContentValues values = new ContentValues();
                values.put("hbId",funcID);
                values.put("ciDate",nowDate);
                db.insert("Clockin",null,values);
                values.clear();
                String beginPath = "android.resource://com.example.yantu.androidproject/drawable/";
                hobby.setHbImg(beginPath + newImg + "_1");
                Log.i("result", beginPath + newImg + "_1");
                Toast.makeText(getActivity(),"您已完成打卡",Toast.LENGTH_SHORT).show();
                imageView.setImageURI(Uri.parse(hobby.getHbImg()));


                ContentValues values1 = new ContentValues();
                Cursor cursor2 = db.query("Log", null, "hbId=?", new String[]{String.valueOf(funcID)}, null, null, null);
                int total = 0;
                int lgContinue = 0;
                if(cursor2.moveToFirst()){ // 必须加！！！
                    Log.i("result", cursor2.getColumnIndex("lgTotal") + "  cursor2");
                    total=cursor2.getInt(cursor2.getColumnIndex("lgTotal"));
                    lgContinue=cursor2.getInt(cursor2.getColumnIndex("lgContinue"));
                    Log.i("result", total + "  total");
                    Log.i("result", lgContinue + "  logcintinue");
                }
                cursor2.close();

                Cursor cursor3 = db.query("Clockin", null, "ciDate=?", new String[]{lastDate}, null, null, null);
                if(cursor3.moveToFirst()){
                    values1.put("lgTotal", total+1);
                    values1.put("lgContinue", lgContinue+1);
                }
                else{
                    values1.put("lgTotal", total+1);
                    values1.put("lgContinue", 1);
                }
                cursor3.close();

                db.update("Log", values1,"hbId=?",new String[]{String.valueOf(funcID)});
                values1.clear();

                dialog.dismiss();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    public void querySignedItem(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        final String nowDate = sdf.format(new Date());

        Cursor cursor = db.rawQuery("select Clockin.hbId as id, hbName, hbTime, hbCycle, hbIcon, ciDate from Clockin, Hobby where Hobby.hbId = Clockin.hbid and Clockin.ciDate = ?", new String[]{nowDate});
        if(cursor.moveToFirst()){
            do {
                Hobby hobby = new Hobby();
                hobby.setHbId(cursor.getInt(cursor.getColumnIndex("hbId")));
                hobby.setHbName(cursor.getString(cursor.getColumnIndex("hbName")));
                hobby.setHbImg(cursor.getString(cursor.getColumnIndex("hbIcon")));
                hobby.setHbTime(cursor.getString(cursor.getColumnIndex("hbTime")));
                hobby.setHbCycle(cursor.getInt(cursor.getColumnIndex("hbCycle")));
                hbSet.add(hobby);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void updateList(List<Hobby> list){
        for(int i = 0; i<list.size(); i++ ){
            Hobby hobby = list.get(i);
            if(hbSet.contains(hobby)){
                String newImg = "android.resource://com.example.yantu.androidproject/drawable/" + hobby.getHbImg() + "_1";
                hbSet.remove(hobby);
                hobby.setHbImg(newImg);
                hbSet.add(hobby);
            }
        }
    }


}
