package com.example.courtmanagement_service.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourtDetailResponse {
    Integer id;
    Integer courtId;
    String courtNumber;
    Integer courtType;
    Double pricePerHouse;
    String description;
    Long createdAt;
    Long updatedAt;
}

