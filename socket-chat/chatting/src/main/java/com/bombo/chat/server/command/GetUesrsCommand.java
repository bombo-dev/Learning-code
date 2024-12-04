package com.bombo.chat.server.command;


import com.bombo.chat.server.Session;
import com.bombo.chat.server.SocketManager;

import java.io.DataOutputStream;
import java.io.IOException;

import static com.bombo.chat.common.MyLogger.log;

public class GetUesrsCommand implements Command {

    @Override
    public void execute(Session session, SocketManager socketManager) {
        DataOutputStream output = session.getOutput();
        try {
            output.writeUTF("현재 접속자 수: " + socketManager.getSessions().size() + "명");
            output.writeUTF("접속 중인 사용자 목록 ");
        } catch (IOException e) {
            log("GetUesrsCommand execute error");
        }
        socketManager.getSessions()
                .forEach(s -> {
                    try {
                        output.writeUTF(s.getUserName() + "\n");
                    } catch (IOException e) {
                        log(e);
                    }
                });
    }
}
