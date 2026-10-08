package dk.kino.kino.service;


import dk.kino.kino.model.*;
import dk.kino.kino.repository.ReservationSeatRepository;
import org.springframework.stereotype.Service;
import java.util.List;

import dk.kino.kino.repository.ReservationRepository;
import dk.kino.kino.repository.SeatRepository;
import dk.kino.kino.repository.ShowingRepository;
import org.springframework.transaction.annotation.Transactional;
import dk.kino.kino.exception.NotFoundException;
import dk.kino.kino.model.SeatStatus;




@Service
public class ReservationService {


    private final ReservationSeatRepository reservationSeatRepository;
    private final ReservationRepository reservationRepository;
    private final ShowingRepository showingRepository;
    private final SeatRepository seatRepository;


    public ReservationService(ReservationSeatRepository reservationSeatRepository,
                              ReservationRepository reservationRepository,
                              ShowingRepository showingRepository,
                              SeatRepository seatRepository) {
        this.reservationSeatRepository = reservationSeatRepository;
        this.reservationRepository = reservationRepository;
        this.showingRepository = showingRepository;
        this.seatRepository = seatRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation createReservation(Reservation reservation, List<Long> seatIds) {
        if (reservation.getCustomerName() == null || reservation.getCustomerName().isBlank()) {
            throw new IllegalArgumentException("Kundenavn skal udfyldes");
        }

        if (reservation.getPhone() == null || reservation.getPhone().isBlank()) {
            throw new IllegalArgumentException("Telefonnummer skal udfyldes");
        }

        if (reservation.getShowing() == null || reservation.getShowing().getId() == null) {
            throw new IllegalArgumentException("Vælg en forestilling");
        }

        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Vælg mindst ét sæde");
        }

        Showing showing = showingRepository.findById(reservation.getShowing().getId())
                .orElseThrow(() -> new IllegalArgumentException("Forestillingen findes ikke"));

        List<Long> uniqueSeatIds = seatIds.stream().distinct().toList();
        List<Seat> seats = seatRepository.findAllById(uniqueSeatIds);

        if (seats.size() != uniqueSeatIds.size()) {
            throw new IllegalArgumentException("Et eller flere sæder findes ikke");
        }

        List<Long> reservedSeatIds = reservationSeatRepository.findByReservationShowingId(showing.getId())
                .stream()
                .map(reservationSeat -> reservationSeat.getSeat().getId())
                .toList();

        for (Seat seat : seats) {
            if (!seat.getTheatre().getId().equals(showing.getTheatre().getId())) {
                throw new IllegalArgumentException("Sædet hører ikke til salen for denne forestilling");
            }

            if (reservedSeatIds.contains(seat.getId())) {
                throw new IllegalStateException(
                        "Række " + seat.getRowNumber() + ", sæde " + seat.getSeatNumber() + " er allerede reserveret");
            }
        }

        reservation.setShowing(showing);
        Reservation savedReservation = reservationRepository.save(reservation);

        for (Seat seat : seats) {
            reservationSeatRepository.save(new ReservationSeat(savedReservation, seat, SeatStatus.RESERVED));
        }

        return savedReservation;
    }
    @Transactional
    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservationen blev ikke fundet"));

        // Sæderne skal slettes først, fordi de peger på reservationen
        reservationSeatRepository.deleteAll(reservationSeatRepository.findByReservationId(reservation.getId()));
        reservationRepository.delete(reservation);
    }

    public List<ReservationSeat> getReservationsForShowing(Long showingId) {
        return reservationSeatRepository.findByReservationShowingId(showingId);
    }


}
