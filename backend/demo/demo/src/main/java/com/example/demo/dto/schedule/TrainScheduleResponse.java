package com.example.demo.dto.schedule;

import java.time.LocalDate;
import java.time.LocalTime;

public record TrainScheduleResponse(

        Long scheduleId,

        String trainNo,

        String trainName,

        LocalDate travelDate,

        LocalTime departureTime,

        String status

) {}
