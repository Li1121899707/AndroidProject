package com.example.yantu.androidproject.Fragment;
/*侯禹驰*/
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yantu.androidproject.Adapter.DailyRecycleAdapter;
import com.example.yantu.androidproject.DBHelper.MyDB;
import com.example.yantu.androidproject.DBHelper.MyDatabaseHelper;
import com.example.yantu.androidproject.EditHobbyActivity;
import com.example.yantu.androidproject.Entity.Hobby;
import com.example.yantu.androidproject.Entity.Log;
import com.example.yantu.androidproject.HobbyDetailAvtivity;
import com.example.yantu.androidproject.R;
import com.example.yantu.androidproject.Util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DailyFragment extends Fragment implements DailyRecycleAdapter.DailyOnItemClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily, container, false);
    }

    private RecyclerView rvDaily;
    private List<Hobby> hobbyList;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Utils.setStatusBar(getActivity(), false, false);
        init();
    }

    public void init(){
        // 初始化控件
        rvDaily = getActivity().findViewById(R.id.rvDaily);
        hobbyList = new ArrayList<>();
        ImageView addHobby = getActivity().findViewById(R.id.ivAddHobby);

        addHobby.setOnClickListener(addHobbyListener);

        android.util.Log.i("result", "create Daily");
        MyDB myDB = new MyDB();
        myDB.createDatabase(getActivity());
        hobbyList = myDB.queryAllFromHobby();
        addToList();
    }

    public void addToList(){
        android.util.Log.i("result", "addtolist");
        // 设置线性布局管理器
        rvDaily.addItemDecoration(new MyDecoration());
        //添加分割线
        rvDaily.setLayoutManager(new LinearLayoutManager(getContext()));
        // 设置Adapter
        DailyRecycleAdapter adapter = new DailyRecycleAdapter(getContext(), hobbyList);
        // 添加监听事件
        adapter.setOnItemClickListener(this);
        rvDaily.setAdapter(adapter);
    }

    class MyDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(getResources().getDimensionPixelOffset(R.dimen.dimenForDaily),getResources().getDimensionPixelOffset(R.dimen.dimenForDaily),getResources().getDimensionPixelOffset(R.dimen.dimenForDaily),getResources().getDimensionPixelOffset(R.dimen.dimenForDaily));
        }
    }

    // ② 实现接口
    @Override
    public void recycleViewOnClick(View parent, int position) {
        //Toast.makeText(getContext(), "点击了第" + (position + 1) + "项", Toast.LENGTH_SHORT).show();
        // 开启番茄钟
        handleClock(position);
    }

    @Override
    public void recycleViewOnClick(View parent, int position, boolean translate) {
        Intent intent = new Intent(getContext(), HobbyDetailAvtivity.class);
        Hobby hobby = hobbyList.get(position);
        intent.putExtra("Hobby", hobby);
        startActivity(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    public View.OnClickListener addHobbyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), EditHobbyActivity.class);
            intent.putExtra("choice","insert");
            startActivity(intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Objects.requireNonNull(getActivity()).finish();
            }
        }
    };

    public void handleClock(Integer position){
        int cycle = hobbyList.get(position).getHbCycle();
        String name = hobbyList.get(position).getHbName();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("cycle", cycle);
        bundle.putString("name", name);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
