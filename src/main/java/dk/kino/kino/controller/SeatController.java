package dk.kino.kino.controller;

import dk.kino.kino.model.ReservationSeat;
import dk.kino.kino.model.Seat;
import dk.kino.kino.model.Showing;
import dk.kino.kino.repository.ReservationSeatRepository;
import dk.kino.kino.repository.SeatRepository;
import dk.kino.kino.repository.ShowingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatRepository seatRepository;
    private final ShowingRepository showingRepository;
    private final ReservationSeatRepository reservationSeatRepository;

    public SeatController(SeatRepository seatRepository,
                          ShowingRepository showingRepository,
                          ReservationSeatRepository reservationSeatRepository) {
        this.seatRepository = seatRepository;
        this.showingRepository = showingRepository;
        this.reservationSeatRepository = reservationSeatRepository;
    }


    @GetMapping
    public ResponseEntity<List<Seat>> getSeatsForShowing(@RequestParam Long showingId) {
        Optional<Showing> showing = showingRepository.findById(showingId);

        if (showing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Long theatreId = showing.get().getTheatre().getId();
        return ResponseEntity.ok(seatRepository.findByTheatreIdOrderByRowNumberAscSeatNumberAsc(theatreId));
    }


    @GetMapping("/reserved")
    public List<Seat> getReservedSeats(@RequestParam Long showingId) {
        return reservationSeatRepository.findByReservationShowingId(showingId)
                .stream()
                .map(ReservationSeat::getSeat)
                .toList();
    }
}
