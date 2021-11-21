package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.exception.DataLinkOpenException;
import com.ss.ftpserver.ftpService.Settings;

import java.io.File;
import java.io.IOException;

public class CmdRETR extends Command {
    private static final String TAG = "CmdRETR";

    /**
     * RETR <SP> <pathname> <CRLF>
     * causes the server-DTP to transfer a copy of the
     * file, specified in the pathname, to the user-DTP
     * at the other end of the data connection.  The status and
     * contents of the file at the server site shall be unaffected.
     * 简化处理，没有目录结构，pathname就是固定文件夹下的文件名
     */
    @Override
    public void run() {
        Log.d(TAG, "execute: ");
        if (!cmdChannel.isLogIn()){//客户端不应该允许没有登陆的用户上传下载，服务端只是检查一下防止前端绕过
            cmdChannel.writeResponse("332 Need account for action.");
            return;
        }
        File file = new File(Settings.getFilePath() + File.separator + arg);
        Log.d(TAG, "execute: "+file.getPath());
        if (!file.exists()) {
            cmdChannel.writeResponse("550 Requested action not taken. File not found.");
        } else {//文件存在
            try {
                cmdChannel.getDataChannel().sendFile(file);
            } catch (DataLinkOpenException e) {
                cmdChannel.writeResponse("425 Can’t open data connection.Close data connection.");
            }  catch (IOException e) {
                cmdChannel.writeResponse("451 Requested action aborted: local error in processing.");
            } finally {//关闭socket连接
                cmdChannel.getDataChannel().closeSocket();
            }
        }
    }
}
