package com.example.courtmanagement_service.dto;

import com.qlsc.qlsc_common.util.AppUtils;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BadmintonInfoTimeDTO {
    Integer courtId;
    Integer courtNumber;
    Integer openingTime;
    Integer closingTime;

    public static List<BadmintonInfoTimeDTO> convertRawDataToDTO(List<Object[]> rawData) {
        return rawData.stream().map(
                x -> new BadmintonInfoTimeDTO(
                        AppUtils.parseIntNew(x[0]),
                        AppUtils.parseIntNew(x[1]),
                        AppUtils.parseIntNew(x[2]),
                        AppUtils.parseIntNew(x[3])
                )
        ).collect(Collectors.toList());
    }
}
