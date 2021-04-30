package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.WaypointDao;
import com.chpok.logiweb.dao.exception.DatabaseRuntimeException;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
public class WaypointDaoImpl implements WaypointDao {
    private static final String FIND_ALL_QUERY = "SELECT w FROM com.chpok.logiweb.model.Order$Waypoint w";
    private static final String FIND_ALL_BY_ORDER_ID_QUERY = "SELECT w FROM com.chpok.logiweb.model.Order$Waypoint w WHERE w.order.id = :id";

    private final HibernateUtil hibernateUtil;

    public WaypointDaoImpl(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public void save(Order.Waypoint entity) {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()){
            session.beginTransaction();

            session.save(entity);

            session.getTransaction().commit();
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB waypoint saving exception", npe);
        }
    }

    @Override
    public Optional<Order.Waypoint> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Order.Waypoint> findAll() {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()){
            session.beginTransaction();

            final List<Order.Waypoint> waypoints = session.createQuery(FIND_ALL_QUERY, Order.Waypoint.class).getResultList();

            for (Order.Waypoint waypoint : waypoints) {
                Hibernate.initialize(waypoint.getCargo());
                Hibernate.initialize(waypoint.getOrder());
                Hibernate.initialize(waypoint.getLocation());
            }
            session.getTransaction().commit();

            return waypoints;
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB getting all waypoints exception", npe);
        }
    }

    @Override
    public void update(Order.Waypoint entity) {

    }

    @Override
    public void deleteById(Long id) {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()){
            session.beginTransaction();

            final Order.Waypoint deletingWaypoint = session.load(Order.Waypoint.class, id);

            session.delete(deletingWaypoint);

            session.getTransaction().commit();
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB delete waypoint by id exception", npe);
        }
    }

    @Override
    public void deleteAllByIds(Set<Long> ids) {

    }

    @Override
    public List<Order.Waypoint> findAllByOrderId(Long orderId) {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()){
            session.beginTransaction();

            Query<Order.Waypoint> query = session.createQuery(FIND_ALL_BY_ORDER_ID_QUERY, Order.Waypoint.class);

            query.setParameter("id", orderId);

            List<Order.Waypoint> waypoints = query.getResultList();

            session.getTransaction().commit();

            return waypoints;
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB finding waypoint by order id exception", npe);
        }
    }
}
