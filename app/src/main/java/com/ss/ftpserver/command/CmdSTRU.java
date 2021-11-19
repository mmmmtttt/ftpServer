package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.ftpService.CommandChannel;

public class CmdSTRU implements Command {
    private static final String TAG = "CmdSTRU";
    /**
     * STRU <SP> <structure-code> <CRLF>
     * F - File (no record structure)
     * R - Record structure
     * P - Page structure
     * The default structure is File.
     * 简化处理，都按照F来实现
     */
    @Override
    public void execute(String typeCode, CommandChannel channel) {
        Log.d(TAG, "execute: ");
        if ("F".equals(typeCode) || "R".equals(typeCode) || "P".equals(typeCode)) {
            channel.writeResponse("200 Command okay.");
        } else {
            channel.writeResponse("500 Syntax error, command unrecognized.");
        }
    }
}
