package com.example.yantu.androidproject.Adapter;
/*侯禹驰*/
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yantu.androidproject.DBHelper.MyDatabaseHelper;
import com.example.yantu.androidproject.DailyHobbyActivity;
import com.example.yantu.androidproject.HobbyDetailAvtivity;
import com.example.yantu.androidproject.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyRecycleAdapter extends RecyclerView.Adapter<DailyRecycleAdapter.LinearViewHolder> {
    // 声明引用
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private List<Map<String, String>> list;

    //创建构造函数引入Contex类型变量
    public DailyRecycleAdapter(Context context){
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        createDatabase();
    }

    // 创建数据库并查询所需数据
    public void createDatabase(){
        dbHelper = new MyDatabaseHelper((HobbyDetailAvtivity) mContext, "yantu.db", null, 1);
        dbHelper.getWritableDatabase();
        db = dbHelper.getWritableDatabase();
        list = new ArrayList<>();
//        Cursor c = db.rawQuery("select * from yantu",null);
//        if (c != null)
//        {
//            Map<String, String> map = new HashMap<>();
//            String s = "";
//            while (c.moveToNext()){
//                map.put("id", s.valueOf(c.getInt(c.getColumnIndex("hbId"))));
//                map.put("name", c.getString(c.getColumnIndex("hbName")));
//                map.put("time", c.getString(c.getColumnIndex("hbTime")));
//                map.put("cycle", s.valueOf(c.getInt(c.getColumnIndex("hbCycle"))));
//                list.add(map);
//            }
//        }
//        c.close();
//        db.close();
    }

    @Override
    public DailyRecycleAdapter.LinearViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new LinearViewHolder(mLayoutInflater.inflate(R.layout.item_recycle_daily,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(DailyRecycleAdapter.LinearViewHolder viewHolder, int i) {
//        for (Map<String, String> m : list)
//        {
//            if (m.containsKey("id"))
//            {
//
//            }
//        }
//        viewHolder.itemView.setId(values.getAsInteger("id"));
//        viewHolder.textView.setText(values.getAsString("name") + " " + values.getAsString("time"));
//        //此处直接将数字转为字符串，可能出错
//        viewHolder.button.setId(values.getAsInteger("id"));
//        viewHolder.button.setText(values.getAsInteger("cycle"));
//        if (viewHolder.button.getText().equals("0"))
//        {
//            viewHolder.button.setEnabled(false);
//        }
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tag", "点击位置"+ v.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private Button button;
        public LinearViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvRecycleItem);
            button = itemView.findViewById(R.id.button);
        }
    }
}
