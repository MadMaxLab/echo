package io.github.madmaxlab.echocore.service;

import io.github.madmaxlab.echocore.dao.ContactDAO;
import io.github.madmaxlab.echocore.entity.Contact;
import io.github.madmaxlab.echocore.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
    private ContactDAO contactDAO;
    private UserService userService;

    @Autowired
    public ContactServiceImpl(ContactDAO contactDAO, UserService userService) {
        this.contactDAO = contactDAO;
        this.userService = userService;
    }

    @Override
    @Transactional
    public List<Contact> getContactList(String userLogin) {
        User user = userService.getUserByLogin(userLogin);
        List<Contact> result = contactDAO.getContacts(user);
        if (result == null) {
            return Collections.emptyList();
        }
        return result;
    }
}
