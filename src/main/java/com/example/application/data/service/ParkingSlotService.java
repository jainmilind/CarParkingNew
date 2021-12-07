package com.example.application.data.service;

import com.example.application.data.entity.ParkingSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class ParkingSlotService extends CrudService<ParkingSlot, Integer> {

    private ParkingSlotRepository parkingSlotRepository;

    public ParkingSlotService(@Autowired ParkingSlotRepository parkingSlotRepository){
        this.parkingSlotRepository = parkingSlotRepository;
    }


    @Override
    protected ParkingSlotRepository getRepository() {
        return parkingSlotRepository;
    }
}
