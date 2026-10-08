package dk.kino.kino.controller;

import dk.kino.kino.model.ReservationSeat;
import dk.kino.kino.service.ReservationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import dk.kino.kino.model.Reservation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;


@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;


    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation,
                                                         @RequestParam List<Long> seatIds) {
        Reservation savedReservation = reservationService.createReservation(reservation, seatIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleSeatTaken(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
    }

    @GetMapping("/showing/{showingId}")
    public List<ReservationSeat> getReservationsForShowing(@PathVariable Long showingId) {
        return reservationService.getReservationsForShowing(showingId);
    }

}
