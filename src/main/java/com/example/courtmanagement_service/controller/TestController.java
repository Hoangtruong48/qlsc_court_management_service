package com.example.courtmanagement_service.controller;

import com.qlsc.qlsc_common.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/test")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TestController {

    public static ApiResponse<?> test() {
        int n = new Random().nextInt(100);
        return ApiResponse.<Integer>builder().data(n).build();
    }

    public static void main(String[] args) {
        for (int i = 0;  i < (1 << 25); i++) {
            System.out.println(test());
        }
    }
}
