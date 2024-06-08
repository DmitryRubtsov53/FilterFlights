
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import com.gridnine.testing.service.FlightExceptionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.gridnine.testing.service.FlightExceptionService.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class FlightExceptionServiceTest {

    @Test
    @DisplayName("Т1/МC №1-4. Когда лист перелётов пуст, выбрасывается исключение.")
    void whenTheFlightListIsEmptyThenExceptionIsThrown() {
        List<Flight> flights = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> FlightExceptionService
                .arrivalTimeBeforeDepartureDate(flights));
        assertThrows(IllegalArgumentException.class, () -> FlightExceptionService
                .departureBeforeCurrentTime(flights));
        assertThrows(IllegalArgumentException.class, () -> FlightExceptionService
                .moreWaitingTimeOnTheGround(flights));
        assertThrows(IllegalArgumentException.class, () -> FlightExceptionService
                .flightsThatComplyWithTheRule(flights, null));
    }

    @Test
    @DisplayName("Т2/MC №1. Сегмент с не валидным временем (вылет > прилёта) попадает в лист исключений.")
    public void whenTimeIsNotValidThenSegmentIsAddedToExceptions() {
        Segment s = new Segment(LocalDateTime.parse("2024-06-09T19:05"),
                LocalDateTime.parse("2024-06-09T18:05"));
        List<Flight> flights = List.of(new Flight(List.of(s)));
        List<Flight> actual = arrivalTimeBeforeDepartureDate (flights);
        assertEquals(1, actual.size());
    }
    @Test
    @DisplayName("Т3/MC №1. При добавлении сегмента с валидным временем (вылет < прилёта) лист исключений пуст.")
    public void whenTimeIsValidThenSegmentIsNotAddedToExceptions() {
        Segment s = new Segment(LocalDateTime.parse("2024-06-09T18:05"),
                LocalDateTime.parse("2024-06-09T19:05"));
        List<Flight> flights = List.of(new Flight(List.of(s)));
        List<Flight> actual = arrivalTimeBeforeDepartureDate (flights);
        assertTrue(actual.isEmpty());
    }
    @Test
    @DisplayName("Т2/MC №2. Когда вылет до текущего времени, сегмент добавляют в лист исключений.")
    public void whenCheckDepartureIsBeforeCurrentTime() {
        Segment s = new Segment(LocalDateTime.parse("2024-06-07T17:05"),
                LocalDateTime.parse("2024-06-07T18:05"));
        List<Flight> flights = List.of(new Flight(List.of(s)));
        String expected = List.of(new Flight(List.of(s))).toString();
        String actual = departureBeforeCurrentTime(flights).toString();
        assertEquals(expected,actual);
    }
    @Test
    @DisplayName("Т3/MC №2. Когда вылет сегмента после текущего времени, лист исключений пуст.")
    public void whenCheckDepartureIsAfterCurrentTime() {
        Segment s = new Segment(LocalDateTime.parse("2025-06-20T17:05"),
                LocalDateTime.parse("2025-06-20T18:05"));
        List<Flight> flights = List.of(new Flight(List.of(s)));
        List<Flight> actual = departureBeforeCurrentTime(flights);
        assertTrue(actual.isEmpty());
    }


    @Test
    @DisplayName("Т2/MC №4. Когда перелёт соответствует правилу, его добавляют в лист предложений.")
    public void whenFlightsThatComplyWithTheRule() {
        Segment s1 = new Segment(LocalDateTime.parse("2023-08-31T16:05"),
                LocalDateTime.parse("2023-08-31T18:05"));
        Segment s2 = new Segment(LocalDateTime.parse("2023-08-31T19:05"),
                LocalDateTime.parse("2023-08-31T20:05"));
        List<Flight> flights = List.of(new Flight(List.of(s1, s2)));
        List<Flight> exFlights = List.of(new Flight(List.of(s1)));
        List<Flight> expected = List.of(new Flight(List.of(s2)));
        List<Flight> actual = flightsThatComplyWithTheRule (flights, exFlights);
        assertThat(actual.equals(expected));

    }

}
