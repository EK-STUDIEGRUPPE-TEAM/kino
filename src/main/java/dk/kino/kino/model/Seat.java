package dk.kino.kino.model;

import jakarta.persistence.*;

@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_row")
    private int rowNumber;

    @Column(name = "seat_number")
    private int seatNumber;

    @ManyToOne
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    public Seat() {
    }

    public Seat(int rowNumber, int seatNumber, Theatre theatre) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.theatre = theatre;
    }

    public Long getId() {
        return id;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Theatre getTheatre() {
        return theatre;
    }

    public void setTheatre(Theatre theatre) {
        this.theatre = theatre;
    }
}