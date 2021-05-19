package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.LocationDao;
import com.chpok.logiweb.model.Location;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LocationDaoImpl implements LocationDao {
    private static final String FIND_BY_NAME_QUERY = "SELECT l FROM Location l WHERE l.name = :name";
    private static final String FIND_ALL_QUERY = "SELECT l FROM Location l";

    private final SessionFactory sessionFactory;

    public LocationDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Location entity) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            session.save(entity);

            session.getTransaction().commit();
        }
    }

    @Override
    public Optional<Location> findById(Long id) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final Location foundLocation = session.get(Location.class, id);

            session.getTransaction().commit();

            return Optional.ofNullable(foundLocation);
        }
    }

    @Override
    public List<Location> findAll() {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final List<Location> locations = session.createQuery(FIND_ALL_QUERY, Location.class).getResultList();

            session.getTransaction().commit();

            return locations;
        }
    }

    @Override
    public Optional<Location> findByName(String name) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            Query<Location> selectQuery = session.createQuery(FIND_BY_NAME_QUERY, Location.class);

            selectQuery.setParameter("name", name);

            final Optional<Location> result = selectQuery.uniqueResultOptional();

            session.getTransaction().commit();

            return result;
        }
    }
}
