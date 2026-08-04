package com.example.demo.dto.booking;



public record BookingResponse(

        Long bookingId,

        String coachName,

        Integer seatNumber,

        String originStation,

        String destinationStation,

        Double fare

) {

}