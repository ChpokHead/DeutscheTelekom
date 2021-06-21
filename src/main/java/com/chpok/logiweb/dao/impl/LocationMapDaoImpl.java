package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.LocationMapDao;
import com.chpok.logiweb.model.LocationMap;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class LocationMapDaoImpl implements LocationMapDao {
    private static final String FIND_BY_STARTING_AND_ENDING_IDS_QUERY
            = "SELECT locationMap FROM LocationMap locationMap WHERE locationMap.startingLocation.id = :startingId AND locationMap.endingLocation.id = :endingId";

    private final SessionFactory sessionFactory;

    public LocationMapDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long save(LocationMap entity) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            session.save(entity);

            session.getTransaction().commit();

            return entity.getId();
        }
    }

    @Override
    public Optional<LocationMap> findById(Long id) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final LocationMap foundLocationMap = session.get(LocationMap.class, id);

            session.getTransaction().commit();

            return Optional.ofNullable(foundLocationMap);
        }
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

            return Optional.ofNullable(findingLocationMap);
        }
    }

}
