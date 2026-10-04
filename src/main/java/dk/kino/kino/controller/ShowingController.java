package dk.kino.kino.controller;

import dk.kino.kino.model.Showing;
import dk.kino.kino.service.ShowingService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/showings")
public class ShowingController {

    private final ShowingService showingService;


    // Dependency injection
    public ShowingController(ShowingService showingService) {
        this.showingService = showingService;
    }


    // Opretter en forestilling
    @PostMapping
    public ResponseEntity<Showing> createShowing(
            @RequestBody Showing request) {

        Showing savedShowing =
                showingService.createShowing(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedShowing);
    }


    // Redigerer en eksisterende forestilling
    @PutMapping("/{id}")
    public ResponseEntity<Showing> updateShowing(
            @PathVariable Long id,
            @RequestBody Showing request) {

        Showing updatedShowing =
                showingService.updateShowing(id, request);

        return ResponseEntity.ok(updatedShowing);
    }


    // Sletter en eksisterende forestilling
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowing(
            @PathVariable Long id) {

        showingService.deleteShowing(id);

        return ResponseEntity.noContent().build();
    }
}