package com.avin.HotelBookingApplication.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private Long bookingId; // Changed from 'id' to 'bookingId'
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String guestFullName; // Changed from 'guestName' to 'guestFullName'
    private String guestEmail;
    private int numOfAdults;
    private int numOfChildren;
    private int totalNumOfGuest; // Changed to match your model
    private String bookingConfirmationCode;
    private RoomResponse room;

    // Small constructor for summaries
    public BookingResponse(Long bookingId, LocalDate checkInDate, LocalDate checkOutDate,
                           String bookingConfirmationCode) {
        this.bookingId = bookingId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}