package com.cloudbees.ticket_booking.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketReceiptResponse {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String fromLocation;
    private String toLocation;
    private BigDecimal pricePaid;
    private Long bookingId;
    private LocalDate bookingDate;
    private String seatSection;
    private String seatNumber;
}
