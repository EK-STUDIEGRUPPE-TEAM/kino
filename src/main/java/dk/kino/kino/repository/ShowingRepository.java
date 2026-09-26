package dk.kino.kino.repository;

import dk.kino.kino.model.Showing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowingRepository extends JpaRepository<Showing, Long> {
}