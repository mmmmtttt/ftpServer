package com.ss.ftpserver.command;

import com.ss.ftpserver.exception.CommandNotSupportException;
import com.ss.ftpserver.ftpService.CommandChannel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 工厂类，利用反射对不同命令返回command类
 */
public class CommandFactory {
    public static ExecutorService fixedThreadPool = null;

    /*
    抛出命令不存在的异常
     */
    public static void dealCommand(String cmdLine, CommandChannel channel) throws CommandNotSupportException {
        if (fixedThreadPool == null){
            fixedThreadPool = Executors.newFixedThreadPool(5);
        }
        String[] parts = cmdLine.split(" ",2);
        Command command = null;//创建对象
        try {
            command = (Command) Class.forName("com.ss.ftpserver.command."+"Cmd"+parts[0].toUpperCase()).newInstance();
        } catch (Exception e){
            throw new CommandNotSupportException();
        }
        String arg = "";
        if (parts.length==2){//有参数
            arg = parts[1];
        }
        command.init(arg,channel);
        fixedThreadPool.execute(command);
    }
}