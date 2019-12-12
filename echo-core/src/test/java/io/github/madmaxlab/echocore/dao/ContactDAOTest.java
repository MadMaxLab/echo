package io.github.madmaxlab.echocore.dao;


import io.github.madmaxlab.echocore.entity.Contact;
import io.github.madmaxlab.echocore.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ContactDAOTest {

    @Autowired
    UserDAO userDAO;

    @Autowired
    ContactDAO contactDAO;

    @Test
    @Sql("/reset-data.sql")
    protected void getContact() {

        User user = userDAO.getById(UUID.fromString("4be47ff1-96f0-41d4-a8c5-c85dde2512c2"));

        List<Contact> actualContacts = contactDAO.getContacts(user);
        assertThat(actualContacts).isNotNull();
        actualContacts.forEach(contact ->
            assertThat(contact.getAccepter().equals(user) || contact.getInitiator().equals(user))
                    .isTrue()
        );
    }
}