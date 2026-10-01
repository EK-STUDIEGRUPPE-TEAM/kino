package dk.kino.kino.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationDTO(
        Long id,
        String customerName,
        String phone,
        String movieTitle,
        String theatreName,
        LocalDateTime showingDateTime,
        List<String> seats) {
}
