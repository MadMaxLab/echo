package io.github.madmaxlab.echocore.dao;

import io.github.madmaxlab.echocore.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserDAOHibernateImplTest {
    private final UUID USER1_ID = UUID.fromString("3e87cf37-d92d-4201-9d09-b4330c268bd0");
    private final String USER1_NAME = "Test User 1";
    private final String USER1_LOGIN = "testlogin1";
    private final String USER1_PASSWORD = "password";
    private final String USER_TO_SAVE_NAME = "Test User 3";
    private final String USER_TO_SAVE_LOGIN = "testlogin3";
    private final String USER_TO_SAVE_PASSWORD = "password";


    @Autowired
    UserDAO userDAO;

    @Test
    @Sql("/reset-data.sql")
    void getById() {
        User actualUser = userDAO.getById(USER1_ID);
        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getId()).isEqualTo(USER1_ID);
        assertThat(actualUser.getName()).isEqualTo(USER1_NAME);
        assertThat(actualUser.getLogin()).isEqualTo(USER1_LOGIN);
        assertThat(actualUser.getPassword()).isEqualTo(USER1_PASSWORD);
    }

    @Test
    @Sql("/reset-data.sql")
    void save() {
        User newTestUser  = User.builder()
                .name(USER_TO_SAVE_NAME)
                .login(USER_TO_SAVE_LOGIN)
                .password(USER_TO_SAVE_PASSWORD)
                .build();
        userDAO.save(newTestUser);
        assertThat(newTestUser.getId()).isNotNull();
        User actualUser = userDAO.getById(newTestUser.getId());
        assertThat(actualUser).isEqualTo(newTestUser);
    }

    @Test
    @Sql("/reset-data.sql")
    void deleteById() {
        userDAO.deleteById(USER1_ID);
        assertThat(userDAO.getById(USER1_ID)).isNull();
    }

    @Test
    @Sql("/reset-data.sql")
    void getCountByLogin() {
        assertThat(userDAO.getCountByLogin("")).isZero();
    }
}