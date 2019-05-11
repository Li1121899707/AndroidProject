package com.example.yantu.androidproject.Adapter;
/*李洋*/
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yantu.androidproject.R;

import java.util.List;

public class EditRecycleAdapter extends RecyclerView.Adapter <EditRecycleAdapter.LinearViewHolder>{
    private Context mContext;
    // 布局填充
    private LayoutInflater mLayoutInflater;
    // 由 EditRecycleActvity 传入，是 RecyclerView 列表的所有数据。
    private List<String> mDatas;
    // 自定义类接口 OnItemClickListener， 由Activity实现并传入。
    private OnItemClickListener mOnItemClickListener = null;

    //创建一个构造函数
    public EditRecycleAdapter(Context context, List<String> datas){
        this.mContext=context;
        this.mDatas = datas;
        //利用LayoutInflater把控件所在的布局文件加载到当前类当中
        mLayoutInflater = LayoutInflater.from(context);
    }

    //为Adapter设定布局，并且返回一个Holder对象
    @Override
    public EditRecycleAdapter.LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(mLayoutInflater.inflate(R.layout.item_recycle_edit,parent,false));
    }

    // 点击事件接口
    public interface OnItemClickListener {
        void editImageViewOnClick(ImageView parent, int position);
    }

    // 设置点击事件
    // mOnItemClickListener由实现OnItemClickListener接口的类传入。
    // EditHobbyActivity中： editRecycleAdapter.setOnItemClickListener(this);
    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    // 使用Holder对recycleView中的ImageView进行赋值。position为当前位置。
    // onBindViewHolder在为 activity 中的 RecyclerList 设定Adapter时会自动调用。
    @Override
    public void onBindViewHolder(final EditRecycleAdapter.LinearViewHolder holder, final int position) {
        String iconPath = mDatas.get(position);
        String beginPath = "android.resource://com.example.yantu.androidproject/drawable/";

        Log.i("result", beginPath + iconPath  + "  onBindViewHolder");
        holder.imageView.setImageURI(Uri.parse(beginPath + iconPath));

        // 点击事件注册
        if(null != mOnItemClickListener) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.editImageViewOnClick(holder.imageView, position);
                }
            });
        }

    }

    // 返回刷新列表的数据，这里默认是list的大小
    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    //自定义类 LinearViewHolder 来减少 findViewById() 的使用
    class LinearViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        LinearViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.editIcon);
        }
    }

}
