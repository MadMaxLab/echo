package io.github.madmaxlab.echocore.controller;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import io.github.madmaxlab.echocore.DTO.MessageDTO;
import io.github.madmaxlab.echocore.DTO.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import javax.websocket.CloseReason;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class WebSocketControllerTest {

    private static final String TEST_MESSAGE = "Java";
    private static final String HOST_URL = "ws://localhost:";
    private static final String WEBSOCKET_CONTEXT_ROOT = "/api/websocket";

    private Kryo kryo;
    private CountDownLatch latch;
    private WebSocketClient client;

    @BeforeEach
    void init() {
        kryo = new Kryo();
        kryo.register(MessageDTO.class);
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        latch = new CountDownLatch(1);
        client = new WebSocketClient(HOST_URL + port + WEBSOCKET_CONTEXT_ROOT, latch);
    }

    @LocalServerPort
    int port;

    @Test
    public void simpleTest() throws Exception{
        MessageDTO message  = MessageDTO.builder()
                .id(UUID.randomUUID())
                .messageType(MessageType.GREETINGS)
                .text(TEST_MESSAGE)
                .build();
        sendMessage(message);
        latch.await(); // Wait until server handle the message and send back an answer
        closeSession();

        assertThat(TEST_MESSAGE.toUpperCase()).isEqualTo(client.getAnswer());
    }



    @Test
    @Sql("/reset-data.sql")
    void registrationTest() throws IOException, InterruptedException{
        MessageDTO messageDTO = MessageDTO.builder()
                .messageType(MessageType.REGISTRATION)
                .from("Test New User Login")
                .name("Test New User Name")
                .text("Password")
                .build();
        sendMessage(messageDTO);
        latch.await();
        closeSession();

        assertThat(client.getAnswer()).isEqualTo("OK");
    }

    private void sendMessage(MessageDTO message) throws IOException {
        try(Output output = new Output(new ByteArrayOutputStream())) {
            kryo.writeObject(output, message);
            output.flush();
            ByteBuffer bb = ByteBuffer.wrap(output.getBuffer());
            client.getCurrentSession().getBasicRemote().sendBinary(bb);
        }
    }

    private void closeSession() throws IOException {
        client.getCurrentSession().close(new CloseReason(
                CloseReason.CloseCodes.NORMAL_CLOSURE,
                "Test finished"));
    }
}