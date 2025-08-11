package com.example.courtmanagement_service.controller;

import com.example.quanlysancau.request.CourtDetailRequest;
import com.example.quanlysancau.response.ApiResponse;
import com.example.quanlysancau.response.CourtDetailResponse;
import com.example.quanlysancau.service.CourtDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/court-details")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourtDetailController {
    CourtDetailService service;

    @GetMapping
    public ApiResponse<List<CourtDetailResponse>> getAll() {
        return service.getAll();
    }

    @GetMapping("/by-id")
    public ApiResponse<CourtDetailResponse> getById(@RequestParam Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public ApiResponse<CourtDetailResponse> create(@RequestBody CourtDetailRequest request) {
        return service.create(request);
    }

    @PutMapping
    public ApiResponse<CourtDetailResponse> update(@RequestParam Integer id,
                                                   @RequestBody CourtDetailRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping
    public ApiResponse<?> delete(@RequestParam Integer id) {
        return service.delete(id);
    }
}
