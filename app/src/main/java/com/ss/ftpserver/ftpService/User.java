package com.ss.ftpserver.ftpService;

import com.google.gson.Gson;
import com.ss.ftpserver.gui.MyApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    public String name;
    public String pass;
    public boolean isLegal;

    public User(String name){
        this.name = name;
    }

    public User(String name,String pass,boolean isLegal){
        this.name = name;
        this.pass = pass;
        this.isLegal = isLegal;
    }

    public String getName() {
        return name;
    }

    /**
     * 默认的两个user
     * 1.legal:test/test
     * 2.illegal:pikachu / pikachu
     */
    public static String getDefaultUsers(){
        User legal = new User("test","test",true);
        User illegal = new User("pikachu","pikachu",false);
        List<User> users = new ArrayList<>();
        users.add(legal);
        users.add(illegal);
        Gson gson = new Gson();
        String result = gson.toJson(users);
        Settings.writeDefaultUsers(result);//在shared preference中写入默认的用户
        return result;
    }
}
