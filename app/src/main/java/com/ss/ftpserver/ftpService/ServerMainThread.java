package com.ss.ftpserver.ftpService;

import android.os.Bundle;
import android.util.Log;

import com.ss.ftpserver.gui.HomeFragment;
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

    @Override
    public void run() {
        Log.d(TAG, "run: ");
        try {
            serverSocket = new ServerSocket(Settings.getPort());
        } catch (IOException e) {
            Log.e(TAG, "run: serversocket open error");
            return;
        }
        Log.d(TAG, "run: server socket listen at port " + serverSocket.getLocalPort());
        fixedThreadPool = Executors.newFixedThreadPool(5);//创建线程池
        while (true) {
            Socket socket;
            try {
                socket = serverSocket.accept();
            } catch (SocketException e){//服务器停止运行时会关闭serverSocket，捕捉此异常结束服务器主线程
                break;
            } catch (IOException e) {
                Log.e(TAG, "run: serversocket accept error");
                continue;
            }
            Log.i(TAG, "accept connection from " + socket.getRemoteSocketAddress().toString());
            Thread commandChannel = new CommandChannel(socket);//和每一个客户端的请求建立命令信道
            fixedThreadPool.execute(commandChannel);
        }
        Log.d(TAG, "run: thread terminate");
    }

    /**
     * 服务器停止服务时的清理工作
     */
    public void cleanUp() {
        try {
            serverSocket.close();
            Log.d(TAG, "cleanUp: cleanup finish");
        } catch (IOException e) {
            Log.e(TAG, "cleanUp: serverSocket close error");
        }
        if (fixedThreadPool != null) {
            fixedThreadPool.shutdownNow();
        }
    }
}
