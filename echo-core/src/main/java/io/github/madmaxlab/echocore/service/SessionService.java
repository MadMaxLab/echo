package io.github.madmaxlab.echocore.service;

import org.springframework.web.socket.WebSocketSession;

public interface SessionService {
    void save(String userLogin, WebSocketSession session);
    void delete(WebSocketSession session);
    WebSocketSession get(String userLogin);
}
