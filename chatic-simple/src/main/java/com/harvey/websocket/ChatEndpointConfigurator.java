package com.harvey.websocket;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;

/**
 * @author harvey
 */
public class ChatEndpointConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        // Store http session to the websocket session's config.
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        config.getUserProperties().put("HttpSession", httpSession);
    }
}
