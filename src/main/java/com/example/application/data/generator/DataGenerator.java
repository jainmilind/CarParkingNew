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


            User newUser = new User("Mili", "Kun", "user", "pass",
                    Role.USER, "a", "9969696969", "a@b.in", "z");
            newUser.setActive(true);
            userRepository.save(newUser);
            //firstName, lastName, username, password, Role.USER, address, mobile, email, registrationNumber
            User newAdmin = new User("Bhagwan", "Singh", "admin", "pass",
                    Role.ADMIN, "Heaven", "1234567890", "jesus@god.hv", "000");
            newAdmin.setActive(true);
            userRepository.save(newAdmin);

            parkingSlotRepository.save(new ParkingSlot("images/RGIA.jpg", "RGIA", 200, 10));
            parkingSlotRepository.save(new ParkingSlot("images/BITS.jfif", "BITS", 150, 20));

        };
    }

}