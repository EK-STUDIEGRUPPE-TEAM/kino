package dk.kino.kino.controller;

import dk.kino.kino.model.Movie;
import dk.kino.kino.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovieController {

    @Autowired
    MovieRepository movieRepository;

    @GetMapping("/movies")
    public List<Movie> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies;
    }

    @PostMapping("/addMovie")
    @ResponseStatus(HttpStatus.CREATED)
    public Movie addMovie(@RequestBody Movie movie){
        return movieRepository.save(movie);
    }

    @PutMapping("/editMovie/{id}")
    public ResponseEntity<Movie> editMovie (@PathVariable Long id, @RequestBody Movie movie){
        movie.setId(id);
        // tjekker ik om film eksisterer endnu men laves i service lag
        return new ResponseEntity<>(movieRepository.save(movie), HttpStatus.OK);
    }

    @DeleteMapping("/deleteMovie/{id}")
    public ResponseEntity<String> deleteMovie (@PathVariable Long id){
        movieRepository.deleteById(id);
        return ResponseEntity.ok("Movie_deleted");
    }

}