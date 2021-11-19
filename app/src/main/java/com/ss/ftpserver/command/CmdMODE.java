package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.ftpService.CommandChannel;

public class CmdMODE implements Command{
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
    public void execute(String typeCode, CommandChannel channel) {
        Log.d(TAG, "execute: ");
        if ("S".equals(typeCode) || "B".equals(typeCode) || "C".equals(typeCode)) {
            channel.writeResponse("200 Command okay.");
        } else {
            channel.writeResponse("500 Syntax error, command unrecognized.");
        }
    }
}
