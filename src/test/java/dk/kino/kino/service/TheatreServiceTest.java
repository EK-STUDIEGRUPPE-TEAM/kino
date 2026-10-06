package dk.kino.kino.service;

import dk.kino.kino.model.Movie;
import dk.kino.kino.model.Seat;
import dk.kino.kino.model.Theatre;
import dk.kino.kino.repository.SeatRepository;
import dk.kino.kino.repository.TheatreRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TheatreServiceTest {


    @Mock
    private TheatreRepository theatreRepository;

    @Mock
    private SeatRepository seatRepository;


    @InjectMocks
    private TheatreService theatreService;


    @Test
    void getTheatreShouldReturnTheatre() {

        //arrange
        Theatre theatre = new Theatre();
        theatre.setId(2L);
        when(theatreRepository.findById(2L)).thenReturn(Optional.of(theatre));
        //act

        Theatre result = theatreService.getTheatre(2L);

        //assert
        assertThat(result.getId()).isEqualTo(2L);

    }

    @Test
    void getAllTheatresShouldReturnAllTheatres() {

        // arrange
        Theatre theatre = new Theatre();
        Theatre theatre1 = new Theatre();
        when(theatreRepository.findAll()).thenReturn(List.of(theatre, theatre1));

        //act
        List<Theatre> result = theatreService.getAllTheatre();

        //assert
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);


    }

    @Test
    void createTheatreShouldCreateTheatre() {

        // Arrange
        Theatre theatre = new Theatre();
        theatre.setName("Sal 1");
        theatre.setNumberOfRows(3);
        theatre.setSeatsPerRow(4);

        when(theatreRepository.save(any(Theatre.class)))
                .thenReturn(theatre);

        // Act
        Theatre result = theatreService.createTheatre(theatre);

        // Assert
        assertEquals("Sal 1", result.getName());

        verify(seatRepository, times(12))
                .save(any(Seat.class));

        verify(theatreRepository)
                .save(any(Theatre.class));
    }

    @Test
    void deleteTheatreShouldDeleteTheatre() {

        //Arrange
        Long theatreId = 2L;

        //Act

        theatreService.deleteTheatre(theatreId);



        //Assert
        verify(theatreRepository).deleteById(theatreId);

    }

    @Test
    void updateTheatreShouldUpdateTheatre() {

        Theatre theatre1 = new Theatre();
        theatre1.setName("Sal 1");

        when(theatreRepository.save(theatre1)).thenReturn(theatre1);

        //act
        Theatre result = theatreService.updateTheatre(theatre1);

        //assert
        assertThat(result.getName()).isEqualTo("Sal 1");

        verify(theatreRepository).save(theatre1);
    }

}



