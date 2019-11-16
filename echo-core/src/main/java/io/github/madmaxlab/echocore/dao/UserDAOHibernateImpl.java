package io.github.madmaxlab.echocore.dao;

import io.github.madmaxlab.echocore.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.UUID;

@Repository
public class UserDAOHibernateImpl implements UserDAO {

    @Autowired
    EntityManager entityManager;

    @Override
    public User getById(UUID id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(User.class, id);
    }

    @Override
    public void save(User user) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(user);
    }

    @Override
    public void deleteById(UUID id) {
        Session session = entityManager.unwrap(Session.class);

        Query<User> query = session.createQuery("delete from User where id=:user_id", User.class);

        query.setParameter("user_id", id);
        query.executeUpdate();

    }


}
