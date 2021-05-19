package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.UserDao;
import com.chpok.logiweb.model.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserDaoImpl implements UserDao {
    private static final String FIND_BY_USERNAME_QUERY = "SELECT u FROM User u WHERE u.username = :username";

    private final SessionFactory sessionFactory;

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(User entity) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            session.save(entity);

            session.getTransaction().commit();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final User foundUser = session.get(User.class, id);

            if (foundUser != null) {
                Hibernate.initialize(foundUser.getDriver());

                Hibernate.initialize(foundUser.getEmployee());
            }

            session.getTransaction().commit();

            return Optional.ofNullable(foundUser);
        }
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            Query<User> query = session.createQuery(FIND_BY_USERNAME_QUERY, User.class);

            query.setParameter("username", username);

            final Optional<User> foundUser = query.uniqueResultOptional();

            session.getTransaction().commit();

            return foundUser;
        }
    }

}
