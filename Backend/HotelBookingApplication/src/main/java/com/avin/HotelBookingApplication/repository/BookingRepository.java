package com.avin.HotelBookingApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.avin.HotelBookingApplication.model.BookedRoom;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookedRoom, Long> {

    // Correctly finds bookings linked to the Room's ID
    List<BookedRoom> findByRoomId(Long roomId);

    Optional<BookedRoom> findByBookingConfirmationCode(String confirmationCode);

    // Matches the field 'guestEmail' in the BookedRoom model
    List<BookedRoom> findByGuestEmail(String email);
}