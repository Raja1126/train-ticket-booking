package com.cloudbees.ticket_booking.service;

import com.cloudbees.ticket_booking.dto.SeatAllocationResponse;
import com.cloudbees.ticket_booking.dto.SeatModificationRequest;
import com.cloudbees.ticket_booking.dto.TicketPurchaseRequest;
import com.cloudbees.ticket_booking.dto.TicketReceiptResponse;
import com.cloudbees.ticket_booking.model.*;
import com.cloudbees.ticket_booking.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final UserRepository userRepository;
    private final TrainRepository trainRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final BookingDetailsRepository bookingDetailsRepository;

    public BookingService(UserRepository userRepository, TrainRepository trainRepository,
                          SeatRepository seatRepository, BookingRepository bookingRepository,
                          BookingDetailsRepository bookingDetailsRepository) {
        this.userRepository = userRepository;
        this.trainRepository = trainRepository;
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
        this.bookingDetailsRepository = bookingDetailsRepository;
    }

    @Transactional
    public TicketReceiptResponse bookTicket(TicketPurchaseRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Train train = trainRepository.findById(request.getTrainId()).orElseThrow(() -> new RuntimeException("Train not found"));

        List<Seat> availableSeats = seatRepository.findAvailableSeats(train.getTrainId());

        if (availableSeats.isEmpty()) {
            throw new RuntimeException("No seats available");
        }

        Seat allocatedSeat = availableSeats.get(0);
        try {
            // code to handle concurrent booking for the same seat
            allocatedSeat.setAvailabilityStatus(false);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception");
            }
            allocatedSeat = seatRepository.save(allocatedSeat);

            Booking booking = new Booking();
            booking.setUser(user);
            booking.setTrain(train);
            booking.setBookingStatus("INITIATED");
            booking.setBookingDate(LocalDate.now());
            booking.setTotalAmount(train.getPrice());
            booking = bookingRepository.save(booking);

            BookingDetails bookingDetails = new BookingDetails();
            bookingDetails.setBooking(booking);
            bookingDetails.setSeat(allocatedSeat);
            bookingDetails.setPassengerName(user.getFirstName() + " " + user.getLastName());
            bookingDetails.setPassengerAge(user.getAge());
            bookingDetails.setPassengerGender(user.getGender());

            bookingDetailsRepository.save(bookingDetails);

            return new TicketReceiptResponse(user.getUserId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                    train.getFromLocation(), train.getToLocation(), train.getPrice(), booking.getBookingId(), LocalDate.now(),
                    allocatedSeat.getSection().toString(), allocatedSeat.getSeatNumber());
        } catch (Exception e) {
            throw new RuntimeException("Concurrent booking happened for seat selected, try again");
        }
    }

    public List<SeatAllocationResponse> getBookingsBySection(Long trainId, SeatSection section) {
        List<Seat> seats = seatRepository.findByTrain_TrainIdAndSection(trainId, section);

        return seats.stream()
                .flatMap(seat -> bookingDetailsRepository.findBySeat_SeatId(seat.getSeatId()).stream())
                .map(bookingDetails -> new SeatAllocationResponse(
                        bookingDetails.getBooking().getUser().getFirstName(),
                        bookingDetails.getBooking().getUser().getLastName(),
                        bookingDetails.getBooking().getUser().getEmail(),
                        bookingDetails.getSeat().getSection().toString(),
                        bookingDetails.getSeat().getSeatNumber()
                ))
                .toList();
    }

    @Transactional
    public void removeUserFromTrain(UUID userId, Long trainId) {
        List<Booking> bookings = bookingRepository.findByUser_UserId(userId);

        bookings.stream()
                .filter(booking -> booking.getTrain().getTrainId().equals(trainId))
                .forEach(booking -> {
                    bookingDetailsRepository.deleteByBooking_BookingId(booking.getBookingId());
                    bookingRepository.delete(booking);
                });
    }

    @Transactional
    public void modifyUserSeat(SeatModificationRequest request) {
        Booking booking = bookingRepository.findByUser_UserId(request.getUserId())
                .stream()
                .filter(b -> b.getTrain().getTrainId().equals(request.getTrainId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (seatRepository.isSeatAlreadyBooked(request.getTrainId(), request.getNewSeatNumber()))
            throw new RuntimeException("New seat not available");

        List<Seat> newSeats = seatRepository.findByTrain_TrainIdAndSeatNumber(request.getTrainId(), request.getNewSeatNumber());

        if (newSeats.size() == 0)
            throw new IllegalArgumentException("Invalid seat number is provided");

        BookingDetails bookingDetails = bookingDetailsRepository.findByBooking_BookingId(booking.getBookingId()).get(0);
        bookingDetails.setSeat(newSeats.get(0));

        bookingDetailsRepository.save(bookingDetails);
    }
}