package dk.kino.kino.controller;

import dk.kino.kino.model.Movie;
import dk.kino.kino.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovieController {

    @Autowired
    MovieService movieService;

    @GetMapping("/movies")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/movies/{id}")
    public Movie getMovie(@PathVariable Long id) {
        return movieService.getMovie(id);
    }

    @PostMapping("/addMovie")
    @ResponseStatus(HttpStatus.CREATED)
    public Movie addMovie(@RequestBody Movie movie){
        return movieService.addMovie(movie);
    }

    @PutMapping("/editMovie/{id}")
    public Movie editMovie (@PathVariable Long id, @RequestBody Movie movie){
       return movieService.editMovie(id, movie);
    }

    @DeleteMapping("/deleteMovie/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie (@PathVariable Long id){
        movieService.deleteMovie(id);
    }

}