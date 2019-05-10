package com.example.yantu.androidproject.Adapter;
/*姚越*/
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yantu.androidproject.DBHelper.MyDatabaseHelper;
import com.example.yantu.androidproject.Entity.Hobby;
import com.example.yantu.androidproject.Entity.Log;
import com.example.yantu.androidproject.Fragment.TodayFragment;
import com.example.yantu.androidproject.R;

import java.util.List;
import java.util.Map;

public class HorLinearAdapter extends RecyclerView.Adapter<HorLinearAdapter.LinearViewHolder> {
    //声明引用
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Hobby> funclist;
    private HorLinearAdapter.OnItemClickListener mOnItemClickListener = null;

    //创建一个构造函数
    //public HorLinearAdapter(Context context){
    public HorLinearAdapter(Context context, List<Hobby> list) {
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
    public void onBindViewHolder(final HorLinearAdapter.LinearViewHolder holder, final int position) {
        String beginPath = "android.resource://com.example.yantu.androidproject/drawable/";
        final Hobby hobby = funclist.get(position);
        String funcName = hobby.getHbName();
        String funcIcon = hobby.getHbImg();
        holder.imageView.setImageURI(Uri.parse(beginPath + funcIcon));
        holder.textView.setText(funcName);
        if(null != mOnItemClickListener) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick1(holder.imageView, hobby, position);
                }
            });

        }

        //Glide.with(mContext).load("http://img.zcool.cn/community/01c8b4557aca590000002d5c60d85e.jpg@1280w_1l_2o_100sh.jpg").into(holder.imageView);

    }





    @Override
    public int getItemCount() {
        return funclist == null ? 0 : funclist.size();
    }

    // 点击事件接口
    public interface OnItemClickListener {
        void onClick1(ImageView imageView, Hobby hobby, int position);
    }
    // 设置点击事件
    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
        //Toast.makeText(HorLinearAdapter.this, "123", Toast.LENGTH_SHORT).show();
        //android.util.Log.i("result", "12345");
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
