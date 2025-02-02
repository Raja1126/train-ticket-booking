package com.cloudbees.ticket_booking.controller;

import com.cloudbees.ticket_booking.dto.SeatAllocationResponse;
import com.cloudbees.ticket_booking.dto.SeatModificationRequest;
import com.cloudbees.ticket_booking.dto.TicketPurchaseRequest;
import com.cloudbees.ticket_booking.dto.TicketReceiptResponse;
import com.cloudbees.ticket_booking.model.SeatSection;
import com.cloudbees.ticket_booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Booking Controller", description = "Handles train ticket bookings and seat allocations")
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(summary = "Purchase a ticket", description = "Allows a user to book a train ticket.")
    @PostMapping("/purchase")
    public ResponseEntity<TicketReceiptResponse> purchaseTicket(@Valid @RequestBody TicketPurchaseRequest request) {
        return ResponseEntity.ok(bookingService.bookTicket(request));

        // uncomment the following code to validate concurrent booking scenario

//        ExecutorService executorService = Executors.newFixedThreadPool(1);
//        executorService.submit(() -> bookingService.bookTicket(request));
//        executorService.shutdown();
//        try {
//            Thread.sleep(1000);
//            bookingService.bookTicket(request);
//        } catch (InterruptedException e) {
//            System.out.println("Interrupted exception caught");
//        } catch (ObjectOptimisticLockingFailureException e) {
//            throw new RuntimeException("Concurrent booking happened for seat selected, try again");
//        }
    }

    @Operation(summary = "Get seat allocations", description = "Retrieves seat allocations for a given train and section.")
    @GetMapping("/seats")
    public ResponseEntity<List<SeatAllocationResponse>> getBookingsBySection(
            @RequestParam Long trainId, @RequestParam SeatSection section) {
        return ResponseEntity.ok(bookingService.getBookingsBySection(trainId, section));
    }

    @Operation(summary = "Remove a user from a train", description = "Removes a user from the train and frees the allocated seat.")
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeUserFromTrain(@RequestParam UUID userId, @RequestParam Long trainId) {
        bookingService.removeUserFromTrain(userId, trainId);
        return ResponseEntity.ok("User removed from train successfully.");
    }

    @Operation(summary = "Modify seat allocation", description = "Allows a user to change their allocated seat.")
    @PutMapping("/modify-seat")
    public ResponseEntity<String> modifyUserSeat(@Valid @RequestBody SeatModificationRequest request) {
        bookingService.modifyUserSeat(request);
        return ResponseEntity.ok("Seat modified successfully.");
    }
}
