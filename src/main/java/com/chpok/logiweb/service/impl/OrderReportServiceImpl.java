package com.chpok.logiweb.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chpok.logiweb.dao.OrderReportDao;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.OrderReport;
import com.chpok.logiweb.model.enums.WaypointType;
import com.chpok.logiweb.service.OrderReportService;

@Service
public class OrderReportServiceImpl implements OrderReportService {
    private static final Logger LOGGER = LogManager.getLogger(OrderReportServiceImpl.class);

    @Autowired
    private OrderReportDao reportDao;

    @Override
    public void createOrderReport(OrderDto order, Short distance) {
        final OrderReport report = new OrderReport();

        report.setOrderId(order.getId());
        report.setReportCreationDate(LocalDate.now());
        report.setOrderCreationDate(order.getCreationDate());
        report.setOrderStartDate(order.getStartDate());
        report.setOrderEndDate(order.getEndDate());

        StringJoiner joiner = new StringJoiner(",");
        for (Driver driver : order.getCurrentDrivers()) {
            joiner.add(driver.getFirstName() + " " + driver.getLastName() + " #" + driver.getId());
        }
        report.setDrivers(joiner.toString());

        report.setTruck(order.getCurrentTruck().getRegNumber());

        report.setDistance(distance);

        joiner = new StringJoiner(",");
        for (Order.Waypoint waypoint : order.getWaypoints()) {
            if (waypoint.getType() == WaypointType.LOADING) {
                joiner.add(waypoint.getLocation().getName() + "(Погрузка) " + waypoint.getCargo().getName() + " #"
                        + waypoint.getCargo().getId());
            } else {
                joiner.add(waypoint.getLocation().getName() + "(Выгрузка) " + waypoint.getCargo().getName() + " #"
                        + waypoint.getCargo().getId());
            }
        }

        report.setRoute(joiner.toString());

        reportDao.save(report);
    }

    @Override
    public List<OrderReport> getAllReports() {
        return reportDao.findAll();
    }

    @Override
    public OrderReport getReportById(Long reportId) {
        try {
            return reportDao.findById(reportId).orElseThrow(NoSuchElementException::new);
        } catch (HibernateException | NoSuchElementException | NoResultException e) {
            LOGGER.error("getting report by id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public OrderReport getReportByOrderId(Long orderId) {
        try {
            return reportDao.findByOrderId(orderId).orElseThrow(NoSuchElementException::new);
        } catch (HibernateException | NoSuchElementException | NoResultException e) {
            LOGGER.error("getting report by order id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public void deleteReport(Long reportId) {
        reportDao.deleteById(reportId);
    }

    @Override
    public List<OrderReport> getFilteredReports(LocalDate start, LocalDate end) {
        return reportDao.findByOrderReportDates(start, end);
    }
}
