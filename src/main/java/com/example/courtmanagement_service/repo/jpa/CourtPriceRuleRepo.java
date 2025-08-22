package com.example.courtmanagement_service.repo.jpa;

import com.example.courtmanagement_service.entity.CourtPriceRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourtPriceRuleRepo extends JpaRepository<CourtPriceRule, Long> {
    boolean existsByCourtIdAndHashKey(Long courtId, String hashKey);

    @Query(value = """
            select c1.court_id, c1.start_time, c1.end_time, c2.day_of_week, c1.factor, c1.hash_key
            from badminton.court_price_rule c1
                left join badminton.court_price_rule_day c2 on c1.id = c2.court_price_rule_id
            where c1.court_id = :courtId
            """, nativeQuery = true)
    List<Object[]> getCourtPriceRuleDetail(@Param("courtId") Integer courtId);
}
