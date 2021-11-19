package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.ftpService.CommandChannel;
import com.ss.ftpserver.ftpService.Settings;

import java.net.Socket;

import lombok.SneakyThrows;

public class CmdPORT implements Command {
    private static final String TAG = "CmdPORT";
    /**
     * PORT h1,h2,h3,h4,p1,p2
     * 主动模式，客户端发送它的随机数据端口给服务端连接
     * 服务端打开默认数据端口2020去连接
     * 非默认模式
     */
    @SneakyThrows
    @Override
    public void execute(String hostPort, CommandChannel channel) {
        Log.d(TAG, "execute: ");
        String[] host = hostPort.split(",");
        if (host.length != 6){
            channel.writeResponse("500 Syntax error, command unrecognized.");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder(16);
        for (int i = 0; i < 4; i++) {
            stringBuilder.append(host[i]);
            stringBuilder.append(".");
        }
        String ip = stringBuilder.substring(0,stringBuilder.length()-1);//去掉最后一个.
        int port = Integer.parseInt(host[4])+(Integer.parseInt(host[5])<<8);
        Socket dataSocket = new Socket(ip,port, Settings.getLocalIpAddress(),2121);
        channel.getDataChannel().setPortSocket(dataSocket);
        channel.writeResponse("220 Service ready. Data channel is open.");
    }
}
