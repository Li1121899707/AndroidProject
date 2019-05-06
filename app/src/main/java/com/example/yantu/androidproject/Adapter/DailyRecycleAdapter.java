package com.example.yantu.androidproject.Adapter;
/*侯禹驰*/
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yantu.androidproject.DailyHobbyActivity;
import com.example.yantu.androidproject.R;

public class DailyRecycleAdapter extends RecyclerView.Adapter<DailyRecycleAdapter.LinearViewHolder> {
    // 声明引用
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    //创建构造函数引入Contex类型变量
    public DailyRecycleAdapter(Context context){
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public DailyRecycleAdapter.LinearViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        return new LinearViewHolder(mLayoutInflater.inflate(R.layout.item_recycle_daily,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(DailyRecycleAdapter.LinearViewHolder viewHolder, int i) {
        viewHolder.textView.setText("Hello Yuan!");
    }

    @Override
    public int getItemCount() {
        return 25;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        public LinearViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvRecycleItem);
        }
    }


}
