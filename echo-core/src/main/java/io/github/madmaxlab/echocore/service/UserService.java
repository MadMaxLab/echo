package io.github.madmaxlab.echocore.service;

import io.github.madmaxlab.echocore.entity.User;

import java.util.UUID;

public interface UserService {
    void createUser(User user);

    void updateUser(User user);

    User getUserById(UUID id);

    User getUserByLogin(String login);

    void deleteUser(UUID id);

    boolean authenticateUser(String login, String password);
}
