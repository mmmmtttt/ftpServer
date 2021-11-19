package com.ss.ftpserver.ftpService;

import android.util.Log;

import com.ss.ftpserver.command.CommandFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import lombok.SneakyThrows;

/**
 * 命令信道，对每一个来自客户端的连接开设新线程
 */
public class CommandChannel extends Thread {
    private static final String TAG = "CommandChannel";
    Socket cmdSocket;
    DataChannel dataChannel;
    boolean closeConnection = false;
    BufferedWriter bw;
    User user;
    boolean logIn;

    public CommandChannel(Socket socket) {
        this.cmdSocket = socket;
        this.dataChannel = new DataChannel(this);
    }

    @SneakyThrows
    @Override
    public void run() {
        String line, response = null;
        //使用br，使用readLine方法，阻塞直到读到/r/n
        try (BufferedReader br = new BufferedReader(new InputStreamReader(cmdSocket.getInputStream(), "ascii"))) {
            bw = new BufferedWriter(new OutputStreamWriter(cmdSocket.getOutputStream(), "ascii"));
            writeResponse("220 Service ready for new user.");
            while ((line = br.readLine()) != null) { //一个指令只在末尾有crlf，每次读出一条指令;只有在数据流发生异常或者另一端被close()掉时，才会返回null值。、
                try {
                    CommandFactory.dealCommand(line, this);
                } catch (Exception e) {//命令不存在
                    writeResponse("502 Command not implemented.");
                }
                //如果是命令是Quit，跳出循环
                if (closeConnection)
                    break;
            }
        } finally {//有任何异常释放资源
            cleanUp();
        }
    }

    public void recordUser(User user, boolean logIn) {
        this.user = user;
        this.logIn = logIn;
    }

    public void closeConnection(boolean closeConnection) {
        this.closeConnection = closeConnection;
    }

    public User getUser() {
        return user;
    }

    public void setLogIn(boolean logIn) {
        this.logIn = logIn;
    }

    public DataChannel getDataChannel() {
        return dataChannel;
    }

    public void setDataChannel(DataChannel dataChannel) {
        this.dataChannel = dataChannel;
    }

    public void writeResponse(String response) {
        try {
            bw.write(response + "\r\n");
            bw.flush();
        } catch (IOException e) {
            Log.e(TAG, "writeResponse: " + e.getMessage());
            ;
        }
    }

    /**
     * 清理工作
     */
    @SneakyThrows
    private void cleanUp() {
        //关闭数据通道和控制通道，与客户端的会话关闭
        cmdSocket.close();
        bw.close();
        dataChannel.cleanUp();
    }
}
