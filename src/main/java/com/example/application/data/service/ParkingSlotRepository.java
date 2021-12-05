package com.example.application.data.service;

import com.example.application.data.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    ParkingSlot getByName(String name);
}