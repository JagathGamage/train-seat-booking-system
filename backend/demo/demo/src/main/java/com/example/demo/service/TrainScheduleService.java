package com.example.demo.service;


import java.time.LocalDate;

import com.example.demo.dto.schedule.TrainScheduleResponse;
import java.util.List;

   
public interface TrainScheduleService {

    List<TrainScheduleResponse> getSchedules(LocalDate travelDate);

}
