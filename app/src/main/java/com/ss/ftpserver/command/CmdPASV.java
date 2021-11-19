package com.ss.ftpserver.command;

import android.util.Log;

import com.ss.ftpserver.ftpService.CommandChannel;
import com.ss.ftpserver.ftpService.Settings;

import java.net.ServerSocket;

import lombok.SneakyThrows;

public class CmdPASV implements Command{
    private static final String TAG = "CmdPASV";
    /**
     * 服务器随机打开一个端口，发送给客户端
     * 参数应该是空
     * 默认模式
     */
    @SneakyThrows
    @Override
    public void execute(String arg, CommandChannel channel) {
        Log.d(TAG, "execute: ");
        ServerSocket socket = new ServerSocket(0);//由系统指定一个可用的端口号
        int port = socket.getLocalPort();
        int p1 = port>>8;
        int p2 = port-(p1<<8);
        String ip = Settings.getLocalIpAddress().getHostAddress();
        ip = ip.replace(".",",");
        channel.getDataChannel().setPasvListenSocket(socket);
        channel.writeResponse("227 Entering Passive Mode ("+ip+","+p1+","+p2+").");
    }
}
