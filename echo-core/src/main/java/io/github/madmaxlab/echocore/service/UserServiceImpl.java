package io.github.madmaxlab.echocore.service;

import io.github.madmaxlab.echocore.dao.UserDAO;
import io.github.madmaxlab.echocore.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {


    private UserDAO userDAO;

    public UserServiceImpl(@Autowired UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public void createUser(User user) {
        if (user.getId() != null) {
            //TODO: create an  exception class
            throw new RuntimeException("It's not acceptable to create a user with UUID generated on the client side");
        }
        userDAO.save(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        if (user.getId() == null) {
            //TODO: create an  exception class
            throw new RuntimeException("It's not acceptable to update a user without UUID. " +
                    "To create new user please use create method");
        }

        userDAO.save(user);
    }

    @Override
    @Transactional
    public User getUserById(UUID id) {
        return userDAO.getById(id);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        userDAO.deleteById(id);
    }
}
