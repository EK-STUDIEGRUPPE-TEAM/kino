package dk.kino.kino.service;

import dk.kino.kino.exception.NotFoundException;
import dk.kino.kino.model.Movie;
import dk.kino.kino.model.Showing;
import dk.kino.kino.model.Theatre;
import dk.kino.kino.repository.MovieRepository;
import dk.kino.kino.repository.ShowingRepository;
import dk.kino.kino.repository.TheatreRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShowingServiceTest {

    @Mock
    private ShowingRepository showingRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private TheatreRepository theatreRepository;

    @InjectMocks
    private ShowingService showingService;


    // Tester at en forestilling bliver oprettet når film og sal findes
    @Test
    void createShowingReturnsShowingWhenMovieAndTheatreExist() {

        Movie movie = mock(Movie.class);
        Theatre theatre = mock(Theatre.class);

        when(movie.getId()).thenReturn(1L);
        when(theatre.getId()).thenReturn(1L);

        Showing request = new Showing();
        request.setMovie(movie);
        request.setTheatre(theatre);

        when(movieRepository.findById(1L))
                .thenReturn(Optional.of(movie));

        when(theatreRepository.findById(1L))
                .thenReturn(Optional.of(theatre));

        when(showingRepository.save(any(Showing.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Showing result = showingService.createShowing(request);

        assertEquals(movie, result.getMovie());
        assertEquals(theatre, result.getTheatre());

        verify(showingRepository).save(any(Showing.class));
    }


    // Tester at en ukendt film giver NotFoundException ved oprettelse
    @Test
    void createShowingThrowsNotFoundWhenMovieDoesNotExist() {

        Movie movie = mock(Movie.class);

        when(movie.getId()).thenReturn(99L);

        Showing request = new Showing();
        request.setMovie(movie);

        when(movieRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> showingService.createShowing(request)
        );
    }


    // Tester at en ukendt sal giver NotFoundException ved oprettelse
    @Test
    void createShowingThrowsNotFoundWhenTheatreDoesNotExist() {

        Movie movie = mock(Movie.class);
        Theatre theatre = mock(Theatre.class);

        when(movie.getId()).thenReturn(1L);
        when(theatre.getId()).thenReturn(99L);

        Showing request = new Showing();
        request.setMovie(movie);
        request.setTheatre(theatre);

        when(movieRepository.findById(1L))
                .thenReturn(Optional.of(movie));

        when(theatreRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> showingService.createShowing(request)
        );
    }


    // Tester at en eksisterende forestilling kan opdateres
    @Test
    void updateShowingReturnsUpdatedShowingWhenEverythingExists() {

        Showing existingShowing = new Showing();

        Movie movie = mock(Movie.class);
        Theatre theatre = mock(Theatre.class);

        when(movie.getId()).thenReturn(1L);
        when(theatre.getId()).thenReturn(1L);

        LocalDateTime newDateTime =
                LocalDateTime.of(2026, 10, 6, 20, 0);

        Showing request = new Showing();
        request.setDateTime(newDateTime);
        request.setMovie(movie);
        request.setTheatre(theatre);

        when(showingRepository.findById(1L))
                .thenReturn(Optional.of(existingShowing));

        when(movieRepository.findById(1L))
                .thenReturn(Optional.of(movie));

        when(theatreRepository.findById(1L))
                .thenReturn(Optional.of(theatre));

        when(showingRepository.save(any(Showing.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Showing result =
                showingService.updateShowing(1L, request);

        assertEquals(newDateTime, result.getDateTime());
        assertEquals(movie, result.getMovie());
        assertEquals(theatre, result.getTheatre());
    }


    // Tester at en ukendt forestilling giver NotFoundException
    @Test
    void updateShowingThrowsNotFoundWhenShowingDoesNotExist() {

        Showing request = new Showing();

        when(showingRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> showingService.updateShowing(99L, request)
        );
    }


    // Tester at en ukendt film giver NotFoundException ved opdatering
    @Test
    void updateShowingThrowsNotFoundWhenMovieDoesNotExist() {

        Showing existingShowing = new Showing();

        Movie movie = mock(Movie.class);

        when(movie.getId()).thenReturn(99L);

        Showing request = new Showing();
        request.setMovie(movie);

        when(showingRepository.findById(1L))
                .thenReturn(Optional.of(existingShowing));

        when(movieRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> showingService.updateShowing(1L, request)
        );
    }


    // Tester at en ukendt sal giver NotFoundException ved opdatering
    @Test
    void updateShowingThrowsNotFoundWhenTheatreDoesNotExist() {

        Showing existingShowing = new Showing();

        Movie movie = mock(Movie.class);
        Theatre theatre = mock(Theatre.class);

        when(movie.getId()).thenReturn(1L);
        when(theatre.getId()).thenReturn(99L);

        Showing request = new Showing();
        request.setMovie(movie);
        request.setTheatre(theatre);

        when(showingRepository.findById(1L))
                .thenReturn(Optional.of(existingShowing));

        when(movieRepository.findById(1L))
                .thenReturn(Optional.of(movie));

        when(theatreRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class ,
                ( ) -> showingService.updateShowing(1L , request)
        );

    }

    // Tester at en eksisterende forestilling bliver slettet
    @Test
    void deleteShowingDeletesShowingWhenShowingExists() {

        Showing existingShowing = new Showing();

        when(showingRepository.findById(1L))
                .thenReturn(Optional.of(existingShowing));

        showingService.deleteShowing(1L);

        verify(showingRepository).delete(existingShowing);
    }


    // Tester at en ukendt forestilling giver NotFoundException ved sletning
    @Test
    void deleteShowingThrowsNotFoundWhenShowingDoesNotExist() {

        when(showingRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> showingService.deleteShowing(99L)
        );
    }
}
