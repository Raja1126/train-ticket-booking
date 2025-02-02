package com.cloudbees.ticket_booking.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@Table(name = "booking_details",
        uniqueConstraints = @UniqueConstraint(columnNames = {"booking_id", "seat_id"}),
        indexes = {
                @Index(name = "idx_booking_details_booking_id", columnList = "booking_id"),
                @Index(name = "idx_booking_details_seat_id", columnList = "seat_id")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingDetailsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(nullable = false, length = 100)
    private String passengerName;

    @Column(nullable = false)
    private int passengerAge;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Gender passengerGender;
}
