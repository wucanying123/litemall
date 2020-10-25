package org.linlinjava.litemall.admin.config.webSocket;//package com.xinda.webSocket;

import java.io.IOException;

public class WebSocketUtil {

    public static void sendInfo(String content) {
        try {
            CustomWebSocket.sendInfo(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}