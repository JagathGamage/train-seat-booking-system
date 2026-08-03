package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "trains")
@Getter
@Setter
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String trainNo;

    private String trainName;

    @OneToMany(mappedBy = "train")
    private List<TrainSchedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "train")
    private List<Coach> coaches = new ArrayList<>();

}