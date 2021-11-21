package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.ftpService.CommandChannel;

public class CmdMODE extends Command{
    private static final String TAG = "CmdMODE";

    /**
     * MODE <SP> <mode-code> <CRLF>
     * S - Stream
     * B - Block
     * C - Compressed
     * The default transfer mode is Stream.
     * 简化处理，一律按stream处理
     */
    @Override
    public void run() {
        Log.d(TAG, "execute: ");
        if ("S".equals(arg) || "B".equals(arg) || "C".equals(arg)) {
            cmdChannel.writeResponse("200 Command okay.");
        } else {
            cmdChannel.writeResponse("500 Syntax error, command unrecognized.");
        }
    }
}
