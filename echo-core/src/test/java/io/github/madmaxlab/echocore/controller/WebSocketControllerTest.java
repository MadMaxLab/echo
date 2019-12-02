package io.github.madmaxlab.echocore.controller;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Output;
import io.github.madmaxlab.echocore.DTO.MessageDTO;
import io.github.madmaxlab.echocore.DTO.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import javax.websocket.CloseReason;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class WebSocketControllerTest {

    private static final String TEST_MESSAGE = "Java";
    private static final String HOST_URL = "ws://localhost:";
    private static final String WEBSOCKET_CONTEXT_ROOT = "/api/websocket";

    @LocalServerPort
    int port;

    @Test
    public void simpleTest() throws Exception{

        CountDownLatch latch = new CountDownLatch(1);
        WebSocketClient client = new WebSocketClient(HOST_URL + port + WEBSOCKET_CONTEXT_ROOT, latch);

        Kryo kryo = new Kryo();
        kryo.register(MessageDTO.class);
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        try(Output output = new Output(new ByteArrayOutputStream())) {
            MessageDTO message  = MessageDTO.builder()
                    .id(UUID.randomUUID())
                    .messageType(MessageType.GREETINGS)
                    .text(TEST_MESSAGE)
                    .build();

            kryo.writeObject(output, message);
            output.flush();
            ByteBuffer bb = ByteBuffer.wrap(output.getBuffer());
            client.getCurrentSession().getBasicRemote().sendBinary(bb);
        }
        latch.await(); // Wait until server handle the message and send back an answer
        client.getCurrentSession().close(new CloseReason(
                CloseReason.CloseCodes.NORMAL_CLOSURE,
                "Test finished"));

        assertEquals(TEST_MESSAGE.toUpperCase(), client.getAnswer());
    }
}