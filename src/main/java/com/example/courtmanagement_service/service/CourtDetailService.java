package com.example.courtmanagement_service.service;

import com.example.courtmanagement_service.entity.CourtDetail;
import com.example.courtmanagement_service.repo.jpa.CourtDetailRepoJpa;
import com.example.courtmanagement_service.request.CourtDetailRequest;
import com.example.courtmanagement_service.response.CourtDetailResponse;
import com.qlsc.qlsc_common.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourtDetailService {
    CourtDetailRepoJpa repository;

    public ApiResponse<List<CourtDetailResponse>> getAll() {
        List<CourtDetailResponse> list = repository.findAll()
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
        return repository.findById(id)
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
}
