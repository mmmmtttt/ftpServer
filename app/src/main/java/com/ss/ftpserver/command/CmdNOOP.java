package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.ftpService.CommandChannel;

public class CmdNOOP implements Command{
    private static final String TAG = "CmdNOOP";

    /**
     * NOOP <CRLF>
     * arg应该是""
     */
    @Override
    public void execute(String arg, CommandChannel channel) {
        Log.d(TAG, "execute: ");
        if ("".equals(arg)) {
            channel.writeResponse("200 Command okay.");
        }else {
            channel.writeResponse("500 Syntax error, command unrecognized.");
        }
    }
}
