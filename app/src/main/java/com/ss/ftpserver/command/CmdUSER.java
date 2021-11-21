package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.ftpService.CommandChannel;
import com.ss.ftpserver.ftpService.Settings;
import com.ss.ftpserver.ftpService.User;
import java.util.List;

public class CmdUSER extends Command {
    private static final String TAG = "CmdUSER";

    @Override
    public void run(){
        Log.d(TAG, "execute: ");
        List<User> userList = Settings.getUsers();
        if (arg.equals("anonymous")){//匿名用户
            cmdChannel.recordUser(new User("anonymous"),true);
            cmdChannel.writeResponse( "230 User logged in, proceed.");
            return;
        }
        User user = userList.stream().filter(item -> arg.equals(item.getName()))
                .findAny().orElse(null);
        if (user==null){//不存在此用户名的用户,关闭连接
            cmdChannel.writeResponse( "332 Need account for login.");
            cmdChannel.cleanUp();
        }else {//存在此用户名的用户，还需要密码验证
            cmdChannel.recordUser(user,false);
            cmdChannel.writeResponse( "331 User name okay, need password.");
        }
    }
}
