package com.example.demo.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.booking.BookingHistoryResponse;
import com.example.demo.dto.booking.BookingRequest;
import com.example.demo.dto.booking.BookingResponse;
import com.example.demo.entity.Booking;
import com.example.demo.entity.Seat;
import com.example.demo.entity.Station;
import com.example.demo.entity.TrainSchedule;
import com.example.demo.exception.BookingConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.SeatRepository;
import com.example.demo.repository.StationRepository;
import com.example.demo.repository.TrainScheduleRepository;
import com.example.demo.service.BookingService;
import com.example.demo.service.FareCalculationService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {


    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final TrainScheduleRepository scheduleRepository;
    private final StationRepository stationRepository;
    private final FareCalculationService fareCalculationService;



    @Override
    public BookingResponse createBooking(BookingRequest request) {


        // 1. Find train schedule
        TrainSchedule schedule =
                scheduleRepository.findById(request.scheduleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Schedule not found"
                        )
                );


        // 2. Find seat
        Seat seat =
                seatRepository.findById(request.seatId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Seat not found"
                        )
                );


        // 3. Find origin station
        Station origin =
                stationRepository.findById(request.originStationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Origin station not found"
                        )
                );


        // 4. Find destination station
        Station destination =
                stationRepository.findById(request.destinationStationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Destination station not found"
                        )
                );



        int originOrder = origin.getStationOrder();
        int destinationOrder = destination.getStationOrder();



        // 5. Validate journey
        if(originOrder >= destinationOrder){

            throw new IllegalArgumentException(
                    "Invalid journey. Destination must be after origin"
            );

        }



        // 6. Check seat availability
        long conflicts =
                bookingRepository.countConflictingBookings(
                        request.scheduleId(),
                        request.seatId(),
                        originOrder,
                        destinationOrder
                );


        if(conflicts > 0){

            throw new BookingConflictException(
                    "Seat already booked for this journey segment"
            );

        }



        // 7. Calculate fare
        //Double fare =
          //      calculateFare(
           //             originOrder,
            //            destinationOrder
            //    );
        double fare =
        fareCalculationService.calculateFare(
                schedule,
                seat,
                origin,
                destination
                );

        



        // 8. Create booking
        Booking booking = new Booking();

        booking.setFare(fare);
        booking.setSchedule(schedule);
        booking.setSeat(seat);
        booking.setOriginStation(origin);
        booking.setDestinationStation(destination);

        booking.setPassengerName(
                request.passengerName()
        );

        booking.setPassengerNic(
                request.passengerNic()
        );

        //booking.setFare(fare);

       // booking.setStatus(
        //        BookingStatus.CONFIRMED
       // );



        // 9. Save booking
        Booking saved =
                bookingRepository.save(booking);



        // 10. Return response
        return new BookingResponse(

                saved.getId(),

                seat.getCoach().getCoachName(),

                seat.getSeatNumber(),

                origin.getStationName(),

                destination.getStationName(),

                fare

                //saved.getStatus().name()

        );

    }



    // private Double calculateFare(
    //         int origin,
    //         int destination
    // ){

    //     int distance =
    //             destination - origin;


    //     BigDecimal fare =
    //             BigDecimal.valueOf(distance * 100);


    //     return fare.doubleValue();

    // }


    @Override
    @Transactional(readOnly = true)
    public List<BookingHistoryResponse> getBookingHistory(String passengerNic) {

        List<Booking> bookings =
                bookingRepository.findByPassengerNicOrderByScheduleTravelDateDesc(
                        passengerNic);

        if (bookings.isEmpty()) {
        throw new ResourceNotFoundException(
                "No bookings found for NIC: " + passengerNic);
    }

        return bookings.stream()
                .map(booking -> new BookingHistoryResponse(

                        booking.getId(),

                        booking.getSchedule().getTravelDate(),

                        booking.getSchedule().getTrain().getTrainName(),

                        booking.getSeat().getCoach().getCoachName(),

                        booking.getSeat().getSeatNumber(),

                        booking.getOriginStation().getStationName(),

                        booking.getDestinationStation().getStationName(),

                        booking.getFare()
                ))
                .toList();

    }

}