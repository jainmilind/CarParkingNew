package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class CarService extends AbstractEntity implements Serializable {
    private String serviceName;
    private int serviceCharge;
    private String carNum;

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

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }
}
