package com.xyzcorp.api.emissionscalculator.repository;

import com.xyzcorp.api.emissionscalculator.entity.CompanyFleet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyFleetRepository extends JpaRepository<CompanyFleet, Long> {

    List<CompanyFleet> findByCompanyIdAndEmployeeId(long companyId, String employeeId);
    List<CompanyFleet> findByCompanyId(long companyId);
}
