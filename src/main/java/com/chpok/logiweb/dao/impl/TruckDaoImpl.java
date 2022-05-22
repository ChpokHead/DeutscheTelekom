package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.TruckDao;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.Truck;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TruckDaoImpl implements TruckDao{
    private static final String FIND_ALL_QUERY = "SELECT t FROM Truck t";
    private static final String FIND_ALL_BY_CURRENT_LOCATION_ID_QUERY = "SELECT t FROM Truck t where t.location.id = :id";
    private static final String FIND_BY_REG_NUMBER_QUERY = "SELECT t FROM Truck t where t.regNumber = :regNumber";

    private final SessionFactory sessionFactory;

    public TruckDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long save(Truck entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.save(entity);

            session.getTransaction().commit();

            return entity.getId();
        }
    }

    @Override
    public Optional<Truck> findById(Long id) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final Truck truck = session.get(Truck.class, id);

            if (truck != null) {
                for (Driver driver : truck.getCurrentDrivers()) {
                    Hibernate.initialize(driver);
                }
            }

            session.getTransaction().commit();

            return Optional.ofNullable(truck);
        }
    }

    @Override
    public List<Truck> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            final List<Truck> allTrucks = session.createQuery(FIND_ALL_QUERY, Truck.class).getResultList();

            session.getTransaction().commit();

            return allTrucks;
        }
    }

    @Override
    public void update(Truck entity) {
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

            final Truck deletingTruck = session.get(Truck.class, id);

            session.delete(deletingTruck);

            session.getTransaction().commit();
        }
    }

    @Override
    public List<Truck> findByCurrentLocationId(Long currentLocationId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Query<Truck> query = session.createQuery(FIND_ALL_BY_CURRENT_LOCATION_ID_QUERY, Truck.class);

            query.setParameter("id", currentLocationId);

            final List<Truck> trucksAtSpecifiedLocation = query.getResultList();

            session.getTransaction().commit();

            return trucksAtSpecifiedLocation;
        }
    }

    @Override
    public Optional<Truck> findByRegNumber(String regNumber) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final Truck truck = session.createQuery(FIND_BY_REG_NUMBER_QUERY, Truck.class)
                    .setParameter("regNumber", regNumber).getSingleResult();

            session.getTransaction().commit();

            return Optional.ofNullable(truck);
        }
    }
}
