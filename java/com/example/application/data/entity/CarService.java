package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.Entity;

@Entity
public class CarService extends AbstractEntity {
    private String serviceName;
    private int serviceCharge;

    public CarService(String serviceName, int serviceCharge) {
        this.serviceName = serviceName;
        this.serviceCharge = serviceCharge;
    }

    public CarService(){}

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String name) {
        this.serviceName = name;
    }

    public int getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(int cost) {
        this.serviceCharge = cost;
    }
}
