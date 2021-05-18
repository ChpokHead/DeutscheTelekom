package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.CargoDao;
import com.chpok.logiweb.model.Cargo;
import com.chpok.logiweb.model.Order;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class CargoDaoImpl implements CargoDao {
    private static final String FIND_ALL_QUERY = "SELECT c FROM Cargo c";
    private static final String FIND_BY_NAME_QUERY = "SELECT c FROM Cargo c WHERE c.name = :name";

    private final SessionFactory sessionFactory;

    public CargoDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Cargo> findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            final List<Cargo> foundCargos = session.createQuery(FIND_BY_NAME_QUERY, Cargo.class).setParameter("name", name).getResultList();

            if (!foundCargos.isEmpty()) {
                for (Cargo cargo : foundCargos) {
                    for (Order.Waypoint waypoint : cargo.getWaypoints()) {
                        Hibernate.initialize(waypoint);
                    }
                }
            }

            session.getTransaction().commit();

            return foundCargos;
        }
    }

    @Override
    public void save(Cargo entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.save(entity);

            session.getTransaction().commit();
        }
    }

    @Override
    public Optional<Cargo> findById(Long id) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final Cargo cargo = session.get(Cargo.class, id);

            if (cargo != null) {
                for (Order.Waypoint waypoint : cargo.getWaypoints()) {
                    Hibernate.initialize(waypoint);
                }
            }

            session.getTransaction().commit();

            return Optional.ofNullable(cargo);
        }
    }

    @Override
    public List<Cargo> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            final List<Cargo> allCargos = session.createQuery(FIND_ALL_QUERY, Cargo.class).getResultList();

            if (!allCargos.isEmpty()) {
                for (Cargo cargo : allCargos) {
                    for (Order.Waypoint waypoint : cargo.getWaypoints()) {
                        Hibernate.initialize(waypoint);
                    }
                }
            }

            session.getTransaction().commit();

            return allCargos;
        }
    }

    @Override
    public void update(Cargo entity) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.update(entity);

            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            final Cargo deletingCargo= session.load(Cargo.class, id);

            session.delete(deletingCargo);

            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteAllByIds(Set<Long> ids) {

    }
}
