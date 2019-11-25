package io.github.madmaxlab.echocore.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import javax.websocket.CloseReason;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

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
        client.getCurrentSession().getBasicRemote().sendText(TEST_MESSAGE);
        latch.await(); // Wait until server handle the message and send back an answer
        client.getCurrentSession().close(new CloseReason(
                CloseReason.CloseCodes.NORMAL_CLOSURE,
                "Test finished"));

        assertEquals(TEST_MESSAGE.toUpperCase(), client.getAnswer());

    }


}