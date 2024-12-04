package com.bombo.chat.server;

import com.bombo.chat.server.command.Command;

public class CommandExecutor implements Runnable {

    private final Command command;
    private final Session session;
    private final SocketManager socketManager;

    public CommandExecutor(Command command, Session session, SocketManager socketManager) {
        this.command = command;
        this.session = session;
        this.socketManager = socketManager;
    }

    @Override
    public void run() {
        command.execute(session, socketManager);
    }
}
