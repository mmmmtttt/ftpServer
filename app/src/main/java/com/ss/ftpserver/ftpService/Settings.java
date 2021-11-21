package com.ss.ftpserver.ftpService;

import android.content.Context;
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
    private static final String TAG = "Settings";
    static SharedPreferences sp = MyApplication.getContext().getSharedPreferences("users", Context.MODE_PRIVATE);
    static List<User> userList = null;
    static InetAddress ip = null;

    /**
     * 从sp 中得到存储的users数据
     */
    public static List<User> getUsers() {
        if (userList == null) {//如果为null，先从shared preference中读取user
            String users = sp.getString("users", "");
            if ("".equals(users)){
                users = User.getDefaultUsers();//在其中把默认的user写入了shared preference.
            }
            Log.d(TAG, "getUsers: "+users);
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
    public static String writeUsers(List<User> users) {
        Gson gson = new Gson();
        String result = gson.toJson(users);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("users", result);
        editor.commit();
        Log.d(TAG, "writeUsers: "+result );
        return result;
    }

    public static int getPort() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        String port = sp.getString("port", "2121");
        return Integer.parseInt(port);
    }

    public static String getFilePath(){
        return MyApplication.getContext().getExternalFilesDir(null).getPath();
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
