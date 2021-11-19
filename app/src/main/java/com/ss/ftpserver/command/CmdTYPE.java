package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.ftpService.CommandChannel;

public class CmdTYPE implements Command{
    private static final String TAG = "CmdTYPE";
    /**
     * TYPE <SP> <type-code> <CRLF>
     * type-code 为 A(ascii) 或者 B(binary)
     * 默认A
     */
    @Override
    public void execute(String typeCode, CommandChannel channel) {
        Log.d(TAG, "execute: ");
        String response = "200 Command okay.";
        switch (typeCode){
            case "A":
                channel.getDataChannel().setBinaryMode(false);
                break;
            case "B":
                channel.getDataChannel().setBinaryMode(true);
                break;
            default:
                response = "500 Syntax error, command unrecognized.";
                break;
        }
        channel.writeResponse(response);
    }
}
