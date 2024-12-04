package com.bombo.chat.server.command;


import com.bombo.chat.server.Session;
import com.bombo.chat.server.SocketManager;

public class ChangeNameCommand implements Command {

    private final String name;

    public ChangeNameCommand(String name) {
        this.name = name;
    }

    @Override
    public void execute(Session session, SocketManager socketManager) {
        session.setUserName(name);
    }
}
