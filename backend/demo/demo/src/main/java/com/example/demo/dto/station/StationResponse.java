package com.example.demo.dto.station;

import com.example.demo.entity.Station;


public record StationResponse(

        Long id,
        String stationName,
        Integer stationOrder

) {

    public static StationResponse fromEntity(Station station) {

        return new StationResponse(
                station.getId(),
                station.getStationName(),
                station.getStationOrder()
        );

    }

}

