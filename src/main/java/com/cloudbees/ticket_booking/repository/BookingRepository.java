package com.cloudbees.ticket_booking.repository;

import com.cloudbees.ticket_booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser_UserId(UUID userId);
}
