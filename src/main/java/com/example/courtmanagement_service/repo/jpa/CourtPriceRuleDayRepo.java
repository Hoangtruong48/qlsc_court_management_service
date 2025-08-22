package com.example.courtmanagement_service.repo.jpa;

import com.example.courtmanagement_service.entity.CourtPriceRuleDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtPriceRuleDayRepo extends JpaRepository<CourtPriceRuleDay, Long> {
}
