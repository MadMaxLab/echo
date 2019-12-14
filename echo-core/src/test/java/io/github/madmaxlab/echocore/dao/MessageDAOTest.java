package io.github.madmaxlab.echocore.dao;

import io.github.madmaxlab.echocore.entity.Message;
import io.github.madmaxlab.echocore.entity.MessageType;
import io.github.madmaxlab.echocore.entity.User;
import io.github.madmaxlab.echocore.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MessageDAOTest {
    private final static UUID EXISTING_USER_ID = UUID.fromString("4be47ff1-96f0-41d4-a8c5-c85dde2512c2");
    private final static int EXPECTING_MESSAGES_COUNT = 2;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageDAO messageDAO;

    @Test
    @Sql("/reset-data.sql")
    void getUserMessages() {
        User user = userService.getUserById(EXISTING_USER_ID);
        List<Message> messages = messageDAO.getUserMessages(user);

        assertThat(messages).isNotNull();
        assertThat(messages).isNotEmpty();
        assertThat(messages.size()).isEqualTo(EXPECTING_MESSAGES_COUNT);
        Message actualMessage = messages.get(0);
        assertThat(actualMessage.getType()).isEqualTo(MessageType.TEXT);
    }
}