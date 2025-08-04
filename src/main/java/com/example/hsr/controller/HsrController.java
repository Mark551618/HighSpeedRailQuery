package com.example.hsr.controller;

import com.example.hsr.model.TrainSchedule;
import com.example.hsr.service.HsrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hsr")
public class HsrController {

    @Autowired
    private HsrService hsrService;

    @GetMapping("/schedule")
    public List<TrainSchedule> getSchedule(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String date,
            @RequestParam(required = false) String time
    ) {
        return hsrService.querySchedule(from, to, date, time);
    }

}