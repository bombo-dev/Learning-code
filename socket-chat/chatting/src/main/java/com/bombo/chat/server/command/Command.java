package com.bombo.chat.server.command;


import com.bombo.chat.server.Session;
import com.bombo.chat.server.SocketManager;

public interface Command {

    void execute(Session session, SocketManager socketManager);
}
