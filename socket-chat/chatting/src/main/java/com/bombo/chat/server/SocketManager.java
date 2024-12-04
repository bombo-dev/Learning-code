package com.bombo.chat.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SocketManager {

    private List<Session> sessions = new ArrayList<>();

    public synchronized void add(Session session) {
        if (sessions.contains(session)) {
            return;
        }
        sessions.add(session);
    }

    public synchronized void remove(Session session) {
        sessions.remove(session);
    }

    public synchronized void clear() {
        for (Session session : sessions) {
            session.close();
        }

        sessions.clear();
    }

    public List<Session> getSessions() {
        return Collections.unmodifiableList(sessions);
    }
}
