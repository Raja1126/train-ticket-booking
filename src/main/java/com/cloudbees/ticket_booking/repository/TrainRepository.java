package com.cloudbees.ticket_booking.repository;

import com.cloudbees.ticket_booking.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainRepository extends JpaRepository<Train, Long> {
}
