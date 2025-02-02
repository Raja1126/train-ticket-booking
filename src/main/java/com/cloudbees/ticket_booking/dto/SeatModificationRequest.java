package com.cloudbees.ticket_booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatModificationRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private Long trainId;

    @NotNull
    private String newSeatNumber;
}
