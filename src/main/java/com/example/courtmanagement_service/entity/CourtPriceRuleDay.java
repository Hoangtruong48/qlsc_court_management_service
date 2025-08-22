package com.example.courtmanagement_service.entity;

import com.qlsc.qlsc_common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = CourtPriceRuleDay.CourtPriceRuleDayConstant.TABLE,
        schema = CourtPriceRuleDay.CourtPriceRuleDayConstant.SCHEMA)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourtPriceRuleDay extends BaseEntity {
    public static class CourtPriceRuleDayConstant {
        public static final String TABLE = "court_price_rule_day";
        public static final String SCHEMA = "badminton";
        private static final String COURT_PRICE_RULE_ID = "court_price_rule_id";
        private static final String DAY_OF_WEEK = "day_of_week";
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = CourtPriceRuleDayConstant.COURT_PRICE_RULE_ID)
    Long courtPriceRuleId;
    @Column(name = CourtPriceRuleDayConstant.DAY_OF_WEEK)
    Integer dayOfWeek;

    @PrePersist
    protected void onCreate() {
        this.setCreatedAt(System.currentTimeMillis());
        this.setUpdatedAt(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.setUpdatedAt(System.currentTimeMillis());
    }

}
