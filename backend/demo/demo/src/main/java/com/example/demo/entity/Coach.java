package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "coaches")
@Getter
@Setter
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    private String coachName;

    @Enumerated(EnumType.STRING)
    private CoachType coachType;

    private Integer seatCount;

    @OneToMany(mappedBy = "coach")
    private List<Seat> seats = new ArrayList<>();

}