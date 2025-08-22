package com.example.courtmanagement_service.service;

import com.example.courtmanagement_service.dto.BadmintonInfoTimeDTO;
import com.example.courtmanagement_service.entity.CourtDetail;
import com.example.courtmanagement_service.repo.jpa.CourtDetailRepoJpa;
import com.example.courtmanagement_service.request.CourtDetailRequest;
import com.example.courtmanagement_service.request.base_request.BaseDeleteRequest;
import com.example.courtmanagement_service.response.BadmintonCourtResponse;
import com.example.courtmanagement_service.response.CourtDetailResponse;
import com.qlsc.qlsc_common.constant.BadmintonConstant;
import com.qlsc.qlsc_common.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourtDetailService {
    CourtDetailRepoJpa repository;
    Logger LOG = LoggerFactory.getLogger(this.getClass());

    public ApiResponse<List<CourtDetailResponse>> getAll() {
        List<CourtDetailResponse> list = repository
                .findAllByStatusIs(BadmintonConstant.STATUS_ON).orElse(new ArrayList<>())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<CourtDetailResponse>>builder()
                .statusCode(0)
                .message("Success")
                .data(list)
                .build();
    }

    public ApiResponse<CourtDetailResponse> getById(Integer id) {
        return repository.findByIdAndStatusIs(id, BadmintonConstant.STATUS_ON)
                .map(detail -> ApiResponse.<CourtDetailResponse>builder()
                        .statusCode(0)
                        .message("Success")
                        .data(toResponse(detail))
                        .build())
                .orElseGet(() -> {
                    ApiResponse<CourtDetailResponse> res = new ApiResponse<>();
                    res.setMessageFailed("Court detail not found");
                    return res;
                });
    }

    public ApiResponse<CourtDetailResponse> create(CourtDetailRequest request) {
        ApiResponse<CourtDetailResponse> response = new ApiResponse<>();

        String error = request.validate();
        if (StringUtils.hasLength(error)) {
            response.setMessageFailed(error);
            return response;
        }

        CourtDetail detail = request.toEntity();
        repository.save(detail);

        response.setDataSuccess(toResponse(detail));
        return response;
    }

    public ApiResponse<CourtDetailResponse> update(Integer id, CourtDetailRequest request) {
        ApiResponse<CourtDetailResponse> response = new ApiResponse<>();

        String error = request.validate();
        if (StringUtils.hasLength(error)) {
            response.setMessageFailed(error);
            return response;
        }

        CourtDetail detail = repository.findById(id)
                .orElse(null);
        if (detail == null) {
            response.setMessageFailed("Court detail not found");
            return response;
        }

        // Map request sang entity
        detail.setCourtId(request.getCourtId());
        detail.setCourtNumber(request.getCourtNumber());
        detail.setCourtType(request.getCourtType());
        detail.setPricePerHour(request.getPricePerHouse());
        detail.setUpdatedAt(Instant.now().toEpochMilli());

        repository.save(detail);

        response.setDataSuccess(toResponse(detail));
        return response;
    }

    public ApiResponse<?> delete(Integer id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (!repository.existsById(id)) {
            response.setMessageFailed("Court detail not found");
            return response;
        }
        repository.deleteById(id);
        response.setMessageSuccess("Court detail deleted successfully");
        return response;
    }

    private CourtDetailResponse toResponse(CourtDetail detail) {
        return CourtDetailResponse.builder()
                .id(detail.getId())
                .courtId(detail.getCourtId())
                .courtNumber(detail.getCourtNumber())
                .courtType(detail.getCourtType())
                .pricePerHouse(detail.getPricePerHour())
                .createdAt(detail.getCreatedAt())
                .updatedAt(detail.getUpdatedAt())
                .build();
    }

    public ApiResponse<?> updateStatusCourtDetail(BaseDeleteRequest request) {
        ApiResponse<Integer> response = new ApiResponse<>();
        String msgError = request.validate();
        if (msgError != null) {
            return response.setMessageFailed(msgError);
        }
        long countRecord = repository.countAllByIdIn(request.getIds());
        if (countRecord != request.getIds().size()) {
            return response.setMessageFailed("BadmintonCourt update failed because item not exists");
        }

        long totalUpdateCourtDetail = repository.updateStatusCourtDetailByCourtIdIn(
                request.getIds(), request.getStatus());

        LOG.info("Total record court detail updated = {}", totalUpdateCourtDetail);

        return response.setMessageSuccess("Update successfully " + totalUpdateCourtDetail + "court detail");
    }

//    public ApiResponse<List<BadmintonInfoTimeDTO>> getInfoTimeCourtDetail(List<Integer> ids) {
//        LOG.info("Start getInfoTimeCourtDetail");
//        List<Object[]> rawData = repository.getInfoTimeCourtDetailByListCourtId(ids);
//        List<BadmintonInfoTimeDTO> lstData = BadmintonInfoTimeDTO.convertRawDataToDTO(rawData);
//        LOG.info("End getInfoTimeCourtDetail");
//        return ApiResponse.<List<BadmintonInfoTimeDTO>>builder()
//                .data(lstData)
//                .total((long) lstData.size())
//                .statusCode(0)
//                .build();
//    }
}
