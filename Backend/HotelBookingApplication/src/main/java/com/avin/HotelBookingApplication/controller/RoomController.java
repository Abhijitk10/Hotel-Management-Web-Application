package com.avin.HotelBookingApplication.controller;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.avin.HotelBookingApplication.exception.PhotoRetrievalException;
import com.avin.HotelBookingApplication.exception.ResourceNotFoundException;
import com.avin.HotelBookingApplication.model.BookedRoom;
import com.avin.HotelBookingApplication.model.Room;
import com.avin.HotelBookingApplication.response.BookingResponse;
import com.avin.HotelBookingApplication.response.RoomResponse;
import com.avin.HotelBookingApplication.service.BookingService;
import com.avin.HotelBookingApplication.service.IRoomService;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final IRoomService roomService;
    private final BookingService bookingService;
    @PostMapping("/add/new-room")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> addNewRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice) {
        try {
            Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
            RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(),
                    savedRoom.getRoomPrice());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) BigDecimal roomPrice,
            @RequestParam(required = false) String status, 
            @RequestParam(required = false) MultipartFile photo) throws SQLException, IOException {
        
        byte[] photoBytes = (photo != null && !photo.isEmpty()) ? photo.getBytes()
                : roomService.getRoomPhotoByRoomId(roomId);
        
        // Fixed: Call service ONCE with all 5 parameters. No more red text!
        Room updatedRoom = roomService.updateRoom(roomId, roomType, roomPrice, photoBytes, status);
        
        return ResponseEntity.ok(getRoomResponse(updatedRoom));
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : rooms) {
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            RoomResponse roomResponse = getRoomResponse(room);
            if (photoBytes != null && photoBytes.length > 0) {
                roomResponse.setPhoto(Base64.encodeBase64String(photoBytes));
            }
            roomResponses.add(roomResponse);
        }
        return ResponseEntity.ok(roomResponses);
    }

    @DeleteMapping("/delete/room/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/room/types")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId) {
        Optional<Room> theRoom = roomService.getRoomById(roomId);
        return theRoom.map(room -> ResponseEntity.ok(Optional.of(getRoomResponse(room))))
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }

    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings = bookingService.getAllBookingsByRoomId(room.getId());
        List<BookingResponse> bookingInfo = bookings.stream()
                .map(booking -> new BookingResponse(
                        booking.getBookingId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(), 
                        booking.getBookingConfirmationCode()))
                .toList();

        byte[] photoBytes = null;
        Blob photoBlob = room.getPhoto();
        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return new RoomResponse(room.getId(), room.getRoomType(), room.getRoomPrice(),
                room.isBooked(), photoBytes, bookingInfo);
    }
}