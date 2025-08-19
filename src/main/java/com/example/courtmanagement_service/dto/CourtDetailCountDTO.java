package com.example.courtmanagement_service.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourtDetailCountDTO {
    Integer courtId;
    Long total;
}
