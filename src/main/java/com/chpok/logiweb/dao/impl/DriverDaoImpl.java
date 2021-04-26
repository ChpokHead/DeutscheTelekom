package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.DriverDao;
import com.chpok.logiweb.dao.exception.DatabaseRuntimeException;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
@Transactional
public class DriverDaoImpl implements DriverDao {
    private static final String FIND_ALL_QUERY = "SELECT d FROM Driver d";

    @Autowired
    HibernateUtil hibernateUtil;

    @Override
    public void save(Driver entity) {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()) {
            session.beginTransaction();

            session.save(entity);

            session.getTransaction().commit();
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB driver saving exception", npe);
        }

    }

    @Override
    public Optional<Driver> findById(Long id) {
        try (Session session = Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()){
            session.beginTransaction();

            final Driver driver = session.get(Driver.class, id);

            Hibernate.initialize(driver.getCurrentTruck());

            session.getTransaction().commit();

            return Optional.of(driver);
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB find driver by id exception", npe);
        }
    }

    @Override
    public List<Driver> findAll() {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()) {
            session.beginTransaction();

            final List<Driver> allDrivers = session.createQuery(FIND_ALL_QUERY, Driver.class).getResultList();

            for (Driver driver : allDrivers) {
                Hibernate.initialize(driver.getCurrentTruck());
            }

            session.getTransaction().commit();

            return allDrivers;
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB getting all driver exception", npe);
        }

    }

    @Override
    public void update(Driver entity) {
        try(Session session =
                    Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()) {
            session.beginTransaction();

            session.update(entity);

            session.getTransaction().commit();
        }  catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB driver updating exception", npe);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()) {
            session.beginTransaction();

            final Driver deletingDriver = session.load(Driver.class, id);

            session.delete(deletingDriver);

            session.getTransaction().commit();
        }  catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB driver deleting exception", npe);
        }
    }

    @Override
    public void deleteAllByIds(Set<Long> ids) {

    }

    @Override
    public Optional<Driver> findByPersonalNumber(String personalNumber) {
        return Optional.empty();
    }
}
