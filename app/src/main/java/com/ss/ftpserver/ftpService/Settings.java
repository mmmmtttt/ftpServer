package com.ss.ftpserver.ftpService;

import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;import com.ss.ftpserver.ftpService.User;
import com.ss.ftpserver.gui.MyApplication;

import java.util.List;

/**
 * 管理shared preference中的数据
 */
public class Settings {
    /**
     * 从sp 中得到存储的users数据
     */
    public static List<User> getUsers(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        String users = sp.getString("users",User.getDefaultUsers());
        //从json字符串恢复成List<User>
        Gson gson = new Gson();
        List userList = gson.fromJson(users,new TypeToken<List<User>>(){}.getType());
        return userList;
    }
}
