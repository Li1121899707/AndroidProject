package com.example.yantu.androidproject.Adapter;
/*李洋*/
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yantu.androidproject.R;

import java.util.List;
import java.util.Map;

public class EditRecycleAdapter extends RecyclerView.Adapter <EditRecycleAdapter.LinearViewHolder>{
    //声明引用
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<String> mDatas = null;
    private String beginPath = "android.resource://com.example.yantu.androidproject/drawable/";
    private OnItemClickListener mOnItemClickListener = null;

    //创建一个构造函数
    public EditRecycleAdapter(Context context, List<String> datas){
        this.mContext=context;
        this.mDatas = datas;
        //利用LayoutInflater把控件所在的布局文件加载到当前类当中
        mLayoutInflater = LayoutInflater.from(context);
    }
    //此方法要返回一个ViewHolder
    @Override
    public EditRecycleAdapter.LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(mLayoutInflater.inflate(R.layout.item_recycle_edit,parent,false));
    }
    //通过holder设置TextView的内容
    @Override
    public void onBindViewHolder(final EditRecycleAdapter.LinearViewHolder holder, final int position) {
        String iconPath = mDatas.get(position);
        Log.i("result", beginPath + iconPath);
        holder.imageView.setImageURI(Uri.parse(beginPath + iconPath));

        // 点击事件注册及分发
        if(null != mOnItemClickListener) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.imageView, position);
                }
            });
        }

    }
    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{
        //声明layout_linearrv_item布局控件的变量
        private ImageView imageView;

        public LinearViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView) itemView.findViewById(R.id.editIcon);
        }
    }

    // 设置点击事件
    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    // 点击事件接口
    public interface OnItemClickListener {
        void onClick(View parent, int position);
    }

}
