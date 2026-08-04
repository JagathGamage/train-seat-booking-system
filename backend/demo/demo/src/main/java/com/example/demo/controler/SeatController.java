package com.example.demo.controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.service.SeatService;
import com.example.demo.dto.seat.SeatResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/available")
    public List<SeatResponse> getAvailableSeats(

            @RequestParam Long scheduleId,

            @RequestParam Long originStationId,

            @RequestParam Long destinationStationId

    ){

        return seatService.getAvailableSeats(

                scheduleId,

                originStationId,

                destinationStationId

        );

    }

}
