package com.xyzcorp.api.emissionscalculator.repository;

import com.xyzcorp.api.emissionscalculator.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findByName(String name);
}
