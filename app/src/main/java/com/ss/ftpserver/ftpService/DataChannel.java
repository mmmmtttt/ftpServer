package com.ss.ftpserver.ftpService;

import android.util.Log;

import com.ss.ftpserver.exception.DataLinkOpenException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import lombok.SneakyThrows;

public class DataChannel {
    private static final String TAG = "DataChannel";
    ServerSocket pasvListenSocket;//被动模式下
    Socket pasvClientSocket;
    Socket portSocket;//主动模式下
    boolean binaryMode;
    boolean portMode;
    CommandChannel commandChannel;

    public DataChannel(CommandChannel channel) {
        this.commandChannel = channel;
    }

    public void setPasvListenSocket(ServerSocket pasvListenSocket) {
        this.pasvListenSocket = pasvListenSocket;
        portMode = false;
    }

    public void setPortSocket(Socket portSocket) {
        this.portSocket = portSocket;
        portMode = true;
    }

    public void setBinaryMode(boolean binaryMode) {
        this.binaryMode = binaryMode;
    }

    /**
     * 针对两种模式发送文件，在上层对异常的处理中关闭socket数据连接
     */
    public void sendFile(File file) throws DataLinkOpenException, IOException {
        OutputStream out = null;
        try {
            if (portMode) {
                out = portSocket.getOutputStream();
            } else {
                commandChannel.writeResponse("150 File status okay; about to open data connection.");//提示客户端可以主动建立连接
                pasvClientSocket = pasvListenSocket.accept();
                out = pasvClientSocket.getOutputStream();
            }
        }catch (IOException e){
            throw new DataLinkOpenException();
        }catch (NullPointerException e){//socket为空
            throw new DataLinkOpenException();
        }
        commandChannel.writeResponse("125 Data connection already open; transfer starting.");

        if (binaryMode) {//二进制流读写
            try (//try-with-resource，保证一定关闭流
                    BufferedOutputStream bo = new BufferedOutputStream(out);
                    BufferedInputStream bi = new BufferedInputStream(new FileInputStream(file))) {
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = bi.read(buffer)) != -1) {
                    bo.write(buffer);
                }
            } catch (IOException e) {
                Log.e(TAG, "sendFile: ioexception");
                throw e;
            }
        } else {//ascii码模式
            try (
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.US_ASCII));
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.US_ASCII))) {
                String line;
                while ((line = br.readLine()) != null) {
                    bw.write(line + "\r\n");
                }
            } catch (IOException e) {
                Log.e(TAG, "sendFile: ioexception");
                throw e;
            }
        }
        commandChannel.writeResponse("250 Requested file action okay, completed.");
    }

    public void receiveFile(File file) throws DataLinkOpenException, IOException {
        InputStream in = null;
        try {
            if (portMode) {//主动模式
                in = portSocket.getInputStream();
            } else {//被动模式
                commandChannel.writeResponse("150 File status okay; about to open data connection.");//提示客户端可以主动建立连接
                pasvClientSocket = pasvListenSocket.accept();
                in = pasvClientSocket.getInputStream();
            }
        } catch (IOException e) {
            throw new DataLinkOpenException();
        }
        commandChannel.writeResponse("125 Data connection already open; transfer starting.");

        if (binaryMode) {//二进制流读写
            try (
                    BufferedInputStream bi = new BufferedInputStream(in);
                    BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(file))) {
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = bi.read(buffer)) != -1) {
                    bo.write(buffer);
                }
            } catch (IOException e) {
                throw e;
            }
        } else {//ascii码模式
            try (
                    BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.US_ASCII));
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.US_ASCII))) {
                String line;
                while ((line = br.readLine()) != null) {
                    bw.write(line + "\r\n");
                }
            } catch (IOException e) {
                throw e;
            }
        }
        commandChannel.writeResponse("250 Requested file action okay, completed.");
    }

    /**
     * 每次文件传输完的清理工作
     */
    public void closeSocket() {
        try {
            if (portMode) {
                portSocket.close();
                portSocket = null;
            } else {
                pasvListenSocket.close();
                pasvListenSocket = null;
                pasvClientSocket.close();
                pasvClientSocket = null;
            }
            Log.d(TAG, "closeSocket: cleanup finish");
        }catch (Exception ignore){
        }
    }
}
