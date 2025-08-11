package com.example.courtmanagement_service.controller;

import com.example.quanlysancau.request.BadmintonCourtRequest;
import com.example.quanlysancau.response.ApiResponse;
import com.example.quanlysancau.service.BadmintonCourtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/badminton-courts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BadmintonCourtController {
    BadmintonCourtService service;

    @GetMapping
    public ApiResponse<?> getAll() {
        return service.getAll();
    }

    @GetMapping("/get-by-id")
    public ApiResponse<?> getById(@RequestParam Integer id) {
        return service.getById(id);
    }

    @PostMapping("/create")
    public ApiResponse<?> create(@RequestBody BadmintonCourtRequest request) {
        return service.create(request);
    }

    @PutMapping("/update")
    public ApiResponse<?> update(@RequestParam Integer id,
                                 @RequestBody BadmintonCourtRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/delete")
    public ApiResponse<?> delete(@RequestParam Integer id) {
        return service.delete(id);
    }
}
