package io.github.madmaxlab.echocore.service;

import io.github.madmaxlab.echocore.entity.Contact;
import io.github.madmaxlab.echocore.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class ContactServiceImplTest {

    private final static String CORRECT_USER_LOGIN = "mylogin";
    @Autowired
    UserService userService;
    @Autowired
    ContactService contactService;


    @Test
    @Sql("/reset-data.sql")
    void getContactList() {
        User user = userService.getUserByLogin(CORRECT_USER_LOGIN);
        List<Contact> contacts = contactService.getContactList(CORRECT_USER_LOGIN);
        assertThat(contacts).isNotNull();
        assertThat(contacts).isNotEmpty();
        contacts.forEach(contact ->
                assertThat(contact.getAccepter().equals(user) || contact.getInitiator().equals(user))
                        .isTrue()
        );
    }

    @Test
    @Sql("/reset-data.sql")
    void getEmptyContactList() {
        try {
            List<Contact> contacts = contactService.getContactList("");
        }
        catch (EmptyResultDataAccessException ex) {
         assertThat(ex).hasMessage("No entity found for query; nested exception is javax.persistence.NoResultException: No entity found for query");
        }
    }
}