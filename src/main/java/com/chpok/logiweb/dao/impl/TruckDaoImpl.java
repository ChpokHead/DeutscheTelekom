package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.TruckDao;
import com.chpok.logiweb.dao.exception.DatabaseRuntimeException;
import com.chpok.logiweb.model.Truck;
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
public class TruckDaoImpl implements TruckDao{
    private static final String FIND_ALL_QUERY = "SELECT t FROM Truck t";

    @Autowired
    private HibernateUtil hibernateUtil;

    @Override
    public void save(Truck entity) {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()) {
            session.beginTransaction();

            session.save(entity);

            session.getTransaction().commit();
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB truck saving exception", npe);
        }

    }

    @Override
    public Optional<Truck> findById(Long id) {
        try (Session session =
                Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()){
            session.beginTransaction();

            final Truck truck = session.get(Truck.class, id);

            Hibernate.initialize(truck.getCurrentDrivers());

            session.getTransaction().commit();

            return Optional.of(truck);
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB find truck by id exception", npe);
        }
    }

    @Override
    public List<Truck> findAll() {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()) {
            session.beginTransaction();

            final List<Truck> allTrucks = session.createQuery(FIND_ALL_QUERY, Truck.class).getResultList();
            session.getTransaction().commit();

            return allTrucks;
        }  catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB getting all trucks exception", npe);
        }

    }

    @Override
    public void update(Truck entity) {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()) {
            session.beginTransaction();

            session.update(entity);

            session.getTransaction().commit();
        }  catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB truck updating exception", npe);
        }

    }

    @Override
    public void deleteById(Long id) {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()) {
            session.beginTransaction();

            final Truck deletingTruck = session.get(Truck.class, id);

            session.delete(deletingTruck);

            session.getTransaction().commit();
        }  catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB driver deleting exception", npe);
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
