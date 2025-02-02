package com.cloudbees.ticket_booking.repository;

import com.cloudbees.ticket_booking.model.BookingDetails;
import com.cloudbees.ticket_booking.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingDetailsRepository extends JpaRepository<BookingDetails, Long> {
    List<BookingDetails> findByBooking_BookingId(Long bookingId);

    // Find booking details by seat ID
    Optional<BookingDetails> findBySeat_SeatId(Long seatId);

    // ✅ Delete all booking details for a given booking
    void deleteByBooking_BookingId(Long bookingId);

    List<BookingDetails> findBySeat(Seat seat);
}