package com.example.courtmanagement_service.controller;

import com.example.courtmanagement_service.request.CourtPriceRuleCreateUpdateRequest;
import com.example.courtmanagement_service.request.base_request.BaseDeleteRequest;
import com.example.courtmanagement_service.service.CourtPriceRuleService;
import com.qlsc.qlsc_common.response.ApiResponse;
import com.qlsc.qlsc_common.util.StringQlscUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/court-price-rule")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourtPriceRuleController {
    Logger LOG = LoggerFactory.getLogger(this.getClass());
    CourtPriceRuleService courtPriceRuleService;

    @GetMapping("/gets")
    public ApiResponse<?> getCourtPriceRule(@RequestParam Integer courtId) {
        String keyLog = StringQlscUtils.generate(6);
        ApiResponse<?> response = new ApiResponse<>();
        LOG.info("{} Start api get court price rule", keyLog);
        response = courtPriceRuleService.getCourtPriceRuleDetail(keyLog, courtId);
        LOG.info("{} End api get court price rule", keyLog);
        return response;
    }

    @PostMapping("/create")
    public ApiResponse<?> createCourtPriceRule(
            @RequestBody CourtPriceRuleCreateUpdateRequest request) {
        String keyLog = StringQlscUtils.generate(6);
        ApiResponse<?> response = new ApiResponse<>();
        LOG.info("{} Start api create court price rule req = {}", keyLog, request);
        response = courtPriceRuleService.create(keyLog, request);
        LOG.info("{} End api create court price rule req = {}", keyLog, request);
        return response;
    }

    // Bổ sung sau
    @PutMapping("/update")
    public ApiResponse<?> updateCourtPriceRule(@RequestBody CourtPriceRuleCreateUpdateRequest request) {
        String keyLog = StringQlscUtils.generate(6);
        ApiResponse<?> response = new ApiResponse<>();
        LOG.info("{} Start api update court price rule", keyLog);

        LOG.info("{} End api update court price rule", keyLog);
        return response;
    }

    @PutMapping("/delete")
    public ApiResponse<?> deleteCourtPriceRule(@RequestBody BaseDeleteRequest request) {
        String keyLog = StringQlscUtils.generate(6);
        ApiResponse<?> response = new ApiResponse<>();
        LOG.info("{} Start api delete court price rule req = {} ", keyLog, request);

        LOG.info("{} End api delete court price rule req = {}", keyLog, request);
        return response;
    }
}
