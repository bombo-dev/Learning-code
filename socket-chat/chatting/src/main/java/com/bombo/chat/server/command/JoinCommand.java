package com.bombo.chat.server.command;

import com.bombo.chat.server.Session;
import com.bombo.chat.server.SocketManager;

import java.io.DataOutputStream;

public class JoinCommand implements Command {

    private String userName;

    private JoinCommand(String userName) {
        this.userName = userName;
    }

    public static JoinCommand newCommand(String userName) {
        return new JoinCommand(userName);
    }

    @Override
    public void execute(Session session, SocketManager socketManager) {
        session.setUserName(userName);
        socketManager.add(session);

        try {
            socketManager.getSessions()
                            .forEach(s -> {
                                try {
                                    DataOutputStream output = s.getOutput();
                                    output.writeUTF(userName + " 님이 채팅방에 입장하였습니다.");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
