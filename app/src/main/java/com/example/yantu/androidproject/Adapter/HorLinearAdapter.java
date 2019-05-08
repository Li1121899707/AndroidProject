package com.example.yantu.androidproject.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yantu.androidproject.DBHelper.MyDatabaseHelper;
import com.example.yantu.androidproject.R;

import java.util.List;
import java.util.Map;

public class HorLinearAdapter extends RecyclerView.Adapter<HorLinearAdapter.LinearViewHolder> {
    //声明引用
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Map<String, Object>> funclist;


    //创建一个构造函数
    //public HorLinearAdapter(Context context){
    public HorLinearAdapter(Context context, List<Map<String, Object>> list) {
        this.mContext = context;
        this.funclist = list;
        //利用LayoutInflater把控件所在的布局文件加载到当前类当中
        mLayoutInflater = LayoutInflater.from(context);
    }

    //此方法要返回一个ViewHolder
    @Override
    public HorLinearAdapter.LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(mLayoutInflater.inflate(R.layout.funclist, parent, false));
    }

    //通过holder设置TextView的内容
    @Override
    public void onBindViewHolder(HorLinearAdapter.LinearViewHolder holder, final int position) {
        String beginPath = "android.resource://com.example.yantu.androidproject/drawable/";
        Map<String,Object> map = funclist.get(position);
        String funcName = map.get("name").toString();
        String funcIcon = map.get("img").toString();
        holder.imageView.setImageURI(Uri.parse(beginPath + funcIcon));
        holder.textView.setText(funcName);
        //Glide.with(mContext).load("http://img.zcool.cn/community/01c8b4557aca590000002d5c60d85e.jpg@1280w_1l_2o_100sh.jpg").into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return funclist == null ? 0 : funclist.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        //声明layout_linearrv_item布局控件的变量
        private TextView textView;
        private ImageView imageView;

        public LinearViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.funcName);
            imageView = (ImageView) itemView.findViewById(R.id.funcImg);
        }
    }
}
