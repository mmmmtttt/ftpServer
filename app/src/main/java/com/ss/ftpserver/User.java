package com.ss.ftpserver;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String name;
    public String pass;
    public String path;
    boolean isLegal;

    public User(String name,String pass,String path,boolean isLegal){
        this.name = name;
        this.pass = pass;
        this.path = path;
        this.isLegal = isLegal;
    }

    /**
     * 默认的两个user
     * 1.legal:test/test
     * 2.illegal:pikachu / pikachu
     */
    public static String getDefaultUsers(){
        String path = MyApplication.getContext().getFilesDir().getPath();

        User legal = new User("test","test",path+"test",true);
        User illegal = new User("pikachu","pikachu",path+"pikachu",false);
        List<User> users = new ArrayList<>();
        users.add(legal);
        users.add(illegal);
        Gson gson = new Gson();
        return gson.toJson(users);
    }
}
