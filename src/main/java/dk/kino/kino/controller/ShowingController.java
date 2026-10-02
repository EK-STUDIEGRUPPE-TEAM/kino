package dk.kino.kino.controller;

import dk.kino.kino.model.Showing;
import dk.kino.kino.repository.MovieRepository;
import dk.kino.kino.repository.ShowingRepository;
import dk.kino.kino.repository.TheatreRepository;
import org.springframework.http.ResponseEntity;
import dk.kino.kino.model.Movie;
import dk.kino.kino.model.Theatre;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping ( "/api/showings" )
public class ShowingController {

    private final ShowingRepository showingRepository;
    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;

    // dependency injection
    public ShowingController( ShowingRepository showingRepository , MovieRepository movieRepository , TheatreRepository theatreRepository ) {

        this.showingRepository = showingRepository; //gemmer nye forestillinger
        this.movieRepository = movieRepository; // finder filmene
        this.theatreRepository = theatreRepository; // finder biografsalen for forestillingen

    }
    @GetMapping
    public List < Showing > getAllShowings( ) {
        return showingRepository.findAll();
    }

    @PostMapping
    public ResponseEntity < Showing > createShowing(@RequestBody Showing request) {

        Optional <Movie> movie =
                movieRepository.findById(request.getMovie().getId());

        Optional <Theatre> theatre =
                theatreRepository.findById(request.getTheatre().getId());

        if (movie.isEmpty() || theatre.isEmpty()) {
            return ResponseEntity.badRequest().build();

        }

        Showing showing = new Showing(
                request.getDateTime() ,
                movie.get() ,
                theatre.get()
        );

        Showing savedShowing = showingRepository.save(showing);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedShowing);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Showing> updateShowing(
            @PathVariable Long id,
            @RequestBody Showing request) {

        Optional < Showing > existingShowing =
                showingRepository.findById(id);

        if ( existingShowing.isEmpty() ) {
            return ResponseEntity.notFound().build();
        }

        Optional < Movie > movie =
                movieRepository.findById(request.getMovie().getId());

        Optional < Theatre > theatre =
                theatreRepository.findById(request.getTheatre().getId());

        if ( movie.isEmpty() || theatre.isEmpty() ) {
            return ResponseEntity.badRequest().build();
        }

        Showing showing = existingShowing.get();

        showing.setDateTime(request.getDateTime());
        showing.setMovie(movie.get());
        showing.setTheatre(theatre.get());

        Showing savedShowing = showingRepository.save(showing);

        return ResponseEntity.ok(savedShowing);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowing(@PathVariable Long id) {

        Optional<Showing> existingShowing =
                showingRepository.findById(id);

        if (existingShowing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        showingRepository.delete(existingShowing.get());

        return ResponseEntity.noContent().build();


    }
}




