package com.ss.ftpserver.gui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ss.ftpserver.R;
import com.ss.ftpserver.databinding.FragmentHomeBinding;
import com.ss.ftpserver.ftpService.FtpService;

public class HomeFragment extends Fragment {
    private static FragmentHomeBinding binding;//使用视图绑定代替findViewById

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("HomeFrag","resume");
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.startStopButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Activity activity = getActivity();
                if (isChecked) {
                    Intent startIntent = new Intent(activity, FtpService.class);
                    activity.startForegroundService(startIntent);
                } else {
                    Intent stopIntent = new Intent(activity, FtpService.class);
                    activity.stopService(stopIntent);
                    binding.serverIp.setText("");
                }
            }});
    }

    //Fragment 的存在时间比其视图长。在 Fragment 的 onDestroyView() 方法中清除对绑定类实例的所有引用。
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * 从ftpService异步传递消息，在主页面显示server运行的ip和端口
     */
    public static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            binding.serverIp.setText("address\n"+msg.getData().getString("serverip"));
            return false;
        }
    });

}