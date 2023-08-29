
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.gridnine.testing.service.FlightExceptionService.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FlightExceptionServiceTest {

    @Test
    public void whenСomparingАrrivalАndDepartureTimesButFlightsIsNullThrowsException() {
        List<Flight> flights = new ArrayList<>();
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> arrivalTimeBeforeDepartureDate (flights));
        String expectedMessage = "Данные для обработки не предоставлены.";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }
    @Test
    public void whenСomparingАrrivalАndDepartureTimes() {
        Segment s = new Segment(LocalDateTime.parse("2023-08-31T19:05"),
                LocalDateTime.parse("2023-08-31T18:05"));
        List<Flight> flights = Arrays.asList(new Flight(Arrays.asList(s)));
        List<Flight> expected = new ArrayList<>();
        List<Flight> actual = arrivalTimeBeforeDepartureDate (flights);
        assertThat(actual.equals(expected));
    }
    @Test
    public void whenCheckDepartureBeforeCurrentTimeButFlightsIsEmptyThrowsException() {
        List<Flight> flights = new ArrayList<>();
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> departureBeforeCurrentTime(flights));
        String expectedMessage = "Данные для обработки не предоставлены.";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }
    @Test
    public void whenCheckDepartureBeforeCurrentTime() {
        Segment s = new Segment(LocalDateTime.parse("2023-08-31T19:05"),
                LocalDateTime.parse("2023-08-31T18:05"));
        List<Flight> flights = Arrays.asList(new Flight(Arrays.asList(s)));
        List<Flight> expected = new ArrayList<>();
        List<Flight> actual = departureBeforeCurrentTime(flights);
        assertThat(actual.equals(expected));
    }
    @Test
    public void whenMoreWaitingTimeOnTheGroundButFlightsIsEmptyThrowsException() {
        List<Flight> flights = new ArrayList<>();
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> moreWaitingTimeOnTheGround(flights));
        String expectedMessage = "Данные не предоставлены или не корректны.";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }
    @Test
    public void whenFlightsThatComplyWithTheRuleButFlightsIsEmptyThrowsException() {
        List<Flight> flights = new ArrayList<>(); List<Flight> exFlights = new ArrayList<>();
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> flightsThatComplyWithTheRule (flights, exFlights));
        String expectedMessage = "Данные не предоставлены или не корректны.";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }
    @Test
    public void whenFlightsThatComplyWithTheRule() {
        Segment s1 = new Segment(LocalDateTime.parse("2023-08-31T16:05"),
                LocalDateTime.parse("2023-08-31T18:05"));
        Segment s2 = new Segment(LocalDateTime.parse("2023-08-31T19:05"),
                LocalDateTime.parse("2023-08-31T20:05"));
        List<Flight> flights = Arrays.asList(new Flight(Arrays.asList(s1, s2)));
        List<Flight> exFlights = Arrays.asList(new Flight(Arrays.asList(s1)));
        List<Flight> expected = Arrays.asList(new Flight(Arrays.asList(s2)));
        List<Flight> actual = flightsThatComplyWithTheRule (flights, exFlights);
        assertThat(actual.equals(expected));
    }

}
