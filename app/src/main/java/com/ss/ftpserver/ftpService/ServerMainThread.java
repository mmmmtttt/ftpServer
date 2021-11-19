package com.ss.ftpserver.ftpService;

import android.util.Log;

import com.ss.ftpserver.gui.MyApplication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.SneakyThrows;

/**
 * 服务端的主线程
 */
public class ServerMainThread extends Thread {
    ServerSocket serverSocket;
    private static final String TAG = "ServerMainThread";

    @SneakyThrows
    @Override
    public void run() {
        serverSocket = new ServerSocket(Settings.getPort());
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);//创建线程池
        while (true) {
            Socket socket = serverSocket.accept();
            Log.i(TAG,socket.getRemoteSocketAddress().toString());
            Thread commandChannel = new CommandChannel(socket);
            fixedThreadPool.execute(commandChannel);
        }
    }
}
