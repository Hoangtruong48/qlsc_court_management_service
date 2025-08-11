package com.example.courtmanagement_service.service;

import com.example.courtmanagement_service.entity.BadmintonCourt;
import com.example.courtmanagement_service.repo.BadmintonCourtRepository;
import com.example.courtmanagement_service.request.BadmintonCourtRequest;
import com.example.courtmanagement_service.response.BadmintonCourtResponse;
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
public class BadmintonCourtService {

    BadmintonCourtRepository repository;

    public ApiResponse<List<BadmintonCourtResponse>> getAll() {
        List<BadmintonCourtResponse> list = repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<BadmintonCourtResponse>>builder()
                .statusCode(0)
                .message("Success")
                .data(list)
                .build();
    }

    public ApiResponse<BadmintonCourtResponse> getById(Integer id) {
        return repository.findById(id)
                .map(court -> ApiResponse.<BadmintonCourtResponse>builder()
                        .statusCode(0)
                        .message("Success")
                        .data(toResponse(court))
                        .build())
                .orElseGet(() -> {
                    ApiResponse<BadmintonCourtResponse> res = new ApiResponse<>();
                    res.setMessageFailed("Court not found");
                    return res;
                });
    }

    public ApiResponse<BadmintonCourtResponse> create(BadmintonCourtRequest request) {
        ApiResponse<BadmintonCourtResponse> response = new ApiResponse<>();

        String error = request.validate();
        if (StringUtils.hasLength(error)) {
            response.setMessageFailed(error);
            return response;
        }

        BadmintonCourt court = request.toEntity();
        repository.save(court);

        response.setDataSuccess(toResponse(court));
        return response;
    }

    public ApiResponse<BadmintonCourtResponse> update(Integer id, BadmintonCourtRequest request) {
        ApiResponse<BadmintonCourtResponse> response = new ApiResponse<>();

        String error = request.validate();
        if (StringUtils.hasLength(error)) {
            response.setMessageFailed(error);
            return response;
        }

        BadmintonCourt court = repository.findById(id)
                .orElse(null);
        if (court == null) {
            response.setMessageFailed("Court not found");
            return response;
        }

        // Map request sang entity
        court.setName(request.getName());
        court.setAddress(request.getAddress());
        court.setDescription(request.getDescription());
        court.setOpeningTime(request.getOpeningTime());
        court.setClosingTime(request.getClosingTime());
        court.setTotalCourts(request.getTotalCourts());
        court.setContactPhone(request.getContactPhone());
        court.setContactEmail(request.getContactEmail());
        court.setFloorMaterial(request.getFloorMaterial());
        court.setLightingType(request.getLightingType());
        court.setHasShower(request.getHasShower());
        court.setHasParking(request.getHasParking());
        court.setHasDrinksService(request.getHasDrinksService());
        court.setUpdatedAt(Instant.now().toEpochMilli());

        repository.save(court);

        response.setDataSuccess(toResponse(court));
        return response;
    }

    public ApiResponse<?> delete(Integer id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (!repository.existsById(id)) {
            response.setMessageFailed("Court not found");
            return response;
        }
        repository.deleteById(id);
        response.setMessageSuccess("Court deleted successfully");
        return response;
    }

    private BadmintonCourtResponse toResponse(BadmintonCourt court) {
        return BadmintonCourtResponse.builder()
                .id(court.getId())
                .name(court.getName())
                .address(court.getAddress())
                .description(court.getDescription())
                .openingTime(court.getOpeningTime())
                .closingTime(court.getClosingTime())
                .totalCourts(court.getTotalCourts())
                .contactPhone(court.getContactPhone())
                .contactEmail(court.getContactEmail())
                .floorMaterial(court.getFloorMaterial())
                .lightingType(court.getLightingType())
                .hasShower(court.getHasShower())
                .hasParking(court.getHasParking())
                .hasDrinksService(court.getHasDrinksService())
                .createdAt(court.getCreatedAt())
                .updatedAt(court.getUpdatedAt())
                .build();
    }
}
