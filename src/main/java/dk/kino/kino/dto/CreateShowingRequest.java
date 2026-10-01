package dk.kino.kino.dto;

import java.time.LocalDateTime;

public class CreateShowingRequest {

    private LocalDateTime dateTime;
    private Long movieId;
    private Long theatreId;

    public CreateShowingRequest( ) {
    }

    public LocalDateTime getDateTime( ) {
        return dateTime;
    }

    public void setDateTime( LocalDateTime dateTime ) {
        this.dateTime = dateTime;
    }

    public Long getMovieId( ) {
        return movieId;

    }

    public void setMovieId( Long movieId ) {
        this.movieId = movieId;

    }

    public Long getTheatreId() {
        return theatreId;

    }

    public void setTheatreId( Long theatreId ) {
        this.theatreId = theatreId;

    }
}
