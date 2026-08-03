package com.example.demo.controler;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.example.demo.dto.schedule.TrainScheduleResponse;
import com.example.demo.service.TrainScheduleService;
import java.util.List;
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class TrainScheduleController {

    private final TrainScheduleService scheduleService;

    @GetMapping
    public List<TrainScheduleResponse> getSchedules(

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return scheduleService.getSchedules(date);

    }
}