package dk.kino.kino.service;

import dk.kino.kino.exception.NotFoundException;
import dk.kino.kino.model.Movie;
import dk.kino.kino.repository.MovieRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository repository;

    @InjectMocks
    private MovieService service;

    @Test
    void getAllMovies_shouldReturnAll() {
        // arrange
        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        when(repository.findAll()).thenReturn(List.of(movie1, movie2));

        //act
        List<Movie> result = service.getAllMovies();

        //assert
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
    }

    @Test
    void getMovie_shouldReturnMovie() {
        // arrange
        Movie movie1 = new Movie();
        movie1.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(movie1));

        // act
        Movie result = service.getMovie(1L);

        // assert
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getMovie_shouldThrowException_whenNotFound() {
        // arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // act + asserrt
        assertThrows(NotFoundException.class, () -> service.getMovie(1L));
    }

    @Test
    void addMovie_shouldCallRepository() {
        // arrange
        Movie movie1 = new Movie();

        //act
        service.addMovie(movie1);

        //assert
        verify(repository, times(1)).save(movie1);
    }

    @Test
    void editMovie_shouldEditMovie() {
        //arrange
        Movie movie1 = new Movie();
        movie1.setDuration(222);

        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(movie1)).thenReturn(movie1);

        //act
        Movie result = service.editMovie(1L, movie1);

        //assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDuration()).isEqualTo(222);
    }

    @Test
    void editMovie_shouldThrowException_whenNotFound() {
        //arrange
        Movie movie1 = new Movie();
        when(repository.existsById(1L)).thenReturn(false);

        //act
        assertThrows(NotFoundException.class, () -> service.editMovie(1L, movie1));

        //assert
        verify(repository, never()).save(any(Movie.class));
    }

    @Test
    void deleteMovie_shouldDeleteMovie() {
        //arrange
        Movie movie1 = new Movie();
        movie1.setId(1L);

        when(repository.existsById(movie1.getId())).thenReturn(true);

        //act
        service.deleteMovie(1L);

        //assert
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteMovie_shouldThrowException_whenNotFound() {
        //arrange
        when(repository.existsById(1L)).thenReturn(false);

        //act
        assertThrows(NotFoundException.class, () -> service.deleteMovie(1L));

        //assert
        verify(repository, never()).deleteById(any(Long.class));
    }
}