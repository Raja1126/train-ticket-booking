package com.cloudbees.ticket_booking.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatAllocationResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String seatSection;
    private String seatNumber;
}
