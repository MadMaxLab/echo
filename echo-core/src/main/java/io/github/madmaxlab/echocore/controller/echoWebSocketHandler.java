package io.github.madmaxlab.echocore.controller;



import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.Output;
import io.github.madmaxlab.echocore.DTO.MessageDTO;
import io.github.madmaxlab.echocore.DTO.MessageType;
import lombok.extern.slf4j.Slf4j;

import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import com.esotericsoftware.kryo.Kryo;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

@Slf4j
public class echoWebSocketHandler extends BinaryWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("New connection was opened. Session id: " + session.getId());
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        Kryo kryo = new Kryo();
        kryo.register(MessageDTO.class);
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        log.info("Server received binary message. Message size : " + message.getPayloadLength());
        try(ByteBufferInput bbi = new ByteBufferInput(message.getPayload())) {
            MessageDTO receivedMessage = kryo.readObject(bbi, MessageDTO.class);
            try (Output output = new Output(new ByteArrayOutputStream())) {
                MessageDTO answer = MessageDTO.builder()
                        .id(UUID.randomUUID())
                        .messageType(MessageType.GREETINGS)
                        .text(receivedMessage.getText().toUpperCase())
                        .build();
                kryo.writeObject(output, answer);
                output.flush();
                ByteBuffer bb = ByteBuffer.wrap(output.getBuffer());
                session.sendMessage(new BinaryMessage(bb));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        log.info("Connection closed. Connection id: " + session.getId() + " Close status: " + status);

    }
}
