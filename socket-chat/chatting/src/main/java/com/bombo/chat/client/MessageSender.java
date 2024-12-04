package com.bombo.chat.client;

import com.bombo.chat.common.SocketCloseUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static com.bombo.chat.common.MyLogger.log;

public class MessageSender implements Runnable {

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private Scanner sc = new Scanner(System.in);

    public MessageSender(Socket socket, DataInputStream input, DataOutputStream output) {
        this.socket = socket;
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    String message = sc.nextLine();
                    log("client -> server: " + message);
                    output.writeUTF(message);

                    if (message.equals("exit")) {
                        break;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            log(e);
        } finally {
            SocketCloseUtil.close(socket);
        }
    }
}
