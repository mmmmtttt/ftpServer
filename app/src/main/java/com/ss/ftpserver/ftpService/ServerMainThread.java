package com.ss.ftpserver.ftpService;

import android.util.Log;

import com.ss.ftpserver.gui.MyApplication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.SneakyThrows;

/**
 * 服务端的主线程
 */
public class ServerMainThread extends Thread {
    ServerSocket serverSocket;
    ExecutorService fixedThreadPool;
    private static final String TAG = "ServerMainThread";
    public volatile boolean isRunning = true; //标志位,结束线程运行

    @SneakyThrows
    @Override
    public void run() {
        Log.d(TAG, "run: ");
        serverSocket = new ServerSocket(Settings.getPort());
        fixedThreadPool = Executors.newFixedThreadPool(5);//创建线程池
        while (isRunning) {
            Socket socket;
            try {
                socket = serverSocket.accept();
                Log.i(TAG, socket.getRemoteSocketAddress().toString());
                Thread commandChannel = new CommandChannel(socket);//和每一个客户端的请求建立命令信道
                fixedThreadPool.execute(commandChannel);
            }catch (SocketException e){
                Log.d(TAG, "run: stop running");
                isRunning = false;
            }
        }
    }

    public void cleanUp() {
        try {
            isRunning = false;
            serverSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "cleanUp: serverSocket close error");
        }
        if (fixedThreadPool != null) {
            fixedThreadPool.shutdown();
        }
    }
}
