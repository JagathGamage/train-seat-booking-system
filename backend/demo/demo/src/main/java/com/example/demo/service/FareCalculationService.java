package com.example.demo.service;


import com.example.demo.entity.Seat;
import com.example.demo.entity.Station;
import com.example.demo.entity.TrainSchedule;

public interface FareCalculationService {

    double calculateFare(
            TrainSchedule schedule,
            Seat seat,
            Station origin,
            Station destination
    );

}
