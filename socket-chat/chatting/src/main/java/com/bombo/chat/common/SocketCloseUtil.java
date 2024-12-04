package com.bombo.chat.common;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static com.bombo.chat.common.MyLogger.log;

public class SocketCloseUtil {

    public static void closeAll(Socket socket, InputStream inputStream, OutputStream outputStream) {
        close(inputStream);
        close(outputStream);
        close(socket);
    }

    public static void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Exception e) {
                log(e.getMessage());
            }
        }
    }

    public static void close(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (Exception e) {
                log(e.getMessage());
            }
        }
    }

    public static void close(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                log(e.getMessage());
            }
        }
    }
}
