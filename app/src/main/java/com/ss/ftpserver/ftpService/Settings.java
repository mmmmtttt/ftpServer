package com.ss.ftpserver.ftpService;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ss.ftpserver.ftpService.User;
import com.ss.ftpserver.gui.MyApplication;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

/**
 * 管理shared preference中的数据
 */
public class Settings {
    static SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
    static List<User> userList = null;
    static InetAddress ip = null;

    /**
     * 从sp 中得到存储的users数据
     */
    public static List<User> getUsers() {
        if (userList == null) {//如果为null，先从shared preference中读取user
            String users = sp.getString("users", User.getDefaultUsers());
            //从json字符串恢复成List<User>
            Gson gson = new Gson();
            userList = gson.fromJson(users, new TypeToken<List<User>>() {
            }.getType());
        }
        return userList;
    }

    /**
     * sp中没有user信息时写入默认的user信息
     */
    public static void writeDefaultUsers(String content) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user", content);
        editor.apply();
    }

    public static int getPort() {
        String port = sp.getString("port", "2121");
        return Integer.parseInt(port);
    }

    public static String getFilePath(){
        return MyApplication.getContext().getFilesDir().getPath();
    }

    /**
     * 获取本机内网ip（要求客户端和服务器在同一局域网内）
     */
    public static InetAddress getLocalIpAddress(){
        if (ip!=null){
            return ip;
        }
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip != null && ip instanceof Inet4Address) {
                            return ip;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Utils", "getIpAddress:IP地址获取失败");
        }
        return null;
    }
}
