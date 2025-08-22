package com.example.courtmanagement_service.service;

import com.example.courtmanagement_service.dto.CourtPriceRuleDetailDTO;
import com.example.courtmanagement_service.entity.CourtPriceRule;
import com.example.courtmanagement_service.entity.CourtPriceRuleDay;
import com.example.courtmanagement_service.repo.jpa.CourtPriceRuleDayRepo;
import com.example.courtmanagement_service.repo.jpa.CourtPriceRuleRepo;
import com.example.courtmanagement_service.request.CourtPriceRuleCreateUpdateRequest;
import com.qlsc.qlsc_common.response.ApiResponse;
import com.qlsc.qlsc_common.util.NumberQlscUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourtPriceRuleService {
    Logger LOG = LoggerFactory.getLogger(this.getClass());
    CourtPriceRuleRepo courtPriceRuleRepo;
    CourtPriceRuleDayRepo courtPriceRuleDayRepo;

    public ApiResponse<?> create(String keyLog, CourtPriceRuleCreateUpdateRequest request) {
        LOG.info("{} Start create CourtPriceRule service ", keyLog);
        ApiResponse<CourtPriceRule> response = new ApiResponse<>();
        String errorMessage = request.validate();
        if (StringUtils.hasLength(errorMessage)) {
            return response.setMessageFailed(errorMessage);
        }
        if (courtPriceRuleRepo.existsByCourtIdAndHashKey(request.getCourtId(), request.getHashKey())) {
            return response.setMessageFailed("CourtPriceRule already exist with court Id = "
                    + request.getCourtId() + ", HashKey = " + request.getHashKey());
        }
        CourtPriceRule courtPriceRule = new CourtPriceRule();
        BeanUtils.copyProperties(request, courtPriceRule);

        CourtPriceRule entity = courtPriceRuleRepo.save(courtPriceRule);

        Long id = entity.getId();
        List<CourtPriceRuleDay> courtPriceRuleDays = request.getDays()
                .stream()
                .map(x -> CourtPriceRuleDay
                        .builder()
                        .courtPriceRuleId(id)
                        .dayOfWeek(x)
                        .build())
                .toList();
        courtPriceRuleDayRepo.saveAll(courtPriceRuleDays);

        LOG.info("Save successfully courtPriceRuleDay size = {}", courtPriceRuleDays.size());
        LOG.info("{} End create CourtPriceRule service ", keyLog);
        return response.setDataSuccess(courtPriceRule);

    }

    public ApiResponse<?> getCourtPriceRuleDetail(String keyLog, Integer courtId) {
        ApiResponse<List<CourtPriceRuleDetailDTO>> response = new ApiResponse<>();
        List<Object[]> dataRaw = courtPriceRuleRepo.getCourtPriceRuleDetail(courtId);
        if (dataRaw.isEmpty()) {
            return response.setDataSuccess(new ArrayList<>());
        }
        Map<String, CourtPriceRuleDetailDTO> map = new LinkedHashMap<>();

        for (Object[] row : dataRaw) {
            Long id = NumberQlscUtils.parseLong(row[0]);
            Integer startTime = NumberQlscUtils.parseInteger(row[1]);
            Integer endTime = NumberQlscUtils.parseInteger(row[2]);
            Integer dayOfWeek = NumberQlscUtils.parseInteger(row[3]);
            Double factor = NumberQlscUtils.parseDouble(row[4]);

            String key = String.valueOf(row[5]);

            CourtPriceRuleDetailDTO dto = map.get(key);
            if (dto == null) {
                dto = new CourtPriceRuleDetailDTO();
                dto.setCourtId(id);
                dto.setStartTime(startTime);
                dto.setEndTime(endTime);
                dto.setFactor(factor);
                dto.setDays(new ArrayList<>());
                map.put(key, dto);
            }
            dto.getDays().add(dayOfWeek);
        }
//        List<CourtPriceRuleDetailDTO> res = new ArrayList<>();
//        for (Map.Entry<String, CourtPriceRuleDetailDTO> entry : map.entrySet()) {
//            res.add(entry.getValue());
//        }
        response.setDataSuccess(new ArrayList<>(map.values()));
        return response;
    }
}
