package com.example.courtmanagement_service.service.generate_data;

import com.example.quanlysancau.entity.BadmintonCourt;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BadmintonCourtGeneratorService {
    private static final String[] NAMES = {
            "Hoàng Trường Badminton", "Cầu Lông 999", "Thể Thao Xanh", "VietSport Badminton",
            "Sunshine Court", "Lucky Feather", "Smash King"
    };

    private static final String[] DISTRICTS = {
            "Hoàng Mai - Hà Nội", "Cầu Giấy - Hà Nội", "Tây Hồ - Hà Nội",
            "Long Biên - Hà Nội", "Hai Bà Trưng - Hà Nội", "Đống Đa - Hà Nội"
    };

    private static final String[] FLOOR_MATERIALS = {
            "Sàn gỗ", "Sàn nhựa", "Sàn tổng hợp"
    };

    private static final String[] LIGHTING_TYPES = {
            "Đèn chống lóa", "Đèn LED cao cấp", "Đèn huỳnh quang"
    };

    public BadmintonCourt generateOne(int index) {
        Random random = new Random();
        String suffix = String.format(" #%d-%s", index + 1, randomAlpha(3));
        int numberCourt = 8 + random.nextInt(8);
        return BadmintonCourt.builder()
                .name("Sân cầu " + NAMES[random.nextInt(NAMES.length)] + suffix)
                .address(DISTRICTS[random.nextInt(DISTRICTS.length)])
                .description("Sân cầu được thiết kế với " +
                        numberCourt + " sân, sử dụng công nghệ mới, " +
                        "đèn sáng tiêu chuẩn, có nhà tắm, canteen")
                .openingTime(randomOpeningTime())
                .closingTime(randomClosingTime())
                .totalCourts(numberCourt)
                .contactPhone("09" + (10000000 + random.nextInt(90000000)))
                .contactEmail("contact" + index + "@badminton.vn")
                .floorMaterial(FLOOR_MATERIALS[random.nextInt(FLOOR_MATERIALS.length)])
                .lightingType(LIGHTING_TYPES[random.nextInt(LIGHTING_TYPES.length)])
                .hasShower(random.nextBoolean())
                .hasParking(random.nextBoolean())
                .hasDrinksService(random.nextBoolean())
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .build();
    }

    private static String randomAlpha(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private static String randomOpeningTime() {
        int hour = ThreadLocalRandom.current().nextInt(5, 9);
        int minute = ThreadLocalRandom.current().nextInt(0, 2) * 30;
        return String.format("%dh%02d", hour, minute);
    }

    private static String randomClosingTime() {
        int hour = ThreadLocalRandom.current().nextInt(21, 24);
        int minute = ThreadLocalRandom.current().nextInt(0, 2) * 30;
        return String.format("%dh%02d", hour, minute);
    }
}
