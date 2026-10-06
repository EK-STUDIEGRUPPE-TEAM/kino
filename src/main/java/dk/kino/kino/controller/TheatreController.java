package dk.kino.kino.controller;

import dk.kino.kino.model.Movie;
import dk.kino.kino.model.Theatre;
import dk.kino.kino.service.TheatreService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theatres")
public class TheatreController {

    private final TheatreService theatreService;

    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    @GetMapping
    public List<Theatre> getAllTheatres() {
        return theatreService.getAllTheatre();
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Theatre addTheatre(@RequestBody Theatre theatre){
        return theatreService.createTheatre(theatre);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheatre (@PathVariable Long id){
        theatreService.deleteTheatre(id);
    }

    @GetMapping("/{id}")
    public Theatre getTheatre(@PathVariable Long id){
        return theatreService.getTheatre(id);
    }


}