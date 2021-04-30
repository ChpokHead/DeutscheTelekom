package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.LocationDao;
import com.chpok.logiweb.dao.exception.DatabaseRuntimeException;
import com.chpok.logiweb.model.Location;
import com.chpok.logiweb.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
public class LocationDaoImpl implements LocationDao {
    private static final String FIND_BY_NAME_QUERY = "SELECT l FROM Location l WHERE l.name = :name";
    private static final String FIND_ALL_QUERY = "SELECT l FROM Location l";

    @Autowired
    private HibernateUtil hibernateUtil;

    @Override
    public void save(Location entity) {

    }

    @Override
    public Optional<Location> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Location> findAll() {
        try (Session session = Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()){
            session.beginTransaction();

            final List<Location> locations = session.createQuery(FIND_ALL_QUERY, Location.class).getResultList();

            session.getTransaction().commit();

            return locations;
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB getting all locations exception", npe);
        }
    }

    @Override
    public void update(Location entity) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteAllByIds(Set<Long> ids) {

    }

    @Override
    public Optional<Location> findByName(String name) {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()){
            session.beginTransaction();

            Query<Location> selectQuery = session.createQuery(FIND_BY_NAME_QUERY, Location.class);

            selectQuery.setParameter("name", name);

            final Optional<Location> result = Optional.of(selectQuery.getSingleResult());

            session.getTransaction().commit();

            return result;
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB find location by name exception", npe);
        }
    }
}
