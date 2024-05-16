package com.harvey.websocket;

import com.alibaba.fastjson2.JSON;
import com.harvey.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author harvey
 */
@Slf4j
@Component
@ServerEndpoint(value = "/chat", configurator = ChatEndpointConfigurator.class)
public class ChatEndpoint {
    private static final Map<String, Session> onlineUserMap = new ConcurrentHashMap<>();
    
    private static HttpSession httpSession;
    
    @OnOpen
    public void onOpen(Session userSession, EndpointConfig config) {
        httpSession = (HttpSession) config.getUserProperties().get("HttpSession");
        
        String srcUsername = (String) httpSession.getAttribute("username");
        
        // Store user's websocket session after the client establishes a connection.
        onlineUserMap.put(srcUsername, userSession);
        
        // Broadcast a message to all users that a new user has logged in.
        Message message = new Message();
        message.setSrcUsername(srcUsername);
        message.setContent(String.format("%s logged in", srcUsername));
        sendMessageToAllUsers(message);
    }
    
    @OnClose
    public void onClose() {
        String srcUsername = (String) httpSession.getAttribute("username");
        
        // Remove user's websocket session after the client closes the connection.
        onlineUserMap.remove(srcUsername);
        
        // Broadcast a message to all users that a user has logged out.
        Message message = new Message();
        message.setContent(String.format("%s logged out", srcUsername));
        message.setSrcUsername(srcUsername);
        sendMessageToAllUsers(message);
    }
    
    @OnMessage
    public void onMessage(String messageJson) {
        sendMessageToTarUser(messageJson);
    }
    
    /**
     * Send a message to the target user.
     */
    private void sendMessageToTarUser(String messageJson) {
        Message message = JSON.parseObject(messageJson, Message.class);
        sendMessageToTarUser(message);
    }
    
    
    /**
     * Send a message to the target user.
     */
    private void sendMessageToTarUser(Message message) {
        String tarUsername = message.getTarUsername();
        Session tarUserSession = onlineUserMap.get(tarUsername);
        String messageJson = JSON.toJSONString(message);
        try {
            tarUserSession.getBasicRemote().sendText(messageJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Broadcast a message to all users.
     */
    private void sendMessageToAllUsers(Message message) {
        for (Map.Entry<String, Session> entry : onlineUserMap.entrySet()) {
            String tarUsername = entry.getKey();
            message.setTarUsername(tarUsername);
            Session tarUserSession = entry.getValue();
            String messageJson = JSON.toJSONString(message);
            try {
                tarUserSession.getBasicRemote().sendText(messageJson);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
