package com.example.courtmanagement_service.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BadmintonCourtResponse {
    Integer id;
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
    Long createdAt;
    Long updatedAt;
}

