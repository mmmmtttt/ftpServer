package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.ftpService.CommandChannel;

public class CmdTYPE extends Command{
    private static final String TAG = "CmdTYPE";

    /**
     * TYPE <SP> <type-code> <CRLF>
     * type-code 为 A(ascii) 或者 B(binary)
     * 默认A
     */
    @Override
    public void run() {
        Log.d(TAG, "execute: ");
        String response = "200 Command okay.";
        switch (arg){
            case "A":
                cmdChannel.getDataChannel().setBinaryMode(false);
                break;
            case "B":
                cmdChannel.getDataChannel().setBinaryMode(true);
                break;
            default:
                response = "500 Syntax error, command unrecognized.";
                break;
        }
        cmdChannel.writeResponse(response);
    }
}
