package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.exception.DataLinkOpenException;
import com.ss.ftpserver.ftpService.CommandChannel;
import com.ss.ftpserver.ftpService.Settings;

import java.io.File;
import java.io.IOException;

import lombok.SneakyThrows;

public class CmdSTOR implements Command{
    private static final String TAG = "CmdSTOR";
    /**
     * STOR <SP> <pathname> <CRLF>
     *
     * causes the server-DTP to accept the data
     * transferred via the data connection and to store the data as
     * a file at the server site.  If the file specified in the
     * pathname exists at the server site, then its contents shall
     * be replaced by the data being transferred.  A new file is
     * created at the server site if the file specified in the
     * pathname does not already exist.
     */
    @SneakyThrows
    @Override
    public void execute(String filename, CommandChannel channel) {
        Log.d(TAG, "execute: ");
        File file = new File(Settings.getFilePath() + File.separator + filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        if (channel.getDataChannel() == null) {
            channel.writeResponse("425 Can’t open data connection.");
            return;
        }
        try {
            channel.getDataChannel().receiveFile(file);
        } catch (DataLinkOpenException e) {
            channel.writeResponse("425 Can’t open data connection.Close data connection.");
        }  catch (IOException e) {
            channel.writeResponse("451 Requested action aborted: local error in processing.");
        } finally {//关闭socket连接
            channel.getDataChannel().cleanUp();
        }
    }
}
