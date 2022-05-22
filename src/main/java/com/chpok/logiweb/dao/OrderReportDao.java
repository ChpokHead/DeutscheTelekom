package com.chpok.logiweb.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.chpok.logiweb.model.OrderReport;

public interface OrderReportDao extends CrudDao<OrderReport> {
    Optional<OrderReport> findByOrderId(Long orderId);
    List<OrderReport> findByOrderReportDates(LocalDate startDate, LocalDate endDate);
}
