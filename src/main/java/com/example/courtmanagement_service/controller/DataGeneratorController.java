package com.example.courtmanagement_service.controller;


import com.example.courtmanagement_service.entity.BadmintonCourt;
import com.example.courtmanagement_service.repo.BadmintonCourtRepository;
import com.example.courtmanagement_service.service.generate_data.BadmintonCourtGeneratorService;

import com.qlsc.qlsc_common.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/dev-tools")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataGeneratorController {
    BadmintonCourtGeneratorService generatorService;
    BadmintonCourtRepository repository;

    @PostMapping("/generate-badminton-courts")
    public ApiResponse<?> generateCourts(@RequestParam(defaultValue = "10") int count) {
        List<BadmintonCourt> courts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            courts.add(generatorService.generateOne(i));
        }
        repository.saveAll(courts);
        return ApiResponse.builder()
                .data(courts)
                .message("Success")
                .statusCode(0)
                .build();
    }
}
