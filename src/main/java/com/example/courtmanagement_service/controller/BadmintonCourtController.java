package com.example.courtmanagement_service.controller;


import com.example.courtmanagement_service.dto.BadmintonInfoTimeDTO;
import com.example.courtmanagement_service.request.BadmintonCourtRequest;
import com.example.courtmanagement_service.request.base_request.BaseDeleteRequest;
import com.example.courtmanagement_service.service.BadmintonCourtService;
import com.qlsc.qlsc_common.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/badminton-courts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BadmintonCourtController {
    BadmintonCourtService service;

    Logger LOG = LoggerFactory.getLogger(BadmintonCourtController.class);


    @GetMapping("/test-get-specification")
    public ApiResponse<?> getTestGet(@RequestParam String email,
                                     @RequestParam Integer status,
                                     @RequestParam Integer start,
                                     @RequestParam Integer limit) {
        LOG.info("Start API test get: {}, {}", email, status);
        ApiResponse<?> response = service.getLstBadmintonCourt(email, status, start, limit);
        LOG.info("End API test get");
        return response;
    }

    @GetMapping("/get-all")
    public ApiResponse<?> getAll() {
        LOG.info("Start API get all badminton courts");
        ApiResponse<?> response = service.getAll();
        LOG.info("End API get all badminton courts");
        return response;
    }

    @GetMapping("/get-by-id")
    public ApiResponse<?> getById(@RequestParam Integer id) {
        LOG.info("Start API get badminton court by id: {}", id);
        ApiResponse<?> response = service.getById(id);
        LOG.info("End API get badminton court by id: {}", id);
        return response;
    }

    @PostMapping("/create")
    public ApiResponse<?> create(@RequestBody BadmintonCourtRequest request) {
        LOG.info("Start API create badminton court with data: {}", request);
        ApiResponse<?> response = service.create(request);
        LOG.info("End API create badminton court");
        return response;
    }

    @PutMapping("/update")
    public ApiResponse<?> update(@RequestParam Integer id,
                                 @RequestBody BadmintonCourtRequest request) {
        LOG.info("Start API update badminton court id: {} with data: {}", id, request);
        ApiResponse<?> response = service.update(id, request);
        LOG.info("End API update badminton court id: {}", id);
        return response;
    }

    @DeleteMapping("/delete")
    public ApiResponse<?> delete(@RequestParam Integer id) throws ChangeSetPersister.NotFoundException {
        LOG.info("Start API delete badminton court by id: {}", id);
        ApiResponse<?> response = service.delete(id);
        LOG.info("End API delete badminton court by id: {}", id);
        return response;
    }

    @PostMapping("/update-status")
    public ApiResponse<?> updateStatus(@RequestBody BaseDeleteRequest request)  {
        LOG.info("Start API delete badminton court by request : {}", request);
        ApiResponse<?> response = service.updateStatus(request);
        LOG.info("End API delete badminton court by request: {}", request);
        return response;
    }

    @GetMapping("/get-info-time-court-detail")
    public ApiResponse<List<BadmintonInfoTimeDTO>> getInfoTimeCourtDetail(@RequestParam List<Integer> courtIds) {
        LOG.info("Start API get time badminton court by courtIds : {}", courtIds);
        ApiResponse<List<BadmintonInfoTimeDTO>> response = service.getInfoTimeCourtDetail(courtIds);
        LOG.info("End API get time badminton court by courtIds : {}", courtIds);
        return response;
    }




}
