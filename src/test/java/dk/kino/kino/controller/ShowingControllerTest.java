package dk.kino.kino.controller;

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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShowingControllerTest {

    @Mock
    private ShowingRepository showingRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private TheatreRepository theatreRepository;

    @InjectMocks
    private ShowingController showingController;

    // Tester at en gyldig film og sal giver 201 Created
    @Test
    void createShowingReturnsCreatedWhenMovieAndTheatreExist() {

        Movie movie = mock(Movie.class);
        Theatre theatre = mock(Theatre.class);

        when(movie.getId()).thenReturn(1L);
        when(theatre.getId()).thenReturn(1L);

        Showing request = new Showing();
        request.setDateTime(LocalDateTime.of(2026, 10, 5, 19, 30));
        request.setMovie(movie);
        request.setTheatre(theatre);

        when(movieRepository.findById(1L))
                .thenReturn(Optional.of(movie));

        when(theatreRepository.findById(1L))
                .thenReturn(Optional.of(theatre));

        when(showingRepository.save(any(Showing.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<Showing> response =
                showingController.createShowing(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    // Tester at en ukendt film giver 400 Bad Request
    @Test
    void createShowingReturnsBadRequestWhenMovieDoesNotExist() {

        Movie movie = mock(Movie.class);
        Theatre theatre = mock(Theatre.class);

        when(movie.getId()).thenReturn(99L);
        when(theatre.getId()).thenReturn(1L);

        Showing request = new Showing();
        request.setDateTime(LocalDateTime.of(2026, 10, 5, 19, 30));
        request.setMovie(movie);
        request.setTheatre(theatre);

        when(movieRepository.findById(99L))
                .thenReturn(Optional.empty());

        when(theatreRepository.findById(1L))
                .thenReturn(Optional.of(theatre));

        ResponseEntity<Showing> response =
                showingController.createShowing(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    // Tester at en ukendt sal giver 400 Bad Request
    @Test
    void createShowingReturnsBadRequestWhenTheatreDoesNotExist() {

        Movie movie = mock(Movie.class);
        Theatre theatre = mock(Theatre.class);

        when(movie.getId()).thenReturn(1L);
        when(theatre.getId()).thenReturn(99L);

        Showing request = new Showing();
        request.setDateTime(LocalDateTime.of(2026 , 10 , 5 , 19 , 30));
        request.setMovie(movie);
        request.setTheatre(theatre);

        when(movieRepository.findById(1L))
                .thenReturn(Optional.of(movie));

        when(theatreRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseEntity < Showing > response =
                showingController.createShowing(request);

        assertEquals(HttpStatus.BAD_REQUEST , response.getStatusCode());

    }
    // Tester at en eksisterende forestilling kan redigeres -> 200 OK
    @Test
    void updateShowingReturnsOkWhenShowingMovieAndTheatreExist() {

        Movie movie = mock(Movie.class);
        Theatre theatre = mock(Theatre.class);

        when(movie.getId()).thenReturn(1L);
        when(theatre.getId()).thenReturn(1L);

        Showing existingShowing = new Showing();

        LocalDateTime newDateTime =
                LocalDateTime.of(2026 , 10 , 6 , 20 , 0);

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

        ResponseEntity < Showing > response =
                showingController.updateShowing(1L , request);

        assertEquals(HttpStatus.OK , response.getStatusCode());
        assertEquals(newDateTime , response.getBody().getDateTime());
    }

    // Tester at en ikke-eksisterende forestilling giver 404 Not Found
    @Test
    void updateShowingReturnsNotFoundWhenShowingDoesNotExist() {

        Showing request = new Showing();

        when(showingRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseEntity < Showing > response =
                showingController.updateShowing(99L , request);

        assertEquals(HttpStatus.NOT_FOUND , response.getStatusCode());

    }

    // Tester at en eksisterende forestilling med ukendt film giver 400 Bad Request
    @Test
    void updateShowingReturnsBadRequestWhenMovieDoesNotExist() {

        Showing existingShowing = new Showing();

        Movie movie = mock(Movie.class);
        Theatre theatre = mock(Theatre.class);

        when(movie.getId()).thenReturn(99L);
        when(theatre.getId()).thenReturn(1L);

        Showing request = new Showing();
        request.setDateTime(LocalDateTime.of(2026 , 10 , 6 , 20 , 0));
        request.setMovie(movie);
        request.setTheatre(theatre);

        when(showingRepository.findById(1L))
                .thenReturn(Optional.of(existingShowing));

        when(movieRepository.findById(99L))
                .thenReturn(Optional.empty());

        when(theatreRepository.findById(1L))
                .thenReturn(Optional.of(theatre));

        ResponseEntity < Showing > response =
                showingController.updateShowing(1L , request);

        assertEquals(HttpStatus.BAD_REQUEST , response.getStatusCode());

    }
    // Tester at en eksisterende forestilling med ukendt sal giver 400 Bad Request
    @Test
    void updateShowingReturnsBadRequestWhenTheatreDoesNotExist() {

        Showing existingShowing = new Showing();

        Movie movie = mock(Movie.class);
        Theatre theatre = mock(Theatre.class);

        when(movie.getId()).thenReturn(1L);
        when(theatre.getId()).thenReturn(99L);

        Showing request = new Showing();
        request.setDateTime(LocalDateTime.of(2026 , 10 , 6 , 20 , 0));
        request.setMovie(movie);
        request.setTheatre(theatre);

        when(showingRepository.findById(1L))
                .thenReturn(Optional.of(existingShowing));

        when(movieRepository.findById(1L))
                .thenReturn(Optional.of(movie));

        when(theatreRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseEntity < Showing > response =
                showingController.updateShowing(1L , request);

        assertEquals(HttpStatus.BAD_REQUEST , response.getStatusCode());

    }

// Tester at en eksisterende forestilling kan slettes -> 204 No Content
        @Test
        void deleteShowingReturnsNoContentWhenShowingExists() {

            Showing existingShowing = new Showing();

            when(showingRepository.findById(1L))
                    .thenReturn(Optional.of(existingShowing));

            ResponseEntity < Void > response =
                    showingController.deleteShowing(1L);

            assertEquals(HttpStatus.NO_CONTENT , response.getStatusCode());

            verify(showingRepository).delete(existingShowing);

        }

    // Tester at en ikke-eksisterende forestilling giver 404 Not Found ved sletning
    @Test
    void deleteShowingReturnsNotFoundWhenShowingDoesNotExist() {

        when(showingRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseEntity<Void> response =
                showingController.deleteShowing(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}







