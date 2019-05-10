package com.example.yantu.androidproject.Adapter;
/*侯禹驰*/
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yantu.androidproject.DBHelper.MyDatabaseHelper;
import com.example.yantu.androidproject.Entity.Hobby;
import com.example.yantu.androidproject.R;

import java.util.List;

public class DailyRecycleAdapter extends RecyclerView.Adapter<DailyRecycleAdapter.LinearViewHolder> {
    // 声明引用
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    //private List<Map<String, String>> list;
    private List<Hobby> hbList;
    private DailyOnItemClickListener mOnItemClickListener = null;


    //创建构造函数引入Contex类型变量
    public DailyRecycleAdapter(Context context, List<Hobby> hbList){
        this.mContext = context;
        this.hbList = hbList;
        mLayoutInflater = LayoutInflater.from(context);

    }

    // 创建数据库并查询所需数据


    @Override
    public DailyRecycleAdapter.LinearViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new LinearViewHolder(mLayoutInflater.inflate(R.layout.item_recycle_daily,viewGroup,false));
    }

    //通过holder设置TextView与button的内容
    @Override
    public void onBindViewHolder(final DailyRecycleAdapter.LinearViewHolder holder, final int position) {
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

        final Hobby hobby = hbList.get(position);
        holder.textView.setText(hobby.getHbName());
        holder.button.setText(String.valueOf(hobby.getHbCycle()));

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tag", "点击位置"+ position);
            }
        });

        // ④ 注册点击事件
        if(null != mOnItemClickListener) {
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.recycleViewOnClick(holder.textView, position);
                }
            });

            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.recycleViewOnClick(holder.textView, position, true);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        //java.lang.NullPointerException: Attempt to invoke interface method 'int java.util.List.size()' on a null object reference
        //return list.size();
        return hbList == null ? 0 : hbList.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private Button button;
        public LinearViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvDailyRecycleItem);
            button = itemView.findViewById(R.id.btnDailyCycle);
        }
    }

    /**
     * ① 在Adapter中创建点击事件接口
     * ② 在Activity中实现点击事件接口
     * ③ 在Adapter中添加设置点击事件函数
     * ④ 在Adapter中调用recycleViewOnClick方法（注册点击事件）
     */

    // ① 点击事件接口
    public interface DailyOnItemClickListener {
        void recycleViewOnClick(View parent, int position);
        void recycleViewOnClick(View parent, int position, boolean translate);
    }

    // ③ 添加设置点击事件函数，参数为DailyOnItemClickListener的实现，DailyHobbyActivity实现了此函数，因此l的值为DailyHobbyActivity.this
    // 该函数在DailyHobbyActivity设置recycleview时调用
    public void setOnItemClickListener(DailyOnItemClickListener l) {
        this.mOnItemClickListener = l;
    }


}
