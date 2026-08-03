package com.example.demo.service.impl;



import java.time.LocalDate;

import com.example.demo.dto.schedule.TrainScheduleResponse;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.example.demo.entity.TrainSchedule;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.TrainScheduleRepository;
import com.example.demo.service.TrainScheduleService;
import java.util.List;
@Service
@RequiredArgsConstructor
public class TrainScheduleServiceImpl implements TrainScheduleService {

    private final TrainScheduleRepository scheduleRepository;

    @Override
    public List<TrainScheduleResponse> getSchedules(LocalDate travelDate) {

        List<TrainSchedule> schedules =
                scheduleRepository.findByTravelDate(travelDate);

        if (schedules.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No train schedules found for " + travelDate);
        }

        return schedules.stream()
                .map(schedule -> new TrainScheduleResponse(

                        schedule.getId(),

                        schedule.getTrain().getTrainNo(),

                        schedule.getTrain().getTrainName(),

                        schedule.getTravelDate(),

                        schedule.getDepartureTime(),

                        schedule.getStatus().name()

                ))
                .toList();
    }
}