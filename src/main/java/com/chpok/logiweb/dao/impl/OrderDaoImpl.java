package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.OrderDao;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.Order;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderDaoImpl implements OrderDao {
    private static final String FIND_ALL_QUERY = "SELECT o FROM Order o";

    private final SessionFactory sessionFactory;

    public OrderDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Order entity) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            session.save(entity);

            session.getTransaction().commit();
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final Order order = session.get(Order.class, id);

            if (order != null) {
                Hibernate.initialize(order.getCurrentTruck());

                for (Driver driver : order.getDrivers()) {
                    Hibernate.initialize(driver);
                }
            }

            session.getTransaction().commit();

            return Optional.ofNullable(order);
        }
    }

    @Override
    public List<Order> findAll() {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final List<Order> orders = session.createQuery(FIND_ALL_QUERY, Order.class).getResultList();

            if (!orders.isEmpty()) {
                for (Order order : orders) {
                    Hibernate.initialize(order.getWaypoints());

                    for (Driver driver : order.getDrivers()) {
                        Hibernate.initialize(driver);
                    }
                }
            }

            session.getTransaction().commit();

            return orders;
        }
    }

    @Override
    public void update(Order entity) {
        try (Session session =
                     sessionFactory.openSession()){
            session.beginTransaction();

            session.update(entity);

            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session =
                     sessionFactory.openSession()){
            session.beginTransaction();

            final Order deletingOrder = session.get(Order.class, id);

            session.delete(deletingOrder);

            session.getTransaction().commit();
        }
    }

}
