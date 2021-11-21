package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.ftpService.CommandChannel;
import com.ss.ftpserver.ftpService.User;

public class CmdPASS extends Command{
    private static final String TAG = "CmdPASS";

    /**
     * 密码不一致 关闭连接
     * 密码一致 非法用户 关闭连接
     * 密码一致 合法用户 成功登陆
     */
    @Override
    public void run() {
        Log.d(TAG, "execute: ");
        User expectedUser = cmdChannel.getUser();
        if (!arg.equals(expectedUser.pass)){
            cmdChannel.writeResponse("530 Password wrong. Not logged in.");
            cmdChannel.cleanUp();
        }else if (!expectedUser.isLegal){
            cmdChannel.writeResponse( "530 Illegal User. Not logged in.");
            cmdChannel.cleanUp();
        }else{//合法用户
            cmdChannel.setLogIn(true);
            cmdChannel.writeResponse( "230 User logged in, proceed.");
        }
    }
}
