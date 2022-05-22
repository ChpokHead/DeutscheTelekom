package com.chpok.logiweb.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.model.OrderReport;

public interface OrderReportService {
    void createOrderReport(OrderDto order, Short distance);
    List<OrderReport> getAllReports();
    OrderReport getReportById(Long reportId);
    OrderReport getReportByOrderId(Long orderId);
    void deleteReport(Long reportId);
    List<OrderReport> getFilteredReports(LocalDate start, LocalDate end);
}
