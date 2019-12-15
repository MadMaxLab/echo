package io.github.madmaxlab.echocore.service;

import io.github.madmaxlab.echocore.dao.MessageDAO;
import io.github.madmaxlab.echocore.entity.Message;
import io.github.madmaxlab.echocore.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService{

    private UserService userService;
    private MessageDAO messageDAO;

    @Autowired
    public MessageServiceImpl(UserService userService, MessageDAO messageDAO) {
        this.userService = userService;
        this.messageDAO = messageDAO;
    }

    @Override
    public List<Message> getUserMessages(String userLogin) {
        User user = userService.getUserByLogin(userLogin);
        List<Message> messages = messageDAO.getUserMessages(user);
        if (messages == null) {
            return Collections.emptyList();
        }
        return messages;
    }

    @Override
    public void saveTextMessage(String from, String to, String text) {
        Message message = Message.builder()
                .id(UUID.randomUUID())
                .created(new Date())
                .isDelivered(false)
                .sender(userService.getUserByLogin(from))
                .receiver(userService.getUserByLogin(to))
                .text(text)
                .build();
        messageDAO.save(message);
    }
}
