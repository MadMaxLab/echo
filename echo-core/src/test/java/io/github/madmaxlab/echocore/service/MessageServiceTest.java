package io.github.madmaxlab.echocore.service;

import io.github.madmaxlab.echocore.entity.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MessageServiceTest {
    private final static String EXISTING_LOGIN = "mylogin";
    private final static int EXPECTING_MESSAGES_COUNT = 2;
    @Autowired
    private MessageService messageService;

    @Test
    @Sql("/reset-data.sql")
    void getUserMessages() {
        List<Message> messages = messageService.getUserMessages(EXISTING_LOGIN);

        assertThat(messages).isNotNull();
        assertThat(messages).isNotEmpty();
        assertThat(messages.size()).isEqualTo(EXPECTING_MESSAGES_COUNT);
    }
}