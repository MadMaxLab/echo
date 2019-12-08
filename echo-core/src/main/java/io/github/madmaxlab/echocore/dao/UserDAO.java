package io.github.madmaxlab.echocore.dao;


import io.github.madmaxlab.echocore.entity.User;

import java.util.UUID;

public interface UserDAO {

    User getById(UUID id);

    void save(User user);

    void deleteById(UUID id);

    long getCountByLogin(String login);

}
