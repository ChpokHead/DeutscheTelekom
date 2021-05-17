package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.TruckDao;
import com.chpok.logiweb.dao.exception.DatabaseRuntimeException;
import com.chpok.logiweb.model.Truck;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
public class TruckDaoImpl implements TruckDao{
    private static final String FIND_ALL_QUERY = "SELECT t FROM Truck t";

    private final SessionFactory sessionFactory;

    public TruckDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Truck entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.save(entity);

            session.getTransaction().commit();
        } catch (PersistenceException pe) {
            throw new DatabaseRuntimeException("DB truck saving exception", pe);
        }
    }

    @Override
    public Optional<Truck> findById(Long id) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final Truck truck = session.get(Truck.class, id);

            if (truck != null) {
                Hibernate.initialize(truck.getCurrentDrivers());
            }

            session.getTransaction().commit();

            return Optional.ofNullable(truck);
        } catch (PersistenceException pe) {
            throw new DatabaseRuntimeException("DB find truck by id exception", pe);
        }
    }

    @Override
    public List<Truck> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            final List<Truck> allTrucks = session.createQuery(FIND_ALL_QUERY, Truck.class).getResultList();

            session.getTransaction().commit();

            return allTrucks;
        }  catch (PersistenceException pe) {
            throw new DatabaseRuntimeException("DB getting all trucks exception", pe);
        }
    }

    @Override
    public void update(Truck entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.update(entity);

            session.getTransaction().commit();
        }  catch (PersistenceException pe) {
            throw new DatabaseRuntimeException("DB truck updating exception", pe);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            final Truck deletingTruck = session.get(Truck.class, id);

            session.delete(deletingTruck);

            session.getTransaction().commit();
        }  catch (PersistenceException pe) {
            throw new DatabaseRuntimeException("DB driver deleting exception", pe);
        }
    }

    @Override
    public void deleteAllByIds(Set<Long> ids) {

    }

    @Override
    public Optional<Truck> findByRegNumber(String regNumber) {
        return Optional.empty();
    }

}
