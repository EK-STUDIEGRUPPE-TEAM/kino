package dk.kino.kino.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReservationSeatId implements Serializable {

    private Long reservationId;
    private Long seatId;

    public ReservationSeatId() {
    }

    public ReservationSeatId(Long reservationId, Long seatId) {
        this.reservationId = reservationId;
        this.seatId = seatId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public Long getSeatId() {
        return seatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationSeatId that)) return false;
        return Objects.equals(reservationId, that.reservationId)
                && Objects.equals(seatId, that.seatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId, seatId);
    }
}