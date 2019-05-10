package com.example.yantu.androidproject.Fragment;
/*张立才*/
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.yantu.androidproject.R;
import com.example.yantu.androidproject.Util.AlarmUtil;
import com.example.yantu.androidproject.Util.Utils;


public class SettingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    private Switch notiSwitch;
    private Switch vibrateSwitch;
    private SharedPreferences sp;
    

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Utils.setStatusBar(getActivity(), false, false);
        init();
    }

    public void init() {
        notiSwitch = getActivity().findViewById(R.id.notiSwitch);
        vibrateSwitch = getActivity().findViewById(R.id.vibrateSwitch);

        sp = getActivity().getSharedPreferences("settings", getActivity().MODE_PRIVATE);

        String notiChoice = sp.getString("notification_setting","0");
        String vibChoice = sp.getString("vibrate_setting","0");

        if(notiChoice != null && notiChoice.equals("1"))
            notiSwitch.setChecked(true);
        else
            vibrateSwitch.setClickable(false);
        if(vibChoice != null && vibChoice.equals("1"))
            vibrateSwitch.setChecked(true);

        notiSwitch.setOnCheckedChangeListener(switchListener);
        vibrateSwitch.setOnCheckedChangeListener(vibListener);
    }


    public CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                vibrateSwitch.setClickable(true);
                OpenNotification();
                Toast.makeText(getActivity(), "您已打开通知!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "您已关闭通知!", Toast.LENGTH_SHORT).show();
                CloseNotification();
                vibrateSwitch.setClickable(false);
            }
        }
    };

    public CompoundButton.OnCheckedChangeListener vibListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                VibrateController("1");
                Toast.makeText(getActivity(), "您已开启震动!", Toast.LENGTH_SHORT).show();
            }else{
                VibrateController("0");
                Toast.makeText(getActivity(), "您已关闭震动!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void OpenNotification(){
        AlarmUtil alarmUtil = new AlarmUtil();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("notification_setting","1");
        editor.apply();
        alarmUtil.openAlarm(getActivity(), 4);
    }

    public void CloseNotification(){
        AlarmUtil alarmUtil = new AlarmUtil();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("notification_setting","0");
        editor.apply();
        alarmUtil.closeAlarm(getActivity());
    }

    public void VibrateController(String open){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("vibrate_setting",open);
        editor.apply();
    }


}
