package com.example.testspringboot.webSocket;

import javax.websocket.*;
import java.net.URI;
import java.util.Scanner;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/2/13 10:35
 */
@ClientEndpoint
public class WebSocketClient {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to server");
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Session closed: " + closeReason);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    public static void main(String[] args) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String uri = "ws://localhost:8081/notice/1";
        System.out.println("Connecting to " + uri);
        try {
            Session session = container.connectToServer(WebSocketClient.class, URI.create(uri));

            // 使用 Scanner 从键盘读取输入
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter messages to send to the server (type 'exit' to quit):");

            while (true) {
                String input = scanner.nextLine();
                if ("exit".equalsIgnoreCase(input)) {
                    break;
                }
                session.getBasicRemote().sendText(input);
            }
            // 关闭会话和扫描器
            session.close();
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
