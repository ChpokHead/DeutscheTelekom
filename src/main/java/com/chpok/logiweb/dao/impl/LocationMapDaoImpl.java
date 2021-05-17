package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.LocationMapDao;
import com.chpok.logiweb.dao.exception.DatabaseRuntimeException;
import com.chpok.logiweb.model.LocationMap;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class LocationMapDaoImpl implements LocationMapDao {
    private static final String FIND_BY_STARTING_AND_ENDING_IDS_QUERY
            = "SELECT locationMap FROM LocationMap locationMap WHERE locationMap.startingLocation.id = :startingId AND locationMap.endingLocation.id = :endingId";

    private final SessionFactory sessionFactory;

    public LocationMapDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(LocationMap entity) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            session.save(entity);

            session.getTransaction().commit();
        } catch (PersistenceException pe) {
            throw new DatabaseRuntimeException("DB locationMap saving exception", pe);
        }
    }

    @Override
    public Optional<LocationMap> findById(Long id) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final LocationMap foundLocationMap = session.get(LocationMap.class, id);

            session.getTransaction().commit();

            return Optional.ofNullable(foundLocationMap);
        } catch (PersistenceException pe) {
            throw new DatabaseRuntimeException("DB locationMap finding by id exception", pe);
        }
    }

    @Override
    public List<LocationMap> findAll() {
        return null;
    }

    @Override
    public void update(LocationMap entity) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteAllByIds(Set<Long> ids) {

    }

    @Override
    public Optional<LocationMap> findByStartingAndEndingLocationsIds(Long startingLocationId, Long endingLocationId) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            Query<LocationMap> query = session.createQuery(FIND_BY_STARTING_AND_ENDING_IDS_QUERY, LocationMap.class);

            query.setParameter("startingId", startingLocationId);

            query.setParameter("endingId", endingLocationId);

            final LocationMap findingLocationMap = query.getSingleResult();

            session.getTransaction().commit();

            return Optional.of(findingLocationMap);
        } catch (PersistenceException pe) {
            throw new DatabaseRuntimeException("DB locationMap finding by start and ending locations ids exception", pe);
        }
    }
}
