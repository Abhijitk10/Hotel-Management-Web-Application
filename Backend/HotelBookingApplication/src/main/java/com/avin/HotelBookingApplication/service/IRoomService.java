package com.avin.HotelBookingApplication.service;

import org.springframework.web.multipart.MultipartFile;
import com.avin.HotelBookingApplication.model.Room;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IRoomService {
    Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice) throws SQLException, IOException;
    List<String> getAllRoomTypes();
    List<Room> getAllRooms();
    byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException;
    void deleteRoom(Long roomId);
    Optional<Room> getRoomById(Long roomId);

    // This matches the 5-parameter call in your controller
    Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes, String status);
}