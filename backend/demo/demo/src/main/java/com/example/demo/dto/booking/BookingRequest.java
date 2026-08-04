package com.example.demo.dto.booking;

public record BookingRequest(

        Long scheduleId,

        Long seatId,

        Long originStationId,

        Long destinationStationId,

        String passengerName,

        String passengerNic

) {

}