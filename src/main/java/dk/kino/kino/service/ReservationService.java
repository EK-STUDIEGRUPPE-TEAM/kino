package dk.kino.kino.service;

import dk.kino.kino.dto.ReservationDTO;
import dk.kino.kino.model.Reservation;
import dk.kino.kino.model.Seat;
import dk.kino.kino.model.Showing;
import dk.kino.kino.repository.ReservationRepository;
import dk.kino.kino.repository.ReservationSeatRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationSeatRepository reservationSeatRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationSeatRepository reservationSeatRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationSeatRepository = reservationSeatRepository;
    }

    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private ReservationDTO toDTO(Reservation reservation) {
        Showing showing = reservation.getShowing();

        List<String> seats = reservationSeatRepository.findByReservationId(reservation.getId())
                .stream()
                .map(reservationSeat -> reservationSeat.getSeat())
                .sorted(Comparator.comparingInt(Seat::getRowNumber)
                        .thenComparingInt(Seat::getSeatNumber))
                .map(seat -> "Række " + seat.getRowNumber() + ", sæde " + seat.getSeatNumber())
                .toList();

        return new ReservationDTO(
                reservation.getId(),
                reservation.getCustomerName(),
                reservation.getPhone(),
                showing != null && showing.getMovie() != null ? showing.getMovie().getTitle() : null,
                showing != null && showing.getTheatre() != null ? showing.getTheatre().getName() : null,
                showing != null ? showing.getDateTime() : null,
                seats
        );
    }
}
