package dk.kino.kino.repository;

import dk.kino.kino.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByTheatreIdOrderByRowNumberAscSeatNumberAsc(Long theatreId);
}
