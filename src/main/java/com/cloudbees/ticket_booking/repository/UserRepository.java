package com.cloudbees.ticket_booking.repository;

import com.cloudbees.ticket_booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
