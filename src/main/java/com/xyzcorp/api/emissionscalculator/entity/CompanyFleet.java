package com.xyzcorp.api.emissionscalculator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "company_fleet")
public class CompanyFleet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Column(name = "vehicle_type", nullable = false)
    private String vehicle;

    @Column(name = "average_weekly_mileage", nullable = false)
    private double mileage;

    @JoinColumn(name = "company_id")
    @OneToOne
    private Company company;

    @Column(name="created_on", nullable = false)
    private LocalDateTime createdOn;
}
