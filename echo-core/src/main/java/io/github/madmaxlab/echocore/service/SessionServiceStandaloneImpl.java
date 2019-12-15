package io.github.madmaxlab.echocore.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
This is simple in memory version for session storing.
It can be used if echo-core service installed as standalone server.

 */
@Service
public class SessionServiceStandaloneImpl implements SessionService {

    private final Map<String, WebSocketSession> sessionRepository = new ConcurrentHashMap<>();

    @Override
    public void save(String userLogin, WebSocketSession session) {
        sessionRepository.put(userLogin, session);
    }

    @Override
    public void delete(WebSocketSession session) {
        sessionRepository.values().remove(session);
    }

    @Override
    public WebSocketSession get(String userLogin) {
        return sessionRepository.get(userLogin);
    }
}
