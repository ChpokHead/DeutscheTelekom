package com.chpok.logiweb.controller;

import java.time.LocalDate;
import java.util.Collections;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.chpok.logiweb.service.OrderReportService;

@Controller
@RequestMapping("/employeeReport")
public class EmployeeReportPageController {
    @Autowired
    private OrderReportService reportService;

    @GetMapping
    public ModelAndView getReports() {
        final ModelAndView mav = new ModelAndView("employeeReportPage");

        mav.addObject("reports", reportService.getAllReports());

        return mav;
    }

    @GetMapping("/{reportId}")
    public ModelAndView viewReport(@PathVariable Long reportId) {
        ModelAndView mav = new ModelAndView("employeeReportViewPage");

        mav.addObject("report", reportService.getReportById(reportId));

        return mav;
    }

    @DeleteMapping
    public String deleteReport(@RequestParam(name = "report-id-delete") Long reportId) {
        if (reportId != null) {
            reportService.deleteReport(reportId);
        }

        return "employeeReportPage";
    }

    @PostMapping
    public ModelAndView getFilteredReports(
            @RequestParam(name = "travel-start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
            @RequestParam(name = "travel-end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate) {

        final ModelAndView mav = new ModelAndView("employeeReportPage");

        mav.addObject("reports", reportService.getFilteredReports(startDate, endDate));

        return mav;
    }

    @PostMapping("/search")
    public ModelAndView searchReport(@RequestParam(name = "orderId") Long orderId) {
        final ModelAndView mav = new ModelAndView("employeeReportPage");

        if (orderId != null) {
            try {
                mav.addObject("searchId", orderId);
                mav.addObject("reports", Collections.singletonList(reportService.getReportByOrderId(orderId)));
            } catch (EntityNotFoundException nfe) {
                mav.addObject("searchId", orderId);
                mav.addObject("reports", Collections.emptyList());
            }
        } else {
            mav.addObject("reports", reportService.getAllReports());
        }

        return mav;
    }
}
