package dk.kino.kino.service;

import dk.kino.kino.model.Seat;
import dk.kino.kino.model.Theatre;
import dk.kino.kino.repository.SeatRepository;
import dk.kino.kino.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TheatreService {


    @Autowired

    TheatreRepository theatreRepository;
    SeatRepository seatRepository;

    public TheatreService(TheatreRepository theatreRepository, SeatRepository seatRepository) {
        this.theatreRepository = theatreRepository;
        this.seatRepository = seatRepository;
    }

    public Theatre updateTheatre(Theatre theatre) {

        return theatreRepository.save(theatre);
    }

    public Theatre createTheatre(Theatre theatre) {

        theatreRepository.save(theatre);
        for (int row = 1; row <= theatre.getNumberOfRows(); row++) {
            for (int seatNumber = 1; seatNumber <= theatre.getSeatsPerRow(); seatNumber++) {

                Seat seat = new Seat();
                seat.setRowNumber(row);
                seat.setSeatNumber(seatNumber);
                seat.setTheatre(theatre);

                seatRepository.save(seat);
            }
        }

        return theatre;
    }

    public void deleteTheatre(long id) {
        theatreRepository.deleteById(id);
    }

    public Theatre getTheatre(long id) {
        return theatreRepository.findById(id).orElse(null);
    }

    public List<Theatre> getAllTheatre() {
        return theatreRepository.findAll();
    }

}
