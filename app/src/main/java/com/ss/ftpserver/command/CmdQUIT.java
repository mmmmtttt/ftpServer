package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.ftpService.CommandChannel;

public class CmdQUIT extends Command{
    private static final String TAG = "CmdQUIT";

    /**
     * arg应该是""
     */
    @Override
    public void run() {
        Log.d(TAG, "execute: ");
        cmdChannel.writeResponse("221 Service closing control connection.");
        cmdChannel.cleanUp();
    }
}
