package dk.kino.kino.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


@Entity
public class ReservationSeat {

    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private Long id;


    @ManyToOne
    @JoinColumn ( name = "reservation_id" )
    @JsonIgnore
    private Reservation reservation;



    @ManyToOne
    @JoinColumn ( name = "seat_id" )
    private Seat seat;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;




    public ReservationSeat( ) {
    }

    public ReservationSeat( Reservation reservation , Seat seat, SeatStatus status) {
        this.reservation = reservation;
        this.seat = seat;
        this.status = status;

    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public Long getId( ) {
        return id;
    }

    public Reservation getReservation( ) {
        return reservation;
    }

    public void setReservation( Reservation reservation ) {
        this.reservation = reservation;
    }

    public Seat getSeat( ) {
        return seat;
    }

    public void setSeat( Seat seat ) {
        this.seat = seat;
    }
}