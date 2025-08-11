package com.example.courtmanagement_service.request;


import com.example.courtmanagement_service.entity.CourtDetail;
import com.qlsc.qlsc_common.mappable.Mappable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourtDetailRequest implements Mappable<CourtDetail> {
    Integer courtId;
    String courtNumber;
    Integer courtType;
    String description;
    Integer status;
    Double pricePerHouse;

    public String validate() {
        if (courtId == null) return "Court ID is required";
        if (courtNumber == null) return "Court number is required";
        if (pricePerHouse == null) return "pricePerHouse is required";
        return null;
    }

    public CourtDetail toEntity() {
        return CourtDetail.builder()
                .courtId(courtId)
                .courtNumber(courtNumber)
                .courtType(courtType)
                .status(status)
                .pricePerHour(pricePerHouse)
                .createdAt(Instant.now().toEpochMilli())
                .build();
    }
}
