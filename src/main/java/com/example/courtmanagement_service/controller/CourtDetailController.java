package com.example.courtmanagement_service.controller;


import com.example.courtmanagement_service.request.CourtDetailRequest;
import com.example.courtmanagement_service.response.CourtDetailResponse;
import com.example.courtmanagement_service.service.CourtDetailService;
import com.qlsc.qlsc_common.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/court-details")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourtDetailController {
    CourtDetailService service;
    Logger LOG = LoggerFactory.getLogger(BadmintonCourtController.class);

    @GetMapping
    public ApiResponse<List<CourtDetailResponse>> getAll() {
        LOG.info("Start API get all court details");
        ApiResponse<List<CourtDetailResponse>> response = service.getAll();
        LOG.info("End API get all court details");
        return response;
    }

    @GetMapping("/by-id")
    public ApiResponse<CourtDetailResponse> getById(@RequestParam Integer id) {
        LOG.info("Start API get court detail by id: {}", id);
        ApiResponse<CourtDetailResponse> response = service.getById(id);
        LOG.info("End API get court detail by id: {}", id);
        return response;
    }

    @PostMapping
    public ApiResponse<CourtDetailResponse> create(@RequestBody CourtDetailRequest request) {
        LOG.info("Start API create court detail with data: {}", request);
        ApiResponse<CourtDetailResponse> response = service.create(request);
        LOG.info("End API create court detail");
        return response;
    }

    @PutMapping
    public ApiResponse<CourtDetailResponse> update(@RequestParam Integer id,
                                                   @RequestBody CourtDetailRequest request) {
        LOG.info("Start API update court detail id: {} with data: {}", id, request);
        ApiResponse<CourtDetailResponse> response = service.update(id, request);
        LOG.info("End API update court detail id: {}", id);
        return response;
    }

    @DeleteMapping
    public ApiResponse<?> delete(@RequestParam Integer id) {
        LOG.info("Start API delete court detail by id: {}", id);
        ApiResponse<?> response = service.delete(id);
        LOG.info("End API delete court detail by id: {}", id);
        return response;
    }
}
