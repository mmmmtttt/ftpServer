package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.ftpService.CommandChannel;
import com.ss.ftpserver.ftpService.Settings;
import com.ss.ftpserver.ftpService.User;
import java.util.List;

public class CmdUSER implements Command {
    private static final String TAG = "CmdUSER";

    @Override
    public void execute(String username, CommandChannel channel){
        Log.d(TAG, "execute: ");
        List<User> userList = Settings.getUsers();
        if (username.equals("anonymous")){//匿名用户
            channel.recordUser(new User("anonymous"),true);
            channel.writeResponse( "230 User logged in, proceed.");
            return;
        }
        User user = userList.stream().filter(item -> username.equals(item.getName()))
                .findAny().orElse(null);
        if (user==null){//不存在此用户名的用户,关闭连接
            channel.closeConnection(true);
            channel.writeResponse( "332 Need account for login.");
        }else {//存在此用户名的用户，还需要密码验证
            channel.recordUser(user,false);
            channel.writeResponse( "331 User name okay, need password.");
        }
    }
}
