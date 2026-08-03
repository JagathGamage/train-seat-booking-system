package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Seat;

import jakarta.persistence.LockModeType;

import com.example.demo.entity.CoachType;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByCoachCoachType(CoachType reserved);

    List<Seat> findAll();
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Seat> findById(Long id);

    long countByCoachCoachType(CoachType coachType);

}
