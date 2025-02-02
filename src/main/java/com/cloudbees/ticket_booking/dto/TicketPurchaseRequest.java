package com.cloudbees.ticket_booking.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class TicketPurchaseRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private Long trainId;
}