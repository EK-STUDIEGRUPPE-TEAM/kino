package dk.kino.kino.config;

import dk.kino.kino.model.*;
import dk.kino.kino.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class InitData implements CommandLineRunner {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    TheatreRepository theatreRepository;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    ShowingRepository showingRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ReservationSeatRepository reservationSeatRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {

        // Testdata indsættes kun hvis databasen er tom
        if (movieRepository.count() > 0) {
            return;
        }

        // Film
        Movie dune = new Movie();
        dune.setTitle("Dune: Part Two");
        dune.setGenre(Genre.SCIFI);
        dune.setAgeLimit(13);
        dune.setDuration(166);
        movieRepository.save(dune);

        Movie sinister = new Movie();
        sinister.setTitle("Sinister");
        sinister.setGenre(Genre.HORROR);
        sinister.setAgeLimit(17);
        sinister.setDuration(110);
        movieRepository.save(sinister);

        Movie tenThings = new Movie();
        tenThings.setTitle("10 Things I Hate About You");
        tenThings.setGenre(Genre.ROMANCE);
        tenThings.setAgeLimit(13);
        tenThings.setDuration(97);
        movieRepository.save(tenThings);

        Movie titanic = new Movie();
        titanic.setTitle("Titanic");
        titanic.setGenre(Genre.DRAMA);
        titanic.setAgeLimit(13);
        titanic.setDuration(194);
        movieRepository.save(titanic);

        // Sale og sæder
        Theatre lilleSal = new Theatre();
        lilleSal.setName("Lille sal");
        lilleSal.setNumberOfRows(20);
        lilleSal.setSeatsPerRow(12);
        theatreRepository.save(lilleSal);

        List<Seat> lilleSalSeats = new ArrayList<>();
        for (int row = 1; row <= lilleSal.getNumberOfRows(); row++) {
            for (int seatNumber = 1; seatNumber <= lilleSal.getSeatsPerRow(); seatNumber++) {
                Seat seat = new Seat();
                seat.setRowNumber(row);
                seat.setSeatNumber(seatNumber);
                seat.setTheatre(lilleSal);
                seatRepository.save(seat);
                lilleSalSeats.add(seat);
            }
        }

        Theatre storeSal = new Theatre();
        storeSal.setName("Store sal");
        storeSal.setNumberOfRows(25);
        storeSal.setSeatsPerRow(16);
        theatreRepository.save(storeSal);

        List<Seat> storeSalSeats = new ArrayList<>();
        for (int row = 1; row <= storeSal.getNumberOfRows(); row++) {
            for (int seatNumber = 1; seatNumber <= storeSal.getSeatsPerRow(); seatNumber++) {
                Seat seat = new Seat();
                seat.setRowNumber(row);
                seat.setSeatNumber(seatNumber);
                seat.setTheatre(storeSal);
                seatRepository.save(seat);
                storeSalSeats.add(seat);
            }
        }


        // Forestillinger

        LocalDate today = LocalDate.now();

        // Lille sal showings
        Showing showing1 = new Showing();
        showing1.setDateTime(today.plusDays(1).atTime(17, 0));
        showing1.setMovie(sinister);
        showing1.setTheatre(lilleSal);
        showingRepository.save(showing1);

        Showing showing2 = new Showing();
        showing2.setDateTime(today.plusDays(1).atTime(20, 0));
        showing2.setMovie(tenThings);
        showing2.setTheatre(lilleSal);
        showingRepository.save(showing2);

        Showing showing3 = new Showing();
        showing3.setDateTime(today.plusDays(2).atTime(21, 0));
        showing3.setMovie(sinister);
        showing3.setTheatre(lilleSal);
        showingRepository.save(showing3);

        Showing showing4 = new Showing();
        showing4.setDateTime(today.plusDays(3).atTime(18, 0));
        showing4.setMovie(titanic);
        showing4.setTheatre(lilleSal);
        showingRepository.save(showing4);

        // Store sal showing
        Showing showing5 = new Showing();
        showing5.setDateTime(today.plusDays(1).atTime(13, 0));
        showing5.setMovie(titanic);
        showing5.setTheatre(storeSal);
        showingRepository.save(showing5);

        Showing showing6 = new Showing();
        showing6.setDateTime(today.plusDays(1).atTime(19, 0));
        showing6.setMovie(dune);
        showing6.setTheatre(storeSal);
        showingRepository.save(showing6);

        Showing showing7 = new Showing();
        showing7.setDateTime(today.plusDays(2).atTime(18, 0));
        showing7.setMovie(dune);
        showing7.setTheatre(storeSal);
        showingRepository.save(showing7);

        Showing showing8 = new Showing();
        showing8.setDateTime(today.plusDays(3).atTime(19, 0));
        showing8.setMovie(tenThings);
        showing8.setTheatre(storeSal);
        showingRepository.save(showing8);

        Showing showing9 = new Showing();
        showing9.setDateTime(today.plusDays(3).atTime(21, 0));
        showing9.setMovie(sinister);
        showing9.setTheatre(storeSal);
        showingRepository.save(showing9);

        // Medarbejdere
        Employee sales1 = new Employee();
        sales1.setName("Anna Sørensen");
        sales1.setType(EmployeeType.SALES);
        employeeRepository.save(sales1);

        Employee sales2 = new Employee();
        sales2.setName("Mikkel Thomsen");
        sales2.setType(EmployeeType.SALES);
        employeeRepository.save(sales2);

        Employee operator = new Employee();
        operator.setName("Peter Kristensen");
        operator.setType(EmployeeType.MOVIE_OPERATOR);
        employeeRepository.save(operator);

        Employee inspector = new Employee();
        inspector.setName("Louise Andersen");
        inspector.setType(EmployeeType.TICKET_INSPECTOR);
        employeeRepository.save(inspector);

        // Reservationer
        createReservation("Mette Hansen", "20345678", showing1, lilleSalSeats, 5, List.of(5, 6),SeatStatus.RESERVED);
        createReservation("Ahmed Ali", "31234567", showing2, lilleSalSeats, 8, List.of(3, 4, 5), SeatStatus.SOLD);
        createReservation("Doctor Doofenschmirtz", "42567890", showing4, lilleSalSeats, 3, List.of(6, 7), SeatStatus.RESERVED);
        createReservation("Nanna Poulsen", "26789012", showing4, lilleSalSeats, 10, List.of(1, 2, 3, 4), SeatStatus.SOLD);
        createReservation("Emma Christensen", "53456789", showing5, storeSalSeats, 20, List.of(4, 5, 6), SeatStatus.SOLD);
        createReservation("Jonas Nielsen", "28901234", showing6, storeSalSeats, 10, List.of(7, 8, 9, 10), SeatStatus.RESERVED);
        createReservation("Sofie Jensen", "61234567", showing6, storeSalSeats, 12, List.of(1, 2), SeatStatus.RESERVED);
        createReservation("Næbdyret Perry", "40123456", showing7, storeSalSeats, 15, List.of(8, 9), SeatStatus.RESERVED);
        createReservation("Ida Rasmussen", "22345678", showing8, storeSalSeats, 6, List.of(9, 10), SeatStatus.SOLD);
        createReservation("Frederik Møller", "51234567", showing9, storeSalSeats, 25, List.of(1, 2, 3, 4), SeatStatus.SOLD);
        createReservation("Oscar Berg", "29876543", showing9, storeSalSeats, 18, List.of(13, 14, 15), SeatStatus.SOLD);
    }

    // Opretter en reservation
    private void createReservation(String customerName, String phone, Showing showing,
                                   List<Seat> theatreSeats, int row, List<Integer> seatNumbers, SeatStatus status) {
        Reservation reservation = new Reservation();
        reservation.setCustomerName(customerName);
        reservation.setPhone(phone);
        reservation.setShowing(showing);
        reservationRepository.save(reservation);

        for (int seatNumber : seatNumbers) {
            ReservationSeat reservationSeat = new ReservationSeat();
            reservationSeat.setReservation(reservation);
            reservationSeat.setSeat(findSeat(theatreSeats, row, seatNumber));
            reservationSeat.setStatus(status);
            reservationSeatRepository.save(reservationSeat);
        }
    }

    // Leder efter sædet i salen og returnerer sæde der passer til række go sædenr
    private Seat findSeat(List<Seat> theatreSeats, int row, int seatNumber) {
        for (Seat seat : theatreSeats) {
            if (seat.getRowNumber() == row && seat.getSeatNumber() == seatNumber) {
                return seat;
            }
        }
        throw new IllegalStateException("Sæde findes ikke: række " + row + ", sæde " + seatNumber);
    }
}
