package dk.kino.kino.model;

import jakarta.persistence.*;


@Entity
public class ReservationSeat {

    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private Long id;

    @ManyToOne
    @JoinColumn ( name = "reservation_id" )
    private Reservation reservation;

    @ManyToOne
    @JoinColumn ( name = "seat_id" )
    private Seat seat;

    public ReservationSeat( ) {
    }

    public ReservationSeat( Reservation reservation , Seat seat ) {
        this.reservation = reservation;
        this.seat = seat;

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