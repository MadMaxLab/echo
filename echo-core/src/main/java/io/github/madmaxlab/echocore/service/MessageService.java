package io.github.madmaxlab.echocore.service;

import io.github.madmaxlab.echocore.entity.Message;

import java.util.List;

public interface MessageService {
    List<Message> getUserMessages(String userLogin);
}
