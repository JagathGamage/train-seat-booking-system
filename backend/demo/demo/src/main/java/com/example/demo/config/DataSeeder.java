package com.example.demo.config;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.entity.Coach;
import com.example.demo.entity.CoachType;
import com.example.demo.entity.FareConfig;
import com.example.demo.entity.ScheduleStatus;
import com.example.demo.entity.Seat;
import com.example.demo.entity.Station;
import com.example.demo.entity.Train;
import com.example.demo.entity.TrainSchedule;
import com.example.demo.repository.CoachRepository;
import com.example.demo.repository.FareConfigRepository;
import com.example.demo.repository.SeatRepository;
import com.example.demo.repository.StationRepository;
import com.example.demo.repository.TrainRepository;
import com.example.demo.repository.TrainScheduleRepository;

@Configuration
public class DataSeeder {


    @Bean
    CommandLineRunner seedDatabase(
            TrainRepository trainRepository,
            StationRepository stationRepository,
            CoachRepository coachRepository,
            SeatRepository seatRepository,
            TrainScheduleRepository scheduleRepository,
            FareConfigRepository fareConfigRepository) {


        return args -> {


            // =========================
            // Create Train
            // =========================

            Train train;


            if(trainRepository.count() == 0){

                train = new Train();

                train.setTrainNo("1005");

                train.setTrainName("Udarata Menike");

                trainRepository.save(train);

            }
            else{

                train = trainRepository.findAll()
                        .get(0);

            }



            // =========================
            // Create Stations
            // =========================

            if(stationRepository.count()==0){

                List<Station> stations =
                        new ArrayList<>();

                stations.add(
                    createStation(
                        "Colombo Fort",
                        1,
                        0)
                );

                stations.add(
                    createStation(
                        "Ragama",
                        2,
                        14)
                );

                stations.add(
                    createStation(
                        "Gampaha",
                        3,
                        28)
                );

                stations.add(
                    createStation(
                        "Kandy",
                        4,
                        120)
                );


                stationRepository.saveAll(stations);

            }



            // =========================
            // Create Coaches
            // =========================

            if(coachRepository.count()==0){


                Coach r1 =
                    createCoach(
                        train,
                        "R1",
                        CoachType.RESERVED,
                        60);


                Coach r2 =
                    createCoach(
                        train,
                        "R2",
                        CoachType.RESERVED,
                        60);


                Coach r3 =
                    createCoach(
                        train,
                        "R3",
                        CoachType.RESERVED,
                        60);



                Coach u1 =
                    createCoach(
                        train,
                        "U1",
                        CoachType.UNRESERVED,
                        0);


                Coach u2 =
                    createCoach(
                        train,
                        "U2",
                        CoachType.UNRESERVED,
                        0);


                Coach u3 =
                    createCoach(
                        train,
                        "U3",
                        CoachType.UNRESERVED,
                        0);


                Coach u4 =
                    createCoach(
                        train,
                        "U4",
                        CoachType.UNRESERVED,
                        0);


                Coach u5 =
                    createCoach(
                        train,
                        "U5",
                        CoachType.UNRESERVED,
                        0);



                coachRepository.saveAll(
                    List.of(
                        r1,r2,r3,
                        u1,u2,u3,u4,u5
                    )
                );

            }



            // =========================
            // Create Seats
            // =========================

            if(seatRepository.count()==0){


                List<Coach> reservedCoaches =
                        coachRepository.findAll()
                        .stream()
                        .filter(c ->
                            c.getCoachType()
                            ==
                            CoachType.RESERVED)
                        .toList();



                for(Coach coach: reservedCoaches){


                    for(int i=1;
                        i<=coach.getSeatCount();
                        i++){


                        Seat seat = new Seat();

                        seat.setCoach(coach);

                        seat.setSeatNumber(i);


                        seatRepository.save(seat);

                    }

                }

            }



            // =========================
            // Create Schedule
            // =========================

            if(scheduleRepository.count()==0){


                TrainSchedule schedule =
                        new TrainSchedule();


                schedule.setTrain(train);

                schedule.setTravelDate(
                    LocalDate.now()
                );


                schedule.setDepartureTime(
                    LocalTime.of(8,30)
                );


                schedule.setStatus(
                    ScheduleStatus.SCHEDULED
                );


                scheduleRepository.save(schedule);

            }




            // =========================
            // Fare Configuration
            // =========================

            if(fareConfigRepository.count()==0){


                saveFareConfig(
                    fareConfigRepository,
                    "BASE_FARE",
                    100);


                saveFareConfig(
                    fareConfigRepository,
                    "PRICE_PER_SEGMENT",
                    120);


                saveFareConfig(
                    fareConfigRepository,
                    "RESERVED_PREMIUM",
                    50);


                saveFareConfig(
                    fareConfigRepository,
                    "PEAK_MORNING_PERCENT",
                    20);


                saveFareConfig(
                    fareConfigRepository,
                    "PEAK_EVENING_PERCENT",
                    15);


                saveFareConfig(
                    fareConfigRepository,
                    "WEEKEND_PERCENT",
                    10);


                saveFareConfig(
                    fareConfigRepository,
                    "DEMAND_40_PERCENT",
                    10);


                saveFareConfig(
                    fareConfigRepository,
                    "DEMAND_70_PERCENT",
                    20);


                saveFareConfig(
                    fareConfigRepository,
                    "DEMAND_90_PERCENT",
                    35);

            }


        };

    }



    private Station createStation(
            String name,
            int order,
            double distance) {


        Station station = new Station();

        station.setStationName(name);

        station.setStationOrder(order);

        station.setDistanceKm(distance);


        return station;

    }




    private Coach createCoach(
            Train train,
            String name,
            CoachType type,
            int seatCount) {


        Coach coach = new Coach();

        coach.setTrain(train);

        coach.setCoachName(name);

        coach.setCoachType(type);

        coach.setSeatCount(seatCount);


        return coach;

    }





    private void saveFareConfig(
            FareConfigRepository repository,
            String key,
            double value) {


        FareConfig config =
                new FareConfig();


        config.setConfigKey(key);

        config.setConfigValue(value);


        repository.save(config);

    }

}