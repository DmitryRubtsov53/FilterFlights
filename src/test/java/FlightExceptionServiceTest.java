
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import com.gridnine.testing.service.FlightExceptionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.gridnine.testing.service.FlightExceptionService.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class FlightExceptionServiceTest {

    @Test
    @DisplayName("М1-4/Т1. Когда лист перелётов пуст, выбрасывается исключение.")
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
    @DisplayName("М1/Т2. Сегмент с не валидным временем (вылет > прилёта) попадает в лист исключений.")
    public void whenTimeIsNotValidThenSegmentIsAddedToExceptions() {
        Segment s = new Segment(LocalDateTime.now().plusHours(2),
                LocalDateTime.now().plusHours(1));
        List<Flight> flights = List.of(new Flight(List.of(s)));
        List<Flight> actual = arrivalTimeBeforeDepartureDate (flights);
        assertEquals(1, actual.size());
    }

    @Test
    @DisplayName("М1/Т3. При добавлении сегмента с валидным временем (вылет < прилёта) лист исключений пуст.")
    public void whenTimeIsValidThenSegmentIsNotAddedToExceptions() {
        Segment s = new Segment(LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2));
        List<Flight> flights = List.of(new Flight(List.of(s)));
        List<Flight> actual = arrivalTimeBeforeDepartureDate (flights);
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("М2/Т2. Когда вылет до текущего времени, сегмент добавляют в лист исключений.")
    public void whenCheckDepartureIsBeforeCurrentTime() {
        Segment s = new Segment(LocalDateTime.now().minusHours(2),
                LocalDateTime.now());
        List<Flight> flights = List.of(new Flight(List.of(s)));
        String expected = List.of(new Flight(List.of(s))).toString();
        String actual = departureBeforeCurrentTime(flights).toString();
        assertEquals(expected,actual);
    }

    @Test
    @DisplayName("М2/Т3. Когда вылет сегмента после текущего времени, лист исключений пуст.")
    public void whenCheckDepartureIsAfterCurrentTime() {
        Segment s = new Segment(LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2));
        List<Flight> flights = List.of(new Flight(List.of(s)));
        List<Flight> actual = departureBeforeCurrentTime(flights);
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("М3/Т2. Когда время ожидания на земле не больше 2 часов, лист исключений пуст.")
    public void whenWaitingMore2ThenListIsEmpty() {

        List<Flight> flights = List.of(new Flight(List.of(
                new Segment(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2)),
                new Segment(LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(4))
                )));
        List<Flight> actual = moreWaitingTimeOnTheGround (flights);
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("М3/Т3. Когда время ожидания на земле больше 2 часов, перелёт добавляют в лист исключений.")
    public void whenWaitingMore2ThenListIsNotEmpty() {
        List<Flight> flights = List.of(new Flight(List.of(
                new Segment(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2)),
                new Segment(LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(7))
        )));

        List<Flight> actual = moreWaitingTimeOnTheGround (flights);
        assertFalse(actual.isEmpty());
    }

    @Test
    @DisplayName("М4/Т2. Когда перелёт соответствует правилу, его добавляют в лист предложений.")
    public void whenFlightsThatComplyWithTheRule() {
        Segment s1 = new Segment(LocalDateTime.now().minusHours(2),
                LocalDateTime.now());
        Segment s2 = new Segment(LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2));
        List<Flight> flights = List.of(new Flight(List.of(s1, s2)));
        List<Flight> exFlights = List.of(new Flight(List.of(s1)));
        List<Flight> expected = List.of(new Flight(List.of(s2)));
        List<Flight> actual = flightsThatComplyWithTheRule (flights, exFlights);
        assertThat(actual.equals(expected));

    }
}
