package dk.kino.kino.repository;

import dk.kino.kino.model.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationSeatRepository
        extends JpaRepository<ReservationSeat, Long> {

    List<ReservationSeat> findByReservationId(Long reservationId);
    List<ReservationSeat> findByReservationShowingId(Long showingId);
}