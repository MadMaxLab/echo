package io.github.madmaxlab.echocore.service;

import io.github.madmaxlab.echocore.dao.MessageDAO;
import io.github.madmaxlab.echocore.entity.Message;
import io.github.madmaxlab.echocore.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
}
