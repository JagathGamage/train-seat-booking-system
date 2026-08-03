package com.example.demo.service.impl;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.station.StationResponse;
import com.example.demo.entity.Station;
import com.example.demo.repository.StationRepository;
import com.example.demo.service.StationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;

    @Override
    public List<StationResponse> getAllStations() {

        return stationRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Station::getStationOrder))
                .map(StationResponse::fromEntity)
                .toList();
    }
}