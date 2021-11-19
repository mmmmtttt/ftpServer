package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.ftpService.CommandChannel;
import com.ss.ftpserver.ftpService.User;

public class CmdPASS implements Command{
    private static final String TAG = "CmdPASS";
    /**
     * 密码不一致 关闭连接
     * 密码一致 非法用户 关闭连接
     * 密码一致 合法用户 成功登陆
     */
    @Override
    public void execute(String pass, CommandChannel channel) {
        Log.d(TAG, "execute: ");
        User expectedUser = channel.getUser();
        if (!pass.equals(expectedUser.pass)){
            channel.closeConnection(true);
            channel.writeResponse("530 Password wrong. Not logged in.");
        }else if (!expectedUser.isLegal){
            channel.closeConnection(true);
            channel.writeResponse( "530 Illegal User. Not logged in.");
        }else{//合法用户
            channel.setLogIn(true);
            channel.writeResponse( "230 User logged in, proceed.");
        }
    }
}
