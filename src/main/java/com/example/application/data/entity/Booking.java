package com.example.application.data.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Booking {
    private User customer;
    private User worker;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private int duration;
    private List<CarService> services = new ArrayList<>();
    private ParkingSlot parkingSlot;

    public Booking(User customer, LocalDateTime startTime, LocalDateTime finishTime, ParkingSlot parkingSlot) {
        this.customer = customer;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.parkingSlot = parkingSlot;
        setDuration();
    }

    public void setWorker(User worker) {
        this.worker = worker;
    }

    private void setDuration(){
        duration = 0;
        while (startTime.plusHours(duration).isBefore(finishTime)) {
            duration++;
        }
    }

    public int getDuration(){
        return duration;
    }

    public void addService(CarService carService){
        services.add(carService);
    }

    public void removeService(CarService carService){
        services.remove(carService);
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getWorker() {
        return worker;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<CarService> getServices() {
        return services;
    }

    public void setServices(List<CarService> services) {
        this.services = services;
    }

    public ParkingSlot getParkingSlot() {
        return parkingSlot;
    }

    public void setParkingSlot(ParkingSlot parkingSlot) {
        this.parkingSlot = parkingSlot;
    }
}
