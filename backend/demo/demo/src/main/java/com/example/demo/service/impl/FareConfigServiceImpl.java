package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.repository.FareConfigRepository;
import com.example.demo.service.FareConfigService;
import com.example.demo.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FareConfigServiceImpl
implements FareConfigService{

    private final FareConfigRepository repository;

    @Override
    public double getValue(String key){

        return repository.findByConfigKey(key)

                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "Fare config not found"))
                .getConfigValue();

    }

}
