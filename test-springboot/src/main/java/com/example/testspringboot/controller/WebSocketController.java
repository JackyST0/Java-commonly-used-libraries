package com.example.testspringboot.controller;

import com.example.testspringboot.webSocket.WebSocketServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/2/10 15:11
 */
@RestController
@RequestMapping("/webSocket")
public class WebSocketController {

    @GetMapping("/test")
    public void test() {
        WebSocketServer.sendMessage("你好，WebSocket");
    }
}
