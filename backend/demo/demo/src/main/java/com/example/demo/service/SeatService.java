package com.example.demo.service;
import java.util.List;
import com.example.demo.dto.seat.SeatResponse;
public interface SeatService {

    List<SeatResponse> getAvailableSeats(

            Long scheduleId,

            Long originStationId,

            Long destinationStationId

    );

}
