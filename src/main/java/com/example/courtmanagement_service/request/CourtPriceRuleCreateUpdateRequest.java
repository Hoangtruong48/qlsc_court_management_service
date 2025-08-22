package com.example.courtmanagement_service.request;

import com.example.courtmanagement_service.entity.CourtPriceRule;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qlsc.qlsc_common.util.NumberUtils;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourtPriceRuleCreateUpdateRequest {
    Long id;
    @JsonProperty(CourtPriceRule.CourtPriceRuleConstant.COURT_ID)
    Long courtId;
    @JsonProperty(CourtPriceRule.CourtPriceRuleConstant.START_TIME)
    Integer startTime;
    @JsonProperty(CourtPriceRule.CourtPriceRuleConstant.END_TIME)
    Integer endTime;
    @JsonProperty(CourtPriceRule.CourtPriceRuleConstant.FACTOR)
    Double factor;
    @JsonProperty(CourtPriceRule.CourtPriceRuleConstant.HASH_KEY)
    String hashKey;
    List<Integer> days;

    public String validate() {
        if (courtId == null) {
            return "courtId is required";
        }
        if (startTime == null) {
            return "startTime is required";
        }
        if (endTime == null) {
            return "endTime is required";
        }
        if (factor == null || NumberUtils.compareDouble(factor, 0.0) == -1) {
            return "percent is required";
        }
        if (days == null || days.isEmpty()) {
            return "days is required";
        } else {
            for (Integer day : days) {
                if (day < 2 || day > 8) {
                    return "Day in [2, 8]";
                }
            }
        }
        hashKey = startTime + "_" + endTime;
        return null;
    }
}
