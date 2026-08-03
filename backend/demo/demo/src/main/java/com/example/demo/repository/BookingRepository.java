package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Booking;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

     @Query("""
        SELECT DISTINCT b.seat.id
        FROM Booking b
        WHERE b.schedule.id = :scheduleId
          AND :newOrigin < b.destinationStation.stationOrder
          AND :newDestination > b.originStation.stationOrder
    """)
    List<Long> findConflictingSeatIds(
            Long scheduleId,
            int newOrigin,
            int newDestination);

    List<Booking> findByScheduleId(Long scheduleId);

    @Query("""
        SELECT COUNT(b)
        FROM Booking b
        WHERE b.schedule.id = :scheduleId
        AND b.seat.id = :seatId
        AND :originOrder < b.destinationStation.stationOrder
        AND :destinationOrder > b.originStation.stationOrder
        """)
    long countConflictingBookings(

            Long scheduleId,

            Long seatId,

            int originOrder,

            int destinationOrder

    );
    List<Booking> findByPassengerNicOrderByScheduleTravelDateDesc(String passengerNic);

    @Query("""
    SELECT COUNT(DISTINCT b.seat.id)
    FROM Booking b
    WHERE b.schedule.id = :scheduleId
    """)
    long countBookedSeats(Long scheduleId);

}