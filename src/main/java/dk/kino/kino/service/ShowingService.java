package dk.kino.kino.service;

import dk.kino.kino.model.Movie;
import dk.kino.kino.model.Showing;
import dk.kino.kino.model.Theatre;
import dk.kino.kino.repository.MovieRepository;
import dk.kino.kino.repository.ShowingRepository;
import dk.kino.kino.repository.TheatreRepository;
import org.springframework.stereotype.Service;
import dk.kino.kino.exception.NotFoundException;


@Service
public class ShowingService {

    private final ShowingRepository showingRepository;
    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;

    public ShowingService(
            ShowingRepository showingRepository,
            MovieRepository movieRepository,
            TheatreRepository theatreRepository) {

        this.showingRepository = showingRepository;
        this.movieRepository = movieRepository;
        this.theatreRepository = theatreRepository;
    }

    public Showing createShowing(Showing request) {

        Movie movie = movieRepository
                .findById(request.getMovie().getId())
                .orElseThrow(() ->
                        new NotFoundException("Filmen blev ikke fundet"));

        Theatre theatre = theatreRepository
                .findById(request.getTheatre().getId())
                .orElseThrow(() ->
                        new NotFoundException("Salen blev ikke fundet"));

        Showing showing = new Showing(
                request.getDateTime(),
                movie,
                theatre
        );

        return showingRepository.save(showing);
    }

    public Showing updateShowing(Long id, Showing request) {

        Showing existingShowing = showingRepository.findById(id)
                .orElseThrow(( ) ->
                        new NotFoundException("Forestillingen blev ikke fundet"));

        Movie movie = movieRepository
                .findById(request.getMovie().getId())
                .orElseThrow(( ) ->
                        new NotFoundException("Filmen blev ikke fundet"));

        Theatre theatre = theatreRepository
                .findById(request.getTheatre().getId())
                .orElseThrow(( ) ->
                        new NotFoundException("Salen blev ikke fundet"));

        existingShowing.setDateTime(request.getDateTime());
        existingShowing.setMovie(movie);
        existingShowing.setTheatre(theatre);

        return showingRepository.save(existingShowing);
    }

    public void deleteShowing(Long id) {

        Showing existingShowing = showingRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Forestillingen blev ikke fundet"));

        showingRepository.delete(existingShowing);

    }
}
