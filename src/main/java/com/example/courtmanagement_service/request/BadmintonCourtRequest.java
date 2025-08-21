package com.example.courtmanagement_service.request;


import com.example.courtmanagement_service.entity.BadmintonCourt;
import com.qlsc.qlsc_common.mappable.Mappable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.util.StringUtils;

import java.time.Instant;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BadmintonCourtRequest implements Mappable<BadmintonCourt> {
    String name;
    String address;
    String description;
    Integer openingTime;
    Integer closingTime;
    Integer totalCourts;
    String contactPhone;
    String contactEmail;
    String floorMaterial;
    String lightingType;
    Boolean hasShower;
    Boolean hasParking;
    Boolean hasDrinksService;
    Integer status;

    @Override
    public BadmintonCourt toEntity() {
        return BadmintonCourt.builder()
                .name(this.name)
                .address(this.address)
                .description(this.description)
                .openingTime(this.openingTime)
                .closingTime(this.closingTime)
                .totalCourts(this.totalCourts)
                .contactPhone(this.contactPhone)
                .contactEmail(this.contactEmail)
                .floorMaterial(this.floorMaterial)
                .lightingType(this.lightingType)
                .hasShower(this.hasShower)
                .hasParking(this.hasParking)
                .hasDrinksService(this.hasDrinksService)
                .status(this.status)
                .createdAt(Instant.now().toEpochMilli())
                .updatedAt(Instant.now().toEpochMilli())
                .build();
    }

    public String validate() {
        if (!StringUtils.hasLength(name)) {
            return "Court name is required";
        }
        if (!StringUtils.hasLength(address)) {
            return "Address is required";
        }
        if (totalCourts == null || totalCourts <= 0) {
            return "Total courts must be greater than 0";
        }
        if (openingTime == null || closingTime == null) {
            return "OpeningTime và ClosingTime không được null";
        }
        if (openingTime < 0 || openingTime > 24) {
            return "OpeningTime phải nằm trong khoảng [0, 24]";
        }
        if (closingTime < 0 || closingTime > 24) {
            return "ClosingTime phải nằm trong khoảng [0, 24]";
        }
        if (openingTime >= closingTime) {
            return "OpeningTime phải nhỏ hơn ClosingTime";
        }
        return null;
    }
}
