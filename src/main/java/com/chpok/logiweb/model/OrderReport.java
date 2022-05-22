package com.chpok.logiweb.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_report")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderReport extends AbstractModel {
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "report_creation_date")
    private LocalDate reportCreationDate;

    @Column(name = "order_creation_date")
    private LocalDate orderCreationDate;

    @Column(name = "order_start_date")
    private LocalDate orderStartDate;

    @Column(name = "order_end_date")
    private LocalDate orderEndDate;

    @Column(name = "route")
    private String route;

    @Column(name = "distance")
    private Short distance;

    @Column(name = "truck")
    private String truck;

    @Column(name = "drivers")
    private String drivers;
}
