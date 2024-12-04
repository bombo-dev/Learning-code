package com.bombo.chat.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyLogger {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static void log(Object obj) {
        String formattedLocalDateTime = LocalDateTime.now().format(formatter);
        System.out.printf("%s: [%9s] %s\n%n", formattedLocalDateTime, Thread.currentThread().getName(), obj);
    }
}
