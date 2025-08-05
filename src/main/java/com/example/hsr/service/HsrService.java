package com.example.hsr.service;

import com.example.hsr.model.TrainSchedule;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HsrService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RestTemplate restTemplate;

    public List<TrainSchedule> querySchedule(String from, String to, String date, String time) {
        List<TrainSchedule> schedules = fetchFromTdx(from, to, date);  // 從 TDX 拿所有班次

        if (time == null || time.isBlank()) {
            return schedules; // 沒有時間條件就回傳全部
        }

        // 解析查詢時間（HH:mm 格式），失敗就直接回空清單
        LocalTime inputTime;
        try {
            inputTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("時間格式錯誤：" + time);
            return List.of(); // 也可以改成 return schedules;
        }

        // 根據出發時間過濾班次
        return schedules.stream()
                .filter(schedule -> {
                    try {
                        String departureTime = schedule.getDepartureTime();
                        if (departureTime == null || departureTime.isBlank()) {
                            return false;
                        }
                        LocalTime trainTime = LocalTime.parse(departureTime, DateTimeFormatter.ofPattern("HH:mm"));
                        return !trainTime.isBefore(inputTime); // trainTime >= inputTime
                    } catch (Exception e) {
                        System.out.println("班次時間格式錯誤: " + schedule.getDepartureTime());
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    private List<TrainSchedule> fetchFromTdx(String from, String to, String date) {
        String accessToken = tokenService.getAccessToken();

        String url = String.format(
                "https://tdx.transportdata.tw/api/basic/v2/Rail/THSR/DailyTimetable/OD/%s/to/%s/%s?$format=JSON",
                from, to, date
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        String json = response.getBody();

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode;
        try {
            arrayNode = (ArrayNode) mapper.readTree(json);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "解析 TDX 回傳資料失敗", e);
        }

        List<TrainSchedule> scheduleList = new ArrayList<>();

        for (JsonNode node : arrayNode) {
            TrainSchedule schedule = new TrainSchedule();

            schedule.setTrainNo(node.path("DailyTrainInfo").path("TrainNo").asText());
            schedule.setStartStationName(node.path("OriginStopTime").path("StationName").path("Zh_tw").asText());
            schedule.setDepartureTime(node.path("OriginStopTime").path("DepartureTime").asText());

            schedule.setEndStationName(node.path("DestinationStopTime").path("StationName").path("Zh_tw").asText());
            schedule.setArrivalTime(node.path("DestinationStopTime").path("ArrivalTime").asText());

            scheduleList.add(schedule);

        }

        return scheduleList;
    }

    private LocalTime parseTimeStrict(String timeStr) {
        try {
            return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "時間格式錯誤，應為 HH:mm，例如 14:43");
        }
    }
}


