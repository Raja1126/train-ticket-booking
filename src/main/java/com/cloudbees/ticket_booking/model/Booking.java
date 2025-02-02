package com.cloudbees.ticket_booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "booking",
        indexes = {@Index(name = "idx_booking_user_id", columnList = "user_id")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @Column(nullable = false, length = 50)
    private String bookingStatus;

    @Column(nullable = false)
    private LocalDate bookingDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

}