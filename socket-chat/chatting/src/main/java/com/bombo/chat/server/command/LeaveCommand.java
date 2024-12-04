package com.bombo.chat.server.command;

import com.bombo.chat.server.Session;
import com.bombo.chat.server.SocketManager;

import java.io.DataOutputStream;
import java.util.List;

import static com.bombo.chat.common.MyLogger.log;

public class LeaveCommand implements Command {

    @Override
    public void execute(Session session, SocketManager socketManager) {
        List<Session> sessions = socketManager.getSessions();

        for (Session s : sessions) {
            try {
                if (s.equals(session)) {
                    DataOutputStream output = s.getOutput();
                    output.writeUTF("채팅방을 나갑니다.");
                    continue;
                }

                DataOutputStream output = s.getOutput();
                output.writeUTF(session.getUserName() + " 님이 채팅방을 나갔습니다.");
            } catch (Exception e) {
                log(e);
            }
        }

        socketManager.remove(session);
        session.close();
    }
}
