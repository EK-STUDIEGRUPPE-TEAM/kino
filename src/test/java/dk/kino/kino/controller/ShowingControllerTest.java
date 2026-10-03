package dk.kino.kino.controller;

import dk.kino.kino.model.Showing;
import dk.kino.kino.service.ShowingService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShowingControllerTest {

    @Mock
    private ShowingService showingService;

    @InjectMocks
    private ShowingController showingController;


    // Tester at en gyldig forestilling giver 201 Created
    @Test
    void createShowingReturnsCreatedWhenShowingIsCreated() {

        Showing request = new Showing();
        Showing savedShowing = new Showing();

        when(showingService.createShowing(request))
                .thenReturn(savedShowing);

        ResponseEntity<Showing> response =
                showingController.createShowing(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedShowing, response.getBody());
    }

    // Tester at en forestilling kan opdateres -> 200 OK
    @Test
    void updateShowingReturnsOkWhenShowingIsUpdated() {

        Showing request = new Showing();
        Showing updatedShowing = new Showing();

        when(showingService.updateShowing(1L, request))
                .thenReturn(updatedShowing);

        ResponseEntity<Showing> response =
                showingController.updateShowing(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedShowing, response.getBody());
    }


    // Tester at en forestilling kan slettes -> 204 No Content
    @Test
    void deleteShowingReturnsNoContentWhenShowingIsDeleted() {

        ResponseEntity<Void> response =
                showingController.deleteShowing(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(showingService).deleteShowing(1L);
    }
}