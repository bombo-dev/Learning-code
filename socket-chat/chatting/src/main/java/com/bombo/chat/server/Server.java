package com.bombo.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.bombo.chat.common.MyLogger.log;

public class Server {

    public static final int PORT = 55535;

    public static void main(String[] args) throws IOException {
        log("서버 시작");
        SocketManager socketManager = new SocketManager();
        ServerSocket serverSocket = new ServerSocket(PORT);
        log("서버 소켓 시작 - 리스닝 포트: " + PORT);

        ShutdownHook shutdownHook = new ShutdownHook(serverSocket, socketManager);
        Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook, "shutdown"));

        try {
            while (true) {
                Socket socket = serverSocket.accept();
                log("소켓 연결: " + socket);
                Session session = new Session(socket, socketManager);
                Thread thread = new Thread(session);
                thread.start();
            }
        } catch (IOException e) {
            log(e);
        }
    }

    private static class ShutdownHook implements Runnable {

        private final ServerSocket serverSocket;
        private final SocketManager socketManager;

        public ShutdownHook(ServerSocket serverSocket, SocketManager socketManager) {
            this.serverSocket = serverSocket;
            this.socketManager = socketManager;
        }

        @Override
        public void run() {
            log("showdown Hook 실행");
            try {
                socketManager.clear();
                serverSocket.close();
            } catch (Exception e) {
                log(e);
            }
            log("서버 종료");
        }
    }
}
