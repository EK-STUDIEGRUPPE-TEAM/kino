package dk.kino.kino.service;

import dk.kino.kino.model.Movie;
import dk.kino.kino.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository repository;

    @InjectMocks
    private MovieService service;

    @Test
    void getAllMoviesShouldReturnAll() {
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
    void addMovieShouldAddMovie() {
        // arrange

    }

    @Test
    void editMovie() {
    }

    @Test
    void deleteMovie() {
    }
}