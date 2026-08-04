package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.entity.CoachType;
import com.example.demo.entity.Seat;
import com.example.demo.entity.Station;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.SeatRepository;
import com.example.demo.repository.StationRepository;
import com.example.demo.service.SeatService;
import com.example.demo.dto.seat.SeatResponse;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final StationRepository stationRepository;

    @Override
    public List<SeatResponse> getAvailableSeats(
            Long scheduleId,
            Long originStationId,
            Long destinationStationId) {

        Station origin = stationRepository.findById(originStationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Origin station not found"));

        Station destination = stationRepository.findById(destinationStationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Destination station not found"));

        int newOrigin = origin.getStationOrder();
        int newDestination = destination.getStationOrder();

        if (newOrigin >= newDestination) {
            throw new IllegalArgumentException(
                    "Origin station must come before destination station.");
        }

        List<Seat> allReservedSeats =
                seatRepository.findByCoachCoachType(CoachType.RESERVED);

        Set<Long> conflictingSeatIds = new HashSet<>(
                bookingRepository.findConflictingSeatIds(
                        scheduleId,
                        newOrigin,
                        newDestination));

        return allReservedSeats.stream()
                .filter(seat -> !conflictingSeatIds.contains(seat.getId()))
                .map(seat -> new SeatResponse(
                        seat.getId(),
                        seat.getCoach().getCoachName(),
                        seat.getSeatNumber()))
                .toList();
    }
}
