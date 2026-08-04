package com.example.demo.dto.booking;

import java.time.LocalDate;

public record BookingHistoryResponse(

        Long bookingId,

        LocalDate travelDate,

        String trainName,

        String coachName,

        Integer seatNumber,

        String origin,

        String destination,

        Double fare

       

) {
}