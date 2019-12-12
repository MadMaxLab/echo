package io.github.madmaxlab.echocore.service;

import io.github.madmaxlab.echocore.dao.UserDAO;
import io.github.madmaxlab.echocore.entity.User;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

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
        if (userDAO.getCountByLogin(user.getLogin()) != 0) {
            throw new RuntimeException(
                    String.format("The user with login %s already exist. Please try another login" , user.getLogin()));
        }
        userDAO.save(user);
        log.info(String.format("A new user was created! User id : %s, User login : %s", user.getId(), user.getLogin()));
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
        User user = userDAO.getById(id);
        if (user == null){
            throw new RuntimeException(String.format("Can't get user by id %s, user does not exist.", id));
        }
        return  user;
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        userDAO.deleteById(id);
    }

    @Override
    @Transactional
    public boolean authenticateUser(String login, String password) {
        User user = userDAO.getUserByLogin(login);
        if (user == null) {
            log.info("User %s does not exist. Authentication failed.", login);
            return false;
        }
        if (!user.getLogin().equals(password)) {
            log.info("User %s is send a wrong password. Authentication failed.", login);
            log.debug("The password is : '%s'", password);
            return false;
        }
        return true;
    }
}
