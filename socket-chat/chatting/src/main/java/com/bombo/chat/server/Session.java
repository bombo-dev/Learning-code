package com.bombo.chat.server;

import com.bombo.chat.common.SocketCloseUtil;
import com.bombo.chat.server.command.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static com.bombo.chat.common.MyLogger.log;

public class Session implements Runnable {

    private String userName;
    private final CommandFactory commandFactory;
    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    private final SocketManager socketManager;
    private State state = State.ALIVE;

    public Session(Socket socket, SocketManager socketManager) throws IOException {
        this.commandFactory = new CommandFactory();
        this.socket = socket;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.socketManager = socketManager;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // 클라이언트로부터 문자 받기
                String receivedRawCommand = input.readUTF();
                log("client -> server : " + receivedRawCommand);

                try {
                    Command command = commandFactory.create(receivedRawCommand);
                    CommandExecutor commandExecutor = new CommandExecutor(command, this, socketManager);
                    commandExecutor.run();
                } catch (IllegalArgumentException e) {
                    output.writeUTF(e.getMessage());
                }
            }
        } catch (IOException e) {
            log(e);
        } finally {
            socketManager.remove(this);
            close();
        }
    }

    public synchronized void close() {
        if (state.isDead()) {
            return;
        }

        SocketCloseUtil.closeAll(socket, input, output);
        state = State.DEAD;
        log("연결 종료: " + socket + " isClosed: " + socket.isClosed());
    }

    public void setUserName(String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("userName is Not Null");
        }

        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public DataOutputStream getOutput() {
        return output;
    }

    private enum State {
        ALIVE, DEAD;

        public boolean isDead() {
            return this == DEAD;
        }
    }
}
