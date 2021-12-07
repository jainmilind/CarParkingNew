package com.example.application.data.service;

import com.example.application.data.entity.ParkingSlot;
import com.example.application.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Integer> {

    ParkingSlot getByName(String name);
}