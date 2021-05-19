package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.WaypointDao;
import com.chpok.logiweb.model.Order;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class WaypointDaoImpl implements WaypointDao {
    private static final String FIND_ALL_QUERY = "SELECT w FROM com.chpok.logiweb.model.Order$Waypoint w";
    private static final String FIND_ALL_BY_ORDER_ID_QUERY = "SELECT w FROM com.chpok.logiweb.model.Order$Waypoint w WHERE w.order.id = :id ORDER BY w.id";

    private final SessionFactory sessionFactory;

    public WaypointDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Order.Waypoint entity) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            session.save(entity);

            session.getTransaction().commit();
        }
    }

    @Override
    public Optional<Order.Waypoint> findById(Long id) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final Order.Waypoint waypoint = session.get(Order.Waypoint.class, id);

            session.getTransaction().commit();

            return Optional.ofNullable(waypoint);
        }
    }

    @Override
    public void updateWaypoints(List<Order.Waypoint> waypoints) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            for (Order.Waypoint waypoint : waypoints) {
                session.update(waypoint);
            }

            session.getTransaction().commit();
        }
    }

    @Override
    public List<Order.Waypoint> findAll() {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final List<Order.Waypoint> waypoints = session.createQuery(FIND_ALL_QUERY, Order.Waypoint.class).getResultList();

            if (!waypoints.isEmpty()) {
                for (Order.Waypoint waypoint : waypoints) {
                    Hibernate.initialize(waypoint.getCargo());
                    Hibernate.initialize(waypoint.getOrder());
                    Hibernate.initialize(waypoint.getLocation());
                }
            }

            session.getTransaction().commit();

            return waypoints;
        }
    }

    @Override
    public void update(Order.Waypoint entity) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            session.update(entity);

            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final Order.Waypoint deletingWaypoint = session.get(Order.Waypoint.class, id);

            session.delete(deletingWaypoint);

            session.getTransaction().commit();
        }
    }

    @Override
    public List<Order.Waypoint> findAllByOrderId(Long orderId) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            Query<Order.Waypoint> query = session.createQuery(FIND_ALL_BY_ORDER_ID_QUERY, Order.Waypoint.class);

            query.setParameter("id", orderId);

            List<Order.Waypoint> waypoints = query.getResultList();

            session.getTransaction().commit();

            return waypoints;
        }
    }

}
