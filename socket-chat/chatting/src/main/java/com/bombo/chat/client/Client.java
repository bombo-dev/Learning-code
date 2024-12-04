package com.bombo.chat.client;

import com.bombo.chat.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static com.bombo.chat.common.MyLogger.log;

public class Client {

    public static void main(String[] args) {
        log("Client 시작");

        try (
                Socket socket = new Socket("localhost", Server.PORT);
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                DataInputStream input = new DataInputStream(socket.getInputStream());
        ) {
            log("소켓 연결 완료 " + socket);

            MessageSender messageSender = new MessageSender(socket, input, output);
            Thread sendThread = new Thread(messageSender);
            sendThread.start();

            // 클라이언트 종료 처리 추가
            while (true) {
                String receivedText = input.readUTF();
                log("client <- server: " + receivedText);
            }
        } catch (IOException e) {
            log(e);
        }
    }
}
