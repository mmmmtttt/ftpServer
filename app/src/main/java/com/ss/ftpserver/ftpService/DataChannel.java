package com.ss.ftpserver.ftpService;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import lombok.SneakyThrows;

public class DataChannel {
    ServerSocket pasvSocket;//被动模式下
    Socket portSocket;//主动模式下
    boolean binaryMode;
    boolean portMode;
    CommandChannel commandChannel;

    public DataChannel(CommandChannel channel) {
        this.commandChannel = channel;
    }

    public void setPasvSocket(ServerSocket pasvSocket) {
        this.pasvSocket = pasvSocket;
        portMode = false;
    }

    public void setPortSocket(Socket portSocket) {
        this.portSocket = portSocket;
        portMode = true;
    }

    public void setBinaryMode(boolean binaryMode) {
        this.binaryMode = binaryMode;
    }

    public void transferFile(File file) {
        OutputStream out = null;
        try {
            if (portMode) {
                out = portSocket.getOutputStream();
            } else {
                out = pasvSocket.accept().getOutputStream();
            }
        } catch (IOException e) {
            commandChannel.writeResponse("425 Can’t open data connection.");
            return;
        }
        commandChannel.writeResponse("125 Data connection already open; transfer starting.");

        if (binaryMode) {//二进制流读写
            try (
                    BufferedOutputStream bo = new BufferedOutputStream(out);
                    BufferedInputStream bi = new BufferedInputStream(new FileInputStream(file))) {
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = bi.read(buffer)) != -1) {
                    bo.write(buffer);
                }
            } catch (Exception e) {
                commandChannel.writeResponse("451 Requested action aborted: local error in processing.");
                return;
            }
        } else {//ascii码模式
            try (
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.US_ASCII));
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.US_ASCII))) {
                String line;
                while ((line = br.readLine()) != null) {
                    bw.write(line + "\r\n");
                }
            } catch (Exception e) {
                commandChannel.writeResponse("451 Requested action aborted: local error in processing.");
                return;
            }
        }
        commandChannel.writeResponse("250 Requested file action okay, completed.");
        //TODO:关闭socket
    }

    @SneakyThrows
    public void cleanUp() {
        if (pasvSocket != null) {
            pasvSocket.close();
        }
        if (portSocket != null) {
            portSocket.close();
        }
    }

    public boolean readyTransfer() {
        if (pasvSocket != null || portSocket != null) {
            return true;
        } else {
            return false;
        }
    }
}
