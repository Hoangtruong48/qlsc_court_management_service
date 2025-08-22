package com.example.courtmanagement_service.service;

import com.example.courtmanagement_service.dto.BadmintonInfoTimeDTO;
import com.example.courtmanagement_service.entity.BadmintonCourt;
import com.example.courtmanagement_service.repo.jpa.BadmintonCourtRepoJpa;
import com.example.courtmanagement_service.repo.jpa.CourtDetailRepoJpa;
import com.example.courtmanagement_service.request.BadmintonCourtRequest;
import com.example.courtmanagement_service.request.base_request.BaseDeleteRequest;
import com.example.courtmanagement_service.response.BadmintonCourtResponse;
import com.example.courtmanagement_service.specification.BaseSpecificationBuilder;
import com.example.courtmanagement_service.specification.SearchOperation;
import com.example.courtmanagement_service.util.PageRequestUtils;
import com.qlsc.qlsc_common.constant.BadmintonConstant;
import com.qlsc.qlsc_common.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
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
        List<BadmintonCourtResponse> list = badmintonCourtRepoJpa
                .findAllByStatusIs(BadmintonConstant.STATUS_ON).orElseGet(ArrayList::new)
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
        return badmintonCourtRepoJpa.findByIdAndStatusIs(id, BadmintonConstant.STATUS_ON)
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

    public ApiResponse<?> getLstBadmintonCourt(String email, Integer status,
                                               Integer start, Integer limit) {
        BaseSpecificationBuilder<BadmintonCourt> builder = new BaseSpecificationBuilder<>();
        builder.with(BadmintonCourt.COLUMN_TO_FIELD_MAP.getOrDefault(BadmintonCourt.BadmintonCourtConstant.CONTACT_EMAIL, ""), SearchOperation.LIKE, email, String.class);
        builder.with(BadmintonCourt.COLUMN_TO_FIELD_MAP.getOrDefault(BadmintonCourt.BadmintonCourtConstant.STATUS, ""), SearchOperation.GREATER_THAN_OR_EQUAL, status, Integer.class);
        Specification<BadmintonCourt> spec = builder.build();
        PageRequest pageRequest = PageRequestUtils.buildPageRequest(start, limit);

        Page<BadmintonCourt> lst =  badmintonCourtRepoJpa.findAll(spec, pageRequest);
        return ApiResponse.builder()
                .data(lst.getContent())
                .total(lst.getTotalElements())
                .statusCode(0)
                .build();

    }

    public ApiResponse<?> updateStatus(BaseDeleteRequest request) {
        ApiResponse<Integer> response = new ApiResponse<>();
        String msgError = request.validate();
        if (msgError != null) {
            return response.setMessageFailed(msgError);
        }
        long countRecord = badmintonCourtRepoJpa.countAllByIdIn(request.getIds());
        if (countRecord != request.getIds().size()) {
            return response.setMessageFailed("BadmintonCourt update failed because item not exists");
        }
        long total = badmintonCourtRepoJpa.updateStatusBadmintonCourtByIdIn(request.getIds(), request.getStatus());
        LOG.info("Total record badminton court updated = {}", total);

        long totalUpdateCourtDetail = courtDetailRepository.updateStatusCourtDetailByCourtIdIn(
                request.getIds(), request.getStatus());

        LOG.info("Total record court detail updated = {}", totalUpdateCourtDetail);

        return response.setMessageSuccess("Update successfully " + total + "badminton court" + "and "
                + totalUpdateCourtDetail + "court detail");

    }

    public ApiResponse<List<BadmintonInfoTimeDTO>> getInfoTimeCourtDetail(List<Integer> ids) {
        LOG.info("Start getInfoTimeCourtDetail");
        List<Object[]> rawData = badmintonCourtRepoJpa.getInfoTimeCourtDetailByListCourtId(ids);
        LOG.info("rawData size = {}", rawData.size());
        List<BadmintonInfoTimeDTO> lstData = BadmintonInfoTimeDTO.convertRawDataToDTO(rawData);
        LOG.info("End getInfoTimeCourtDetail");
        return ApiResponse.<List<BadmintonInfoTimeDTO>>builder()
                .data(lstData)
                .total((long) lstData.size())
                .statusCode(0)
                .build();
    }
}
