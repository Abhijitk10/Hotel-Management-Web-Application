package com.avin.HotelBookingApplication.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.avin.HotelBookingApplication.exception.InvalidBookingRequestException;
import com.avin.HotelBookingApplication.exception.ResourceNotFoundException;
import com.avin.HotelBookingApplication.model.BookedRoom;
import com.avin.HotelBookingApplication.model.Room;
import com.avin.HotelBookingApplication.response.BookingResponse;
import com.avin.HotelBookingApplication.response.RoomResponse;
import com.avin.HotelBookingApplication.service.IBookingService;
import com.avin.HotelBookingApplication.service.IRoomService;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final IBookingService bookingService;
    private final IRoomService roomService;

    public BookingController(IBookingService bookingService, IRoomService roomService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
    }

    @GetMapping("/all-bookings")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookedRoom> bookings = bookingService.getAllBookings();
        // FIX: Explicitly cast to BookingResponse to help the compiler infer type <R>
        List<BookingResponse> bookingResponses = bookings.stream()
                .map((BookedRoom booking) -> this.getBookingResponse(booking))
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookingResponses);
    }

    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId,
                                         @RequestBody BookedRoom bookingRequest) {
        try {
            String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);
            return ResponseEntity.ok(
                    "Room booked successfully! Your booking confirmation code is: " + confirmationCode);
        } catch (InvalidBookingRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing booking: " + e.getMessage());
        }
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        try {
            BookedRoom booking = bookingService.findByBookingConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(booking);
            return ResponseEntity.ok(bookingResponse);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/user/{email}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookingsByUserEmail(@PathVariable String email) {
        List<BookedRoom> bookings = bookingService.getBookingsByUserEmail(email);
        // FIX: Explicitly cast to BookingResponse
        List<BookingResponse> bookingResponses = bookings.stream()
                .map((BookedRoom booking) -> this.getBookingResponse(booking))
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookingResponses);
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    public BookingResponse getBookingResponse(BookedRoom booking) {
        // 1. Get Room safely
        Room theRoom = roomService.getRoomById(booking.getRoom().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        // 2. Create RoomResponse
        RoomResponse roomResponse = new RoomResponse(
                theRoom.getId(), 
                theRoom.getRoomType(), 
                theRoom.getRoomPrice()
        );

        // 3. FIX: Create empty BookingResponse and set values manually 
        // This avoids constructor mismatch errors which cause the "Cannot infer type" error
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getBookingId());
        response.setCheckInDate(booking.getCheckInDate());
        response.setCheckOutDate(booking.getCheckOutDate());
        response.setGuestFullName(booking.getGuestFullName());
        response.setGuestEmail(booking.getGuestEmail());
        response.setNumOfAdults(booking.getNumOfAdults());
        response.setNumOfChildren(booking.getNumOfChildren());
        response.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        response.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        response.setRoom(roomResponse);

        return response;
    }
}