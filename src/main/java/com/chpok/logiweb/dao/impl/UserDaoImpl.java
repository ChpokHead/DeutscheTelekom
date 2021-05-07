package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.UserDao;
import com.chpok.logiweb.dao.exception.DatabaseRuntimeException;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.User;
import com.chpok.logiweb.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserDaoImpl implements UserDao {
    private static final String FIND_BY_USERNAME_QUERY = "SELECT u FROM User u WHERE u.username = :username";
    private static final String FIND_BY_ID_QUERY = "SELECT u FROM User u WHERE u.id = :id";

    private final HibernateUtil hibernateUtil;

    public UserDaoImpl(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public void save(User entity) {

    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()){
            session.beginTransaction();

            Query<User> query = session.createQuery(FIND_BY_ID_QUERY, User.class);

            query.setParameter("id", id);

            Optional<User> user = query.uniqueResultOptional();

            Hibernate.initialize(user.get().getDriver());

            session.getTransaction().commit();

            return user;
        } catch (NullPointerException | NoSuchElementException npe) {
            throw new DatabaseRuntimeException("DB getting user by id exception", npe);
        }
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public void update(User entity) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteAllByIds(Set<Long> ids) {

    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()){
            session.beginTransaction();

            Query<User> query = session.createQuery(FIND_BY_USERNAME_QUERY, User.class);

            query.setParameter("username", username);

            Optional<User> user = query.uniqueResultOptional();

            session.getTransaction().commit();

            return user;
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB getting user by username exception", npe);
        }
    }
}
