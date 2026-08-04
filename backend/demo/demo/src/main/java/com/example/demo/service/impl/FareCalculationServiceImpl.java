package com.example.demo.service.impl;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.springframework.stereotype.Service;

import com.example.demo.entity.CoachType;
import com.example.demo.entity.Seat;
import com.example.demo.entity.Station;
import com.example.demo.entity.TrainSchedule;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.SeatRepository;
import com.example.demo.service.FareCalculationService;
import com.example.demo.service.FareConfigService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FareCalculationServiceImpl
        implements FareCalculationService {

    // ==========================
    // Pricing Constants
    // ==========================
    private final SeatRepository seatRepository;

    private final BookingRepository bookingRepository;
    private final FareConfigService fareConfigService;



    // private static final double BASE_FARE = fareConfigService.getValue("BASE_FARE")

    // private static final double PRICE_PER_SEGMENT = 120.0;

    // private static final double RESERVED_PREMIUM = 50.0;

    // private static final double PEAK_MORNING_PERCENT = 0.20;

    // private static final double PEAK_EVENING_PERCENT = 0.15;

    // private static final double WEEKEND_PERCENT = 0.10;

    @Override
    public double calculateFare(
            TrainSchedule schedule,
            Seat seat,
            Station origin,
            Station destination) {

        double fare = 0;

        fare += getBaseFare();

        fare += calculateDistanceFare(origin, destination);

        fare += calculateCoachPremium(seat);

        fare += calculatePeakCharge(schedule, fare);

        fare += calculateWeekendCharge(schedule, fare);
        fare += calculateDemandCharge(schedule, fare);

        return Math.round(fare);
    }

    // ==========================
    // Base Fare
    // ==========================

    private double getBaseFare() {
        return fareConfigService.getValue("BASE_FARE");
    }

    // ==========================
    // Distance Fare
    // ==========================

    private double calculateDistanceFare(
            Station origin,
            Station destination) {

        int segments =
                destination.getStationOrder()
                        - origin.getStationOrder();

        double pricePerSegment =
        fareConfigService.getValue("PRICE_PER_SEGMENT");
        return segments * pricePerSegment;

    }

    // ==========================
    // Coach Premium
    // ==========================

    private double calculateCoachPremium(
            Seat seat) {

        if (seat.getCoach().getCoachType()
                == CoachType.RESERVED) {

            return fareConfigService.getValue("RESERVED_PREMIUM");

        }

        return 0;

    }

    // ==========================
    // Peak Hour
    // ==========================

    private double calculatePeakCharge(
            TrainSchedule schedule,
            double subtotal) {

        LocalTime departure =
                schedule.getDepartureTime();

        if (!departure.isBefore(LocalTime.of(6, 0))
                && departure.isBefore(LocalTime.of(9, 0))) {

            double percentage =fareConfigService.getValue("PEAK_MORNING_PERCENT");
            return subtotal * (percentage / 100);

        }

        if (!departure.isBefore(LocalTime.of(16, 0))
                && departure.isBefore(LocalTime.of(19, 0))) {

            double percentage =fareConfigService.getValue("PEAK_EVENING_PERCENT");
            return subtotal * (percentage / 100);

        }

        return 0;

    }

    // ==========================
    // Weekend Pricing
    // ==========================

    private double calculateWeekendCharge(
            TrainSchedule schedule,
            double subtotal) {

        DayOfWeek day =
                schedule.getTravelDate()
                        .getDayOfWeek();

        if (day == DayOfWeek.SATURDAY
                || day == DayOfWeek.SUNDAY) {

            double percentage =
        fareConfigService.getValue("WEEKEND_PERCENT");
        return subtotal * (percentage / 100);

        }

        return 0;

    }
    private double calculateDemandCharge(
        TrainSchedule schedule,
        double subtotal) {

        long totalSeats =
                seatRepository.countByCoachCoachType(
                        CoachType.RESERVED);

        long bookedSeats =
                bookingRepository.countBookedSeats(
                        schedule.getId());

        if (totalSeats == 0) {
            return 0;
        }

        double occupancy =
                (double) bookedSeats / totalSeats;

        if (occupancy >= 0.90) {

    return subtotal * (
            fareConfigService.getValue("DEMAND_90_PERCENT") / 100);

        }

        if (occupancy >= 0.70) {

            return subtotal * (
                    fareConfigService.getValue("DEMAND_70_PERCENT") / 100);

        }

        if (occupancy >= 0.40) {

            return subtotal * (
                    fareConfigService.getValue("DEMAND_40_PERCENT") / 100);

        }

        return 0;
    }

}
