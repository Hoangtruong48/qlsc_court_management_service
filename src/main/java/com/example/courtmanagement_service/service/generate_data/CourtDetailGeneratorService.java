package com.example.courtmanagement_service.service.generate_data;

import com.example.courtmanagement_service.dto.CourtDetailCountDTO;
import com.example.courtmanagement_service.entity.BadmintonCourt;
import com.example.courtmanagement_service.entity.CourtDetail;
import com.example.courtmanagement_service.repo.jpa.BadmintonCourtRepoJpa;
import com.example.courtmanagement_service.repo.jpa.CourtDetailRepoJpa;
import com.qlsc.qlsc_common.constant.BadmintonConstant;
import com.qlsc.qlsc_common.response.ApiResponse;
import com.qlsc.qlsc_common.util.NumberUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CourtDetailGeneratorService {
    private static final Random rand = new Random();
    private static final double MIN_NORMAL = 60.000;
    private static final double MAX_NORNAL = 100.000;
    private static final double MIN_VIP = 80.000;
    private static final double MAX_VIP = 120.000;
    private static final double BASE_ROUND_NUMBER = 5.000;

    CourtDetailRepoJpa courtDetailRepoJpa;
    BadmintonCourtRepoJpa badmintonCourtRepoJpa;
    Logger LOG = LoggerFactory.getLogger(this.getClass());

    public List<CourtDetail> generateListCourtDetailFromBadmintonCourt
            (BadmintonCourt badmintonCourt) {
        List<CourtDetail> lst = new ArrayList<>();
        int totalCourt = badmintonCourt.getTotalCourts();
        int courtId = badmintonCourt.getId();
        int numberCourt = 1;
        int size = BadmintonConstant.listCourtType.size();
        double pricePerHouse = 0.0;
        int countType = 0;
        int status = BadmintonConstant.STATUS_ON;

        long timeNow = System.currentTimeMillis();
        for (int i = 0; i < totalCourt; i++) {
            countType = BadmintonConstant.listCourtType.get(rand.nextInt(size));
            if (countType == BadmintonConstant.TYPE_NORMAL) {
                pricePerHouse = rand.nextDouble(MIN_NORMAL, MAX_NORNAL);
            } else if (countType == BadmintonConstant.TYPE_VIP) {
                pricePerHouse = rand.nextDouble(MIN_VIP, MAX_VIP);
            }
            pricePerHouse = NumberUtils.roundToNearestBase(pricePerHouse, BASE_ROUND_NUMBER);
            lst.add(
                    CourtDetail.builder()
                            .courtId(courtId)
                            .courtNumber(String.valueOf(numberCourt++))
                            .courtType(countType)
                            .pricePerHour(pricePerHouse)
                            .status(status)
                            .createdAt(timeNow)
                            .createdBy("GENERATE DATA")
                            .build()
            );
        }
        return lst;
    }

    public ApiResponse<?> generateCourtDetail(int numberCourtGen) {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
//        List<Object[]> dataRaws = courtDetailRepoJpa.getTotalCourtDetailByCourtId();
//        List<CourtDetailCountDTO> results = dataRaws.stream()
//                .map(row -> new CourtDetailCountDTO(
//                        ((Number) row[0]).intValue(),
//                        ((Number) row[1]).longValue()))
//                .toList();
        List<Integer> courtIds = new ArrayList<>();
//        for (CourtDetailCountDTO courtDetailCountDTO : results) {
//            if (courtDetailCountDTO.getTotal() == 0) {
//                if (courtIds.size() >= numberCourtGen) {
//                    break;
//                }
//                courtIds.add(courtDetailCountDTO.getCourtId());
//            }
//        }
//        if (results.isEmpty()) {
        courtIds = badmintonCourtRepoJpa.getCourtIdNotExistInCourtDetail();
        LOG.info("Số sân cầu gen request = {}, số sân cầu còn lại gen = {}", numberCourtGen, courtIds.size());
        numberCourtGen = Math.min(numberCourtGen, courtIds.size());
        courtIds = courtIds.subList(0, numberCourtGen);
//        }


        List<BadmintonCourt> lstBadmintonCourt = badmintonCourtRepoJpa.findAllByIdIn(courtIds).orElse(null);
        if (lstBadmintonCourt == null) {
            apiResponse.setDataSuccess(0L);
            apiResponse.setError();
            return apiResponse;
        }
        List<CourtDetail> courtDetailList = new ArrayList<>();
        for (BadmintonCourt x : lstBadmintonCourt) {
            List<CourtDetail> temp = generateListCourtDetailFromBadmintonCourt(x);
            courtDetailList.addAll(temp);
        }
        courtDetailRepoJpa.saveAll(courtDetailList);
        apiResponse.setDataSuccess((long) courtDetailList.size());
        return apiResponse;
    }
}
