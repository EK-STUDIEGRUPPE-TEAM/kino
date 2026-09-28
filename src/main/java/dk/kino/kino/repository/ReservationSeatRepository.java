package dk.kino.kino.repository;

import dk.kino.kino.model.ReservationSeat;
import dk.kino.kino.model.ReservationSeatId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationSeatRepository
        extends JpaRepository<ReservationSeat, ReservationSeatId> {
}