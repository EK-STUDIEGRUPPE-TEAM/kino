package dk.kino.kino.repository;

import dk.kino.kino.model.Showing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowingRepository extends JpaRepository<Showing, Long> {

    List<Showing> findByDateTimeBetweenOrderByDateTimeAsc(
            LocalDateTime start,
            LocalDateTime end
    );
}