package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.Entity;
import javax.validation.constraints.Email;

@Entity
public class User extends AbstractEntity {
    private String firstName;
    private String lastName;
    private String username;
    private String passwordSalt;
    private String passwordHash;
    private Role role;
    private String address;
    private String mobile;
    @Email
    private String email;
    private String registrationNumber;
    private String activationCode;
    private boolean active;
    private double rating;
    int[] prices;
    String[] services;
//    private transient List<CarService> services;
    private String location;

    public User() {
    }

    // For user
    public User(String firstName, String lastName, String username, String password, Role role, String address,
                String mobile, String email, String last) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.role = role;
        this.passwordSalt = RandomStringUtils.random(32);
        this.passwordHash = DigestUtils.sha1Hex(password + passwordSalt);
        this.activationCode = RandomStringUtils.randomAlphanumeric(32);
        this.address = address;
        this.mobile = mobile;
        this.email = email;
        if (role == Role.WORKER) {
            this.location = last;
            this.rating = -1;
            setServices(new String[] {
                    "Dry Cleaning",
                    "Car Washing",
                    "Disc Tuning",
                    "Engine Oil Replacement",
                    "Oil Filter Replacement",
                    "AC Filter Cleaning",
                    "Brake Fluid Top-Up"
            });
            setPrices(new int[] {
                    250, 500, 500, 250, 150, 100, 100
            });
//            services = new ArrayList<>();
//            services.add(new CarService("Dry Cleaning", 250));
//            services.add(new CarService("Car Washing", 500));
//            services.add(new CarService("Disc Tuning", 500));
//            services.add(new CarService("Engine Oil Replacement", 250));
//            services.add(new CarService("Oil Filter Replacement", 150));
//            services.add(new CarService("AC Filter Cleaning", 100));
//            services.add(new CarService("Brake Fluid Top-Up", 100));
        } else {
            this.registrationNumber = last;
        }
    }

    public boolean checkPassword(String password) {
        return DigestUtils.sha1Hex(password + passwordSalt).equals(passwordHash);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

//    public List<CarService> getServices() {
//        return services;
//    }
//
//    public void setServices(List<CarService> services) {
//        this.services = services;
//    }

    public int[] getPrices() {
        return prices;
    }

    public void setPrices(int[] prices) {
        this.prices = prices;
    }

    public void setPrices(int index, int value) {
        prices[index] = value;
    }

    public String[] getServices() {
        return services;
    }

    public void setServices(String[] services) {
        this.services = services;
    }

}