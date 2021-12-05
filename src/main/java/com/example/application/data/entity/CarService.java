package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.Entity;

@Entity
public class CarService extends AbstractEntity {
    private String serviceName;
    private int serviceCharge;

    public CarService() {

    }

    public CarService(String serviceName, int serviceCharge) {
        this.serviceName = serviceName;
        this.serviceCharge = serviceCharge;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(int serviceCharge) {
        this.serviceCharge = serviceCharge;
    }
}