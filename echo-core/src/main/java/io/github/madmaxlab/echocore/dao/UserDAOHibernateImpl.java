package io.github.madmaxlab.echocore.dao;

import io.github.madmaxlab.echocore.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.UUID;

@Repository
public class UserDAOHibernateImpl implements UserDAO {

    private final EntityManager entityManager;

    @Autowired
    public UserDAOHibernateImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public User getById(UUID id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(User.class, id);
    }

    @Override
    @Transactional
    public void save(User user) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(user);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        Session session = entityManager.unwrap(Session.class);

        Query query = session.createQuery("delete from User u where u.id=:user_id");

        query.setParameter("user_id", id);
        query.executeUpdate();
    }

    @Override
    @Transactional
    public long getCountByLogin(String login) {
        Session session = entityManager.unwrap(Session.class);
        Query<Long> query = session.createQuery("select count(u.id) from User u where u.login=:login", Long.class);
        query.setParameter("login", login);
        return query.getSingleResult();
    }

    @Override
    public User getUserByLogin(String login) {
        Session session = entityManager.unwrap(Session.class);
        Query<User> query = session.createQuery("SELECT u FROM User u where u.login=:login", User.class);
        return query.getSingleResult();
    }
}
