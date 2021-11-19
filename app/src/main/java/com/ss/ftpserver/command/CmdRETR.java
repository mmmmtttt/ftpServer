package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.exception.DataLinkOpenException;
import com.ss.ftpserver.ftpService.CommandChannel;
import com.ss.ftpserver.ftpService.Settings;

import java.io.File;
import java.io.IOException;

public class CmdRETR implements Command {
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
    public void execute(String filename, CommandChannel channel) {
        Log.d(TAG, "execute: ");
        File file = new File(Settings.getFilePath() + File.separator + filename);
        if (!file.exists()) {
            channel.writeResponse("550 Requested action not taken. File not found.");
        } else {//文件存在
            if (channel.getDataChannel()!=null) {
                channel.writeResponse("425 Can’t open data connection.Close data connection.");
                return;
            }
            try {
                channel.getDataChannel().sendFile(file);
            } catch (DataLinkOpenException e) {
                channel.writeResponse("425 Can’t open data connection.Close data connection.");
            }  catch (IOException e) {
                channel.writeResponse("451 Requested action aborted: local error in processing.");
            } finally {//关闭socket连接
                channel.getDataChannel().cleanUp();
            }
        }
    }
}
