package com.example.courtmanagement_service.entity;

import com.qlsc.qlsc_common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = CourtPriceRule.CourtPriceRuleConstant.TABLE,
        schema = CourtPriceRule.CourtPriceRuleConstant.SCHEMA)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourtPriceRule extends BaseEntity {

    public static class CourtPriceRuleConstant {
        public static final String TABLE = "court_price_rule";
        public static final String SCHEMA = "badminton";
        public static final String COURT_ID = "court_id";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String FACTOR = "factor";
        public static final String HASH_KEY = "hash_key";

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = CourtPriceRuleConstant.COURT_ID, nullable = false)
    Long courtId;

    @Column(name = CourtPriceRuleConstant.START_TIME,nullable = false)
    Integer startTime;

    @Column(name = CourtPriceRuleConstant.END_TIME,nullable = false)
    Integer endTime;

    @Column(name = CourtPriceRuleConstant.FACTOR,nullable = false)
    Double factor;

    @Column(name = CourtPriceRuleConstant.HASH_KEY,nullable = false)
    String hashKey;


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

