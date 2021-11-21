package com.ss.ftpserver.command;

import com.ss.ftpserver.ftpService.CommandChannel;

/**
 * 每个指令开新的线程，为了数据传输时不阻塞之后可能发送的QUIT（当即退出）
 */
public abstract class Command implements Runnable{
    public String arg;
    public CommandChannel cmdChannel;
    public void init(String arg,CommandChannel cmdChannel){
        this.arg = arg;
        this.cmdChannel = cmdChannel;
    }
}
