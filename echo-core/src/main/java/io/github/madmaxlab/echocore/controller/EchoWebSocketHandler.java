package io.github.madmaxlab.echocore.controller;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.Output;
import io.github.madmaxlab.echocore.DTO.MessageDTO;
import io.github.madmaxlab.echocore.DTO.MessageType;
import io.github.madmaxlab.echocore.entity.Contact;
import io.github.madmaxlab.echocore.entity.Message;
import io.github.madmaxlab.echocore.entity.User;
import io.github.madmaxlab.echocore.service.ContactService;
import io.github.madmaxlab.echocore.service.MessageService;
import io.github.madmaxlab.echocore.service.SessionService;
import io.github.madmaxlab.echocore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

@Slf4j
public class EchoWebSocketHandler extends BinaryWebSocketHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SessionService sessionService;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("New connection was opened. Session id: " + session.getId());
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        log.info("Server received binary message. Message size : " + message.getPayloadLength());
        Kryo kryo = new Kryo();
        kryo.register(MessageDTO.class);
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        try(ByteBufferInput bbi = new ByteBufferInput(message.getPayload())) {
            MessageDTO receivedMessage = kryo.readObject(bbi, MessageDTO.class);

            switch (receivedMessage.getMessageType()) {
                case GREETINGS:
                    answerGreetings(session, receivedMessage.getText(), kryo);
                    break;
                case REGISTRATION:
                    User newUser = convertMessageToNewUser(receivedMessage);
                    userService.createUser(newUser);
                    sendOK(session, kryo);
                    break;
                case AUTHENTICATION:
                    if (!userService.authenticateUser(receivedMessage.getFrom(), receivedMessage.getText())) {
                        sendError(session, kryo, "Authentication failed. See server logs for details.");
                        break;
                    }
                    List<Contact> contacts = contactService.getContactList(receivedMessage.getFrom());
                    for (Contact contact :
                            contacts) {
                        sendContact(session, kryo, contact);
                    }
                    List<Message> messages = messageService.getUserMessages(receivedMessage.getFrom());
                    for (Message m :
                            messages) {
                        sendTextMessage(session, kryo, m);
                    }
                    sessionService.save(receivedMessage.getFrom(), session);
                    sendOK(session, kryo);
                    break;
                case TEXT:
                    String sender = receivedMessage.getFrom();
                    String receiver = receivedMessage.getTo();
                    String text = receivedMessage.getText();
                    messageService.saveTextMessage(sender, receiver, text);
                    WebSocketSession receiverSession = sessionService.get(receivedMessage.getTo());
                    if (receiverSession != null) {
                        sendTextMessage(receiverSession, kryo, sender, receiver, text);
                    }
                    break;
            }
        }
    }

    private void answerGreetings(WebSocketSession session, String text, Kryo kryo) throws IOException {
        MessageDTO answer = MessageDTO.builder()
                .id(UUID.randomUUID())
                .messageType(MessageType.GREETINGS)
                .text(text.toUpperCase())
                .build();
        sendMessage(session, kryo, answer);

    }

    private void sendOK(WebSocketSession session, Kryo kryo) throws IOException {
        MessageDTO answer = MessageDTO.builder()
                .id(UUID.randomUUID())
                .messageType(MessageType.CONFIRM)
                .build();
        sendMessage(session, kryo, answer);
    }

    private void sendError(WebSocketSession session, Kryo kryo, String errorMessage) throws IOException {
        MessageDTO answer = MessageDTO.builder()
                .id(UUID.randomUUID())
                .messageType(MessageType.ERROR)
                .text(errorMessage)
                .build();
        sendMessage(session, kryo, answer);
    }

    private void sendMessage(WebSocketSession session, Kryo kryo, MessageDTO answer) throws IOException {
        try (Output output = new Output(new ByteArrayOutputStream())) {
            kryo.writeObject(output, answer);
            output.flush();
            ByteBuffer bb = ByteBuffer.wrap(output.getBuffer());
            session.sendMessage(new BinaryMessage(bb));
        }
    }

    private void sendContact(WebSocketSession session, Kryo kryo, Contact contact) throws IOException {
        MessageDTO answer = MessageDTO.builder()
                .id(UUID.randomUUID())
                .messageType(MessageType.CONTACT)
                .contact(contact)
                .build();
        sendMessage(session, kryo, answer);
    }

    private void sendTextMessage(WebSocketSession session, Kryo kryo, Message message) throws IOException {
        sendTextMessage(
                session,
                kryo,
                message.getSender().getLogin(),
                message.getReceiver().getLogin(),
                message.getText()
        );
    }

    private void sendTextMessage(WebSocketSession session, Kryo kryo, String sender, String receiver, String text)
            throws IOException {
        MessageDTO answer = MessageDTO.builder()
                .id(UUID.randomUUID())
                .messageType(MessageType.TEXT)
                .from(sender)
                .to(receiver)
                .text(text)
                .build();
        sendMessage(session, kryo, answer);
    }

    private User convertMessageToNewUser(MessageDTO messageDTO) {
        return User.builder()
                .name(messageDTO.getName())
                .login(messageDTO.getFrom())
                .password(messageDTO.getText())
                .build();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        log.info("Connection closed. Connection id: " + session.getId() + " Close status: " + status);
        sessionService.delete(session);

    }
}
