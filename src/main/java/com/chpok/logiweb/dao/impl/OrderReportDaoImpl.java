package com.chpok.logiweb.dao.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import com.chpok.logiweb.dao.OrderReportDao;
import com.chpok.logiweb.model.OrderReport;

@Component
public class OrderReportDaoImpl implements OrderReportDao {
    private static final String FIND_ALL_QUERY = "SELECT o FROM OrderReport o";
    private static final String FIND_BY_ORDER_ID_QUERY = "SELECT o FROM OrderReport o WHERE o.orderId = :orderId";
    private static final String FIND_BY_DATES_QUERY = "SELECT o FROM OrderReport o "
            + "WHERE o.reportCreationDate >= :startDate and o.reportCreationDate <= :endDate";

    private final SessionFactory sessionFactory;

    public OrderReportDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long save(OrderReport entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.save(entity);

            session.getTransaction().commit();

            return entity.getId();
        }
    }

    @Override
    public Optional<OrderReport> findById(Long id) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final OrderReport report = session.get(OrderReport.class, id);

            session.getTransaction().commit();

            return Optional.ofNullable(report);
        }
    }

    @Override
    public List<OrderReport> findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            final List<OrderReport> reports = session.createQuery(FIND_ALL_QUERY, OrderReport.class).getResultList();

            session.getTransaction().commit();

            return reports;
        }
    }

    @Override
    public void update(OrderReport entity) {
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

            final OrderReport deletingReport = session.load(OrderReport.class, id);

            session.delete(deletingReport);

            session.getTransaction().commit();
        }
    }

    @Override
    public Optional<OrderReport> findByOrderId(Long orderId) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final OrderReport report = session.createQuery(FIND_BY_ORDER_ID_QUERY, OrderReport.class)
                    .setParameter("orderId", orderId).getSingleResult();

            session.getTransaction().commit();

            return Optional.ofNullable(report);
        }
    }

    @Override
    public List<OrderReport> findByOrderReportDates(LocalDate startDate, LocalDate endDate) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final List<OrderReport> reports = session.createQuery(FIND_BY_DATES_QUERY, OrderReport.class)
                    .setParameter("startDate", startDate).setParameter("endDate", endDate).getResultList();

            session.getTransaction().commit();

            return reports;
        }
    }
}
