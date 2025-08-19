package com.example.courtmanagement_service.service;

import com.example.courtmanagement_service.entity.BadmintonCourt;
import com.example.courtmanagement_service.repo.jpa.BadmintonCourtRepoJpa;
import com.example.courtmanagement_service.repo.jpa.CourtDetailRepoJpa;
import com.example.courtmanagement_service.request.BadmintonCourtRequest;
import com.example.courtmanagement_service.response.BadmintonCourtResponse;
import com.qlsc.qlsc_common.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BadmintonCourtService {

    BadmintonCourtRepoJpa badmintonCourtRepoJpa;
    CourtDetailRepoJpa courtDetailRepository;
    Logger LOG = LoggerFactory.getLogger(this.getClass());

    public ApiResponse<List<BadmintonCourtResponse>> getAll() {
        List<BadmintonCourtResponse> list = badmintonCourtRepoJpa.findAll()
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
        return badmintonCourtRepoJpa.findById(id)
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
        badmintonCourtRepoJpa.save(court);

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

        BadmintonCourt court = badmintonCourtRepoJpa.findById(id)
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

        badmintonCourtRepoJpa.save(court);

        response.setDataSuccess(toResponse(court));
        return response;
    }

    public ApiResponse<?> delete(Integer id) throws ChangeSetPersister.NotFoundException {
        ApiResponse<?> response = new ApiResponse<>();
        if (!badmintonCourtRepoJpa.existsById(id)) {
            response.setMessageFailed("Court not found");
            return response;
        }
        BadmintonCourt court = badmintonCourtRepoJpa.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        int totalCourtDetail = courtDetailRepository.deleteByCourtId(court.getId());
        LOG.info("Court deleted successfully court detail size = {}", totalCourtDetail);
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
