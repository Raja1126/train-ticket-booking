package com.cloudbees.ticket_booking.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@Table(name = "seat",
        uniqueConstraints = @UniqueConstraint(columnNames = {"train_id", "seat_number"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private SeatSection section;

    @Column(nullable = false, length = 3)
    private String seatNumber;

    @Version
    private Integer version;

    @Column(nullable = true)
    private Boolean availabilityStatus;

}