package com.example.application.data.service;

import com.example.application.data.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
//    @OneToMany(targetEntity=ParkingSlot.class, mappedBy="college", fetch= FetchType.EAGER)
    List<ParkingSlot> getByName(String name);
}