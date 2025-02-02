package com.cloudbees.ticket_booking.model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "train")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainId;

    @Column(nullable = false, length = 10)
    private String trainNumber;

    @Column(length = 100)
    private String trainName;

    @Column(nullable = false, length = 100)
    private String fromLocation = "London";

    @Column(nullable = false, length = 100)
    private String toLocation = "France";

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.valueOf(20);
}