package com.ss.ftpserver.command;

import com.ss.ftpserver.ftpService.CommandChannel;

public interface Command {
    public void execute(String arg, CommandChannel channel);
}
