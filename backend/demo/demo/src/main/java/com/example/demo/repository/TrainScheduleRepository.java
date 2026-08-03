package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.demo.entity.TrainSchedule;

import java.time.LocalDate;
import java.util.List;


public interface TrainScheduleRepository extends JpaRepository<TrainSchedule, Long> {

    List<TrainSchedule> findByTravelDate(LocalDate travelDate);

}