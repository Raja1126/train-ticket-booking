package com.cloudbees.ticket_booking.repository;

import com.cloudbees.ticket_booking.model.Seat;
import com.cloudbees.ticket_booking.model.SeatSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByTrain_TrainIdAndSection(Long trainId, SeatSection section);

    @Query("SELECT s FROM Seat s WHERE s.train.trainId = :trainId AND s.seatNumber NOT IN (SELECT bd.seat.seatNumber FROM BookingDetails bd)")
    List<Seat> findAvailableSeats(Long trainId);

    // Find a seat by train ID and seat number
    @Query("SELECT COUNT(bd.seat.seatNumber) > 0 FROM BookingDetails bd where bd.seat.seatNumber= :seatNumber")
    Boolean isSeatAlreadyBooked(Long trainId, String seatNumber);

    List<Seat> findByTrain_TrainIdAndSeatNumber(Long trainId, String seatNumber);
}
