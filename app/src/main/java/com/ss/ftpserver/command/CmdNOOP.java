package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.ftpService.CommandChannel;

public class CmdNOOP extends Command{
    private static final String TAG = "CmdNOOP";

    /**
     * NOOP <CRLF>
     * arg应该是""
     */
    @Override
    public void run() {
        Log.d(TAG, "execute: ");
        if ("".equals(arg)) {
            cmdChannel.writeResponse("200 Command okay.");
        }else {
            cmdChannel.writeResponse("500 Syntax error, command unrecognized.");
        }
    }
}
