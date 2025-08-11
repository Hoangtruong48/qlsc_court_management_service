package com.example.courtmanagement_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = CourtDetail.CourtDetailConstant.TABLE_NAME,
        schema = CourtDetail.CourtDetailConstant.SCHEMA)
// thông tin chi tiết từng sân cầu
public class CourtDetail {
    public static class CourtDetailConstant {
        public static final String TABLE_NAME = "court_details";
        public static final String SCHEMA = "badminton";

        public static final String ID = "id";
        public static final String COURT_ID = "court_id";
        public static final String COURT_NUMBER = "court_number";
        public static final String COURT_TYPE = "court_type";

        public static final String STATUS = "status";
        public static final String PRICE_PER_HOUR = "price_per_hour";
        public static final String CREATED_AT = "created_at";
        public static final String UPDATED_AT = "updated_at";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = CourtDetailConstant.ID)
    Integer id;

    // mapping with id table court
    @Column(name = CourtDetailConstant.COURT_ID, nullable = false)
    Integer courtId;

    @Column(name = CourtDetailConstant.COURT_NUMBER, nullable = false)
    String courtNumber;

    @Column(name = CourtDetailConstant.COURT_TYPE, nullable = false)
    Integer courtType;

    // trang thái on/off
    @Column(name = CourtDetailConstant.STATUS)
    Integer status;
    // giá trên giờ
    @Column(name = CourtDetailConstant.PRICE_PER_HOUR)
    Double pricePerHour;

    @Column(name = CourtDetailConstant.CREATED_AT)
    Long createdAt;

    @Column(name = CourtDetailConstant.UPDATED_AT)
    Long updatedAt;
}

