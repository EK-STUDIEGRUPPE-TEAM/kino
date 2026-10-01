//package dk.kino.kino.model;
//
//import jakarta.persistence.*;
//
//@Entity
//public class ReservationSeat {
//
//    @EmbeddedId
//    private ReservationSeatId id;
//
//    @ManyToOne
//    @MapsId("reservationId")
//    @JoinColumn(name = "reservation_id")
//    private Reservation reservation;
//
//    @ManyToOne
//    @MapsId("seatId")
//    @JoinColumn(name = "seat_id")
//    private Seat seat;
//
//    public ReservationSeat() {
//    }
//
//    public ReservationSeat(Reservation reservation, Seat seat) {
//        this.reservation = reservation;
//        this.seat = seat;
//        this.id = new ReservationSeatId(
//                reservation.getId(),
//                seat.getId()
//        );
//    }
//
//    public ReservationSeatId getId() {
//        return id;
//    }
//
//    public Reservation getReservation() {
//        return reservation;
//    }
//
//    public void setReservation(Reservation reservation) {
//        this.reservation = reservation;
//    }
//
//    public Seat getSeat() {
//        return seat;
//    }
//
//    public void setSeat(Seat seat) {
//        this.seat = seat;
//    }
//}