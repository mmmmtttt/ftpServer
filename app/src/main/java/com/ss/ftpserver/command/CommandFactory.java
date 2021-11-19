package com.ss.ftpserver.command;

import com.ss.ftpserver.ftpService.CommandChannel;

/**
 * 工厂类，利用反射对不同命令返回command类
 */
public class CommandFactory {
    /*
    抛出命令不存在的异常
     */
    public static void dealCommand(String cmdLine, CommandChannel channel) throws Exception{
        String[] parts = cmdLine.split(" ",2);
        Command command = (Command) Class.forName("Cmd"+parts[0].toUpperCase()).newInstance();//创建对象
        command.execute(parts[1],channel);
    }
}