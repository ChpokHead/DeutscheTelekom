package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.OrderDao;
import com.chpok.logiweb.dao.exception.DatabaseRuntimeException;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
public class OrderDaoImpl implements OrderDao {
    private static final String FIND_ALL_QUERY = "SELECT o FROM Order o";

    private final HibernateUtil hibernateUtil;

    public OrderDaoImpl(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public void save(Order entity) {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()){
            session.beginTransaction();

            session.save(entity);

            session.getTransaction().commit();
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB order saving exception", npe);
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        try (Session session = Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()){
            session.beginTransaction();

            final Order order = session.get(Order.class, id);

            session.getTransaction().commit();

            return Optional.of(order);
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB find driver by id exception", npe);
        }
    }

    @Override
    public List<Order> findAll() {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()){
            session.beginTransaction();

            final List<Order> orders = session.createQuery(FIND_ALL_QUERY, Order.class).getResultList();

            for (Order order : orders) {
                Hibernate.initialize(order.getWaypoints());
            }
            session.getTransaction().commit();

            return orders;
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB getting all orders exception", npe);
        }
    }

    @Override
    public void update(Order entity) {

    }

    @Override
    public void deleteById(Long id) {
        try (Session session =
                     Objects.requireNonNull(hibernateUtil.sessionFactory().getObject()).openSession()){
            session.beginTransaction();

            final Order deletingOrder = session.load(Order.class, id);

            session.delete(deletingOrder);

            session.getTransaction().commit();
        } catch (NullPointerException npe) {
            throw new DatabaseRuntimeException("DB delete order by id exception", npe);
        }
    }

    @Override
    public void deleteAllByIds(Set<Long> ids) {

    }
}
