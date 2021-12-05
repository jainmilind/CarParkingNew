package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import com.example.application.data.entity.User;
import com.vaadin.flow.internal.Pair;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Entity
public class ParkingSlot extends AbstractEntity {

    private String image;
    private String name;
    private int price;
    public static Map<String, ArrayList<User>> workers = new HashMap<>();
    private int totalSpots;

    static private final Map<String, Map<LocalDateTime, Integer>> spotsBooked = new HashMap<>();

    private static Map<String, ArrayList<User>> customers = new HashMap<>();

    public int getTotalSpots() {
        return totalSpots;
    }

    public void setTotalSpots(int totalSpots) {
        this.totalSpots = totalSpots;
    }


    public ParkingSlot() {
    }

    public ParkingSlot(String image, String name, int price, int totalSpots) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.totalSpots = totalSpots;
        spotsBooked.putIfAbsent(this.name, new HashMap<>());
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean canBook(Pair<LocalDateTime, LocalDateTime> p) {

        for(LocalDateTime time = p.getFirst(); time.isBefore(p.getSecond()); time = time.plusHours(1)){
            spotsBooked.get(name).putIfAbsent(time, 0);
            if(spotsBooked.get(name).get(time) >= totalSpots){
                return false;
            }
        }
        return true;
    }

    public void addBooking(Pair<LocalDateTime, LocalDateTime> p, User customer) {

        for(LocalDateTime time = p.getFirst(); time.isBefore(p.getSecond()); time = time.plusHours(1)) {
            spotsBooked.get(name).putIfAbsent(time, 0);
            spotsBooked.get(name).put(time, (spotsBooked.get(name).get(time) + 1));
        }
        customers.putIfAbsent(name, new ArrayList<>());
        customers.get(name).add(customer);
    }

    public String nextAvailability(Pair<LocalDateTime, LocalDateTime> p){
        LocalDateTime time = p.getFirst();

        int duration = 0;
        for(LocalDateTime t = p.getFirst(); t.plusHours(duration).isBefore(p.getSecond()); duration++);

        boolean flag = false;

        while(!flag){
            while(spotsBooked.get(name).containsKey(time) && spotsBooked.get(name).get(time) >= totalSpots){
                time = time.plusHours(1);
            }
            flag = true;
            for(LocalDateTime t = time; duration > 0; t = t.plusHours(1)){
                if(spotsBooked.get(name).containsKey(time) && spotsBooked.get(name).get(time) >= totalSpots){
                    flag = false;
                    time = p.getSecond();
                    break;
                }
                duration--;
            }
        }

        if(time.equals(p.getFirst())){
            return "Available";
        }

        return "Parking slots for this duration are next available at " + time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a"));
    }

    public int bookedAt(LocalDateTime time){
        spotsBooked.get(name).putIfAbsent(time, 0);
        return spotsBooked.get(name).get(time);
    }

    public void addWorker(User user){
        workers.putIfAbsent(name, new ArrayList<>());
        workers.get(name).add(user);
    }
}