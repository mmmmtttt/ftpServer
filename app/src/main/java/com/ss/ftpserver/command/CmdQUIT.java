package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.ftpService.CommandChannel;

public class CmdQUIT implements Command{
    private static final String TAG = "CmdQUIT";
    /**
     * arg应该是""
     */
    @Override
    public void execute(String arg, CommandChannel channel) {
        Log.d(TAG, "execute: ");
        channel.closeConnection(true);
        channel.writeResponse("221 Service closing control connection.");
    }
}
