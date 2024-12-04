package com.bombo.chat.server.command;

import com.bombo.chat.server.Session;
import com.bombo.chat.server.SocketManager;

import java.io.DataOutputStream;

public class MessageCommand implements Command {

    private String message;

    public MessageCommand(String message) {
        this.message = message;
    }

    @Override
    public void execute(Session session, SocketManager socketManager) {
        socketManager.getSessions()
                .forEach(s -> {
                    try {
                        DataOutputStream output = s.getOutput();
                        output.writeUTF(session.getUserName() + " : " + message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
