package io.github.madmaxlab.echocore.controller;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import io.github.madmaxlab.echocore.DTO.MessageDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.objenesis.strategy.StdInstantiatorStrategy;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
@Setter
@Getter
@Slf4j
public class WebSocketClient {

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
        latch.countDown();
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("Client receive a new message:  " + message);
        answer = message;
        latch.countDown();
    }

    @OnMessage
    public void onBinaryMessage(ByteBuffer message, Session session) {
        log.info("Client receive a new binary message");
        Kryo kryo = new Kryo();
        kryo.register(MessageDTO.class);
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        try (ByteBufferInput bbi = new ByteBufferInput(message)) {
            answer = kryo.readObject(bbi, MessageDTO.class).getText();
            log.info("The answer is : " + answer);
        }
        latch.countDown();
    }
}
