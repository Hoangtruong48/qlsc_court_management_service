package com.example.courtmanagement_service.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourtPriceRuleDetailDTO {
    Long courtId;
    Integer startTime;
    Integer endTime;
    Double factor;
    String hashKey;
    List<Integer> days;
}
