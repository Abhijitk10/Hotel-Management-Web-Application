package com.avin.HotelBookingApplication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.avin.HotelBookingApplication.exception.InvalidBookingRequestException;
import com.avin.HotelBookingApplication.exception.ResourceNotFoundException;
import com.avin.HotelBookingApplication.model.BookedRoom;
import com.avin.HotelBookingApplication.model.Room;
import com.avin.HotelBookingApplication.repository.BookingRepository;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<BookedRoom> getBookingsByUserEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
            throw new InvalidBookingRequestException("Check-in date must come before check-out date");
        }

        Room room = roomService.getRoomById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));

        List<BookedRoom> existingBookings = room.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings);

        if (roomIsAvailable) {
            // Generate confirmation code
            String confirmationCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            bookingRequest.setBookingConfirmationCode(confirmationCode);
            
            // Use the helper method we just added to Room.java
            room.addBooking(bookingRequest);
            
            bookingRepository.save(bookingRequest);
            return confirmationCode;
        } else {
            throw new InvalidBookingRequestException("Sorry, this room is not available for the selected dates.");
        }
    }

    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new ResourceNotFoundException("No booking found with code: " + confirmationCode));
    }

    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existing -> 
                    bookingRequest.getCheckInDate().isBefore(existing.getCheckOutDate()) && 
                    bookingRequest.getCheckOutDate().isAfter(existing.getCheckInDate())
                );
    }
}