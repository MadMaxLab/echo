package io.github.madmaxlab.echocore.service;

import io.github.madmaxlab.echocore.entity.User;

import java.util.UUID;

public interface UserService {

    void createUser(User user);

    void updateUser(User user);

    User getUserById(UUID id);

    void deleteUser(UUID id);
}
