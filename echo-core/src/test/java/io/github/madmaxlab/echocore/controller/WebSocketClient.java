package io.github.madmaxlab.echocore.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

@ClientEndpoint
@Setter
@Getter
@Slf4j
public class WebSocketClient {

    private WebSocketControllerTest webSocketControllerTest;
    private String answer = null;
    private Session currentSession;
    private CountDownLatch latch;

    public WebSocketClient(String url, CountDownLatch latch) {
        try {
            this.latch = latch;
            WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
            webSocketContainer.connectToServer(
                    this,
                    new URI(url));

        } catch (URISyntaxException | DeploymentException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("New client session is open. Session id: " + session.getId());
        currentSession = session;
    }

    @OnClose
    public void onClose(Session session) {
        log.info("Client session is close. Session id: " + session.getId());
    }

    @OnError
    public void onError(Throwable error) {
        log.error("Catch an error from WebSocket Server", error);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("Client receive a new message:  " + message);
        answer = message;
        latch.countDown();
    }
}
