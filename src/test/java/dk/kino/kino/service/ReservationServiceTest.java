package dk.kino.kino.service;

import dk.kino.kino.exception.NotFoundException;
import dk.kino.kino.model.Reservation;
import dk.kino.kino.model.ReservationSeat;
import dk.kino.kino.model.Seat;
import dk.kino.kino.model.Showing;
import dk.kino.kino.model.Theatre;
import dk.kino.kino.repository.ReservationRepository;
import dk.kino.kino.repository.ReservationSeatRepository;
import dk.kino.kino.repository.SeatRepository;
import dk.kino.kino.repository.ShowingRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationSeatRepository reservationSeatRepository;

    @Mock
    private ShowingRepository showingRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private ReservationService reservationService;


    // Laver en reservation med navn, telefon og en forestilling
    private Reservation createRequest(Showing showing) {
        return new Reservation("Test Kunde", "12345678", showing);
    }



    @Test
    void getAllReservationsReturnsAllReservations() {

        List<Reservation> reservations = List.of(new Reservation(), new Reservation());

        when(reservationRepository.findAll())
                .thenReturn(reservations);

        List<Reservation> result = reservationService.getAllReservations();

        assertEquals(2, result.size());
    }



    // Tester at en reservation bliver oprettet når alt er i orden
    @Test
    void createReservationSavesReservationAndSeatsWhenEverythingIsValid() {

        Theatre theatre = mock(Theatre.class);
        Showing showing = mock(Showing.class);
        Seat seat = mock(Seat.class);

        when(theatre.getId()).thenReturn(1L);
        when(showing.getId()).thenReturn(1L);
        when(showing.getTheatre()).thenReturn(theatre);
        when(seat.getId()).thenReturn(5L);
        when(seat.getTheatre()).thenReturn(theatre);

        Reservation request = createRequest(showing);

        when(showingRepository.findById(1L))
                .thenReturn(Optional.of(showing));

        when(seatRepository.findAllById(List.of(5L)))
                .thenReturn(List.of(seat));

        when(reservationSeatRepository.findByReservationShowingId(1L))
                .thenReturn(List.of());

        when(reservationRepository.save(request))
                .thenReturn(request);

        Reservation result = reservationService.createReservation(request, List.of(5L));

        assertEquals("Test Kunde", result.getCustomerName());
        verify(reservationRepository).save(request);
        verify(reservationSeatRepository).save(any(ReservationSeat.class));
    }


    // Tester at en reservation uden kundenavn bliver afvist
    @Test
    void createReservationThrowsWhenCustomerNameIsMissing() {

        Reservation request = createRequest(mock(Showing.class));
        request.setCustomerName("");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reservationService.createReservation(request, List.of(5L))
        );

        assertEquals("Kundenavn skal udfyldes", exception.getMessage());
        verify(reservationRepository, never()).save(any());
    }


    // Tester at en reservation uden telefonnummer bliver afvist
    @Test
    void createReservationThrowsWhenPhoneIsMissing() {

        Reservation request = createRequest(mock(Showing.class));
        request.setPhone("");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reservationService.createReservation(request, List.of(5L))
        );

        assertEquals("Telefonnummer skal udfyldes", exception.getMessage());
        verify(reservationRepository, never()).save(any());
    }


    // Tester at en reservation uden sæder bliver afvist
    @Test
    void createReservationThrowsWhenNoSeatsAreChosen() {

        Showing showing = mock(Showing.class);
        when(showing.getId()).thenReturn(1L);

        Reservation request = createRequest(showing);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reservationService.createReservation(request, List.of())
        );

        assertEquals("Vælg mindst ét sæde", exception.getMessage());
        verify(reservationRepository, never()).save(any());
    }


    // Tester at en reservation til en ukendt forestilling bliver afvist
    @Test
    void createReservationThrowsWhenShowingDoesNotExist() {

        Showing showing = mock(Showing.class);
        when(showing.getId()).thenReturn(99L);

        Reservation request = createRequest(showing);

        when(showingRepository.findById(99L))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reservationService.createReservation(request, List.of(5L))
        );

        assertEquals("Forestillingen findes ikke", exception.getMessage());
        verify(reservationRepository, never()).save(any());
    }


    // Tester at et sæde, der allerede er reserveret, bliver afvist
    @Test
    void createReservationThrowsWhenSeatIsAlreadyReserved() {

        Theatre theatre = mock(Theatre.class);
        Showing showing = mock(Showing.class);
        Seat seat = mock(Seat.class);

        when(theatre.getId()).thenReturn(1L);
        when(showing.getId()).thenReturn(1L);
        when(showing.getTheatre()).thenReturn(theatre);
        when(seat.getId()).thenReturn(5L);
        when(seat.getTheatre()).thenReturn(theatre);
        when(seat.getRowNumber()).thenReturn(1);
        when(seat.getSeatNumber()).thenReturn(2);

        Reservation request = createRequest(showing);

        when(showingRepository.findById(1L))
                .thenReturn(Optional.of(showing));

        when(seatRepository.findAllById(List.of(5L)))
                .thenReturn(List.of(seat));

        when(reservationSeatRepository.findByReservationShowingId(1L))
                .thenReturn(List.of(new ReservationSeat(new Reservation(), seat)));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> reservationService.createReservation(request, List.of(5L))
        );

        assertEquals("Række 1, sæde 2 er allerede reserveret", exception.getMessage());
        verify(reservationRepository, never()).save(any());
    }


    // Tester at en reservation og dens sæder bliver slettet ved annullering
    @Test
    void cancelReservationDeletesReservationAndSeats() {

        Reservation reservation = mock(Reservation.class);
        when(reservation.getId()).thenReturn(1L);

        List<ReservationSeat> seats = List.of(new ReservationSeat(), new ReservationSeat());

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        when(reservationSeatRepository.findByReservationId(1L))
                .thenReturn(seats);

        reservationService.cancelReservation(1L);

        verify(reservationSeatRepository).deleteAll(seats);
        verify(reservationRepository).delete(reservation);
    }


    // Tester at en ukendt reservation giver NotFoundException ved annullering
    @Test
    void cancelReservationThrowsNotFoundWhenReservationDoesNotExist() {

        when(reservationRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> reservationService.cancelReservation(99L)
        );

        verify(reservationRepository, never()).delete(any());
    }
}
