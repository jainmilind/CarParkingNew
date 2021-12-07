package com.example.application.data.generator;

import com.example.application.data.entity.ParkingSlot;
import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
import com.example.application.data.service.ParkingSlotRepository;
import com.example.application.data.service.UserRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(ParkingSlotRepository parkingSlotRepository, UserRepository userRepository) {
        return args -> {


            User newUser = new User("Milind", "Jain", "user", "pass",
                    Role.USER, "VM282 \nBITS Pilani Hyderabad Campus", "7008899665", "jainmilind@gmail.com", "TL-105");
            newUser.setActive(true);
            userRepository.save(newUser);
            //firstName, lastName, username, password, Role.USER, address, mobile, email, registrationNumber
            User newAdmin = new User("Sai", "Panda", "admin", "pass",
                    Role.ADMIN, "V281 \nBITS Pilani Hyderabad Campus", "1234567890", "jesus@god.hv", "000");
            newAdmin.setActive(true);
            userRepository.save(newAdmin);

            User newWorker = new User("Hard", "Worker", "worker", "pass",
                    Role.WORKER, "Mess3 \nBITS Pilani Hyderabad Campus", "9131231231", "union@work.to", "Rajiv Gandhi International Airport");
            newWorker.setActive(true);
            userRepository.save(newWorker);

            parkingSlotRepository.save(new ParkingSlot("images/RGIA.jpg", "Rajiv Gandhi International Airport", 25, 1));
            parkingSlotRepository.save(new ParkingSlot("images/BITS.jfif", "BITS Pilani Hyderabad Campus", 30, 20));
            parkingSlotRepository.save(new ParkingSlot("images/TVM.png", "Thumukunta Vegetable Market", 50, 10));

            parkingSlotRepository.getByName("Rajiv Gandhi International Airport").addWorker(newWorker);
        };
    }

}