package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.DriverDao;
import com.chpok.logiweb.model.Driver;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class DriverDaoImpl implements DriverDao {
    private static final String FIND_ALL_QUERY = "SELECT d FROM Driver d";
    private static final String FIND_ALL_BY_TRUCK_ID_QUERY = "SELECT d FROM Driver d WHERE d.currentTruck.id = :id";
    private static final String FIND_ALL_WITHOUT_CURRENT_ORDER_QUERY = "SELECT d FROM Driver d WHERE d.currentOrder is null";

    private final SessionFactory sessionFactory;

    public DriverDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void save(Driver entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.save(entity);

            session.getTransaction().commit();
        }
    }

    @Override
    public Optional<Driver> findById(Long id) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final Driver driver = session.get(Driver.class, id);

            if (driver != null) {
                Hibernate.initialize(driver.getCurrentTruck());
            }

            session.getTransaction().commit();

            return Optional.ofNullable(driver);
        }
    }

    @Override
    public List<Driver> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            final List<Driver> allDrivers = session.createQuery(FIND_ALL_QUERY, Driver.class).getResultList();

            if (!allDrivers.isEmpty()) {
                for (Driver driver : allDrivers) {
                    Hibernate.initialize(driver.getCurrentTruck());
                }
            }

            session.getTransaction().commit();

            return allDrivers;
        }
    }

    @Override
    public void update(Driver entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.update(entity);

            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            final Driver deletingDriver = session.get(Driver.class, id);

            session.delete(deletingDriver);

            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteAllByIds(Set<Long> ids) {

    }

    @Override
    public List<Driver> findByCurrentTruckId(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Query<Driver> query = session.createQuery(FIND_ALL_BY_TRUCK_ID_QUERY, Driver.class);

            query.setParameter("id", id);

            List<Driver> drivers = query.getResultList();

            session.getTransaction().commit();

            return drivers;
        }
    }

    @Override
    public List<Driver> findAllDriversWithoutCurrentOrder() {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            Query<Driver> query = session.createQuery(FIND_ALL_WITHOUT_CURRENT_ORDER_QUERY, Driver.class);

            List<Driver> foundDrivers = query.getResultList();

            session.getTransaction().commit();

            return foundDrivers;
        }
    }
}
