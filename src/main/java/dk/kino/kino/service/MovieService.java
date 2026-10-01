package dk.kino.kino.service;

import dk.kino.kino.exception.NotFoundException;
import dk.kino.kino.model.Movie;
import dk.kino.kino.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    public Movie addMovie(Movie movie){
        return movieRepository.save(movie);
    }

    public Movie editMovie(Long id, Movie movie) {
        if (!movieRepository.existsById(id)) {
            throw new NotFoundException("Movie with id " + id + " not found.");
        }
        movie.setId(id);
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id){
        if (!movieRepository.existsById(id)) {
            throw new NotFoundException("Movie with id " + id + " not found.");
        }
        movieRepository.deleteById(id);
    }
}
