package com.avin.HotelBookingApplication.security;

import com.avin.HotelBookingApplication.model.Room;
import com.avin.HotelBookingApplication.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class RoomTypeInitializer implements CommandLineRunner {
    private final RoomRepository roomRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roomRepository.count() == 0) {
            // Adding a default room to initialize the database
            Room defaultRoom = new Room();
            defaultRoom.setRoomType("Single");
            defaultRoom.setRoomPrice(BigDecimal.valueOf(100));
            defaultRoom.setBooked(false); // Explicitly set this to be safe
            defaultRoom.setStatus("AVAILABLE");
            
            roomRepository.save(defaultRoom);
            System.out.println("Database initialized with default room.");
        }
    }
}