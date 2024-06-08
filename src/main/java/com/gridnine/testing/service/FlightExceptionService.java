package com.gridnine.testing.service;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FlightExceptionService {
    static Scanner scanner = new Scanner(System.in);
    /**
     * МС №1. Определение списка исключений из перелётов, если время прилёта раньше времени прибытия.
     */
    public static List<Flight> arrivalTimeBeforeDepartureDate (List<Flight> flights) {
        List<Flight> exFlights = new ArrayList<>();
        if (flights.isEmpty()) {
            throw new IllegalArgumentException("Данные для обработки не предоставлены.");
        }
        for (Flight e : flights) {
            for (Segment s : e.getSegments()) {
                if (s.getArrivalDate().compareTo(s.getDepartureDate()) < 0) {
                    exFlights.add(e);
                }
            }
        }
       return exFlights;
    }
    /**
     * МС №2. Определение списка исключений из перелётов, если вылет до текущего времени.
     */
    public static List<Flight> departureBeforeCurrentTime(List<Flight> flights) {
        List<Flight> exFlights = new ArrayList<>();
        if (flights.isEmpty()) {
            throw new IllegalArgumentException("Данные для обработки не предоставлены.");
        }
        for (Flight e : flights) {
            for (Segment s : e.getSegments()) {
                if (s.getDepartureDate().compareTo(LocalDateTime.now()) < 0) {
                    exFlights.add(e);
                }
            }
        }
       return exFlights;
    }
    /**
     * МС №3. Определение списка исключений из перелётов, если время ожидания на земле больше заданного.
     */
    public static List<Flight> moreWaitingTimeOnTheGround (List<Flight> flights) {
        if (flights.isEmpty()) {
            throw new IllegalArgumentException("Данные не предоставлены или не корректны.");
        }
        while (true) {
            try {
                List<Flight> exFlights = new ArrayList<>();
                System.out.print("Задайте приемлемое количество часов ожидания на земле (0 <= целое число <= 2): ");
                int hours = scanner.nextInt();
                for (Flight e : flights) {
                    long sumDiff = 0L;
                    List<Segment> segments = e.getSegments();
                    if (segments.size() >= 2) {
                        for (int i = 0; i < segments.size() - 1; i++) {
                            long diff = Duration.between(segments.get(i).getArrivalDate(),
                                    segments.get(i + 1).getDepartureDate()).toHours();
                            sumDiff += diff;
                        }
                    }
                    if (sumDiff > hours) {
                        exFlights.add(e);
                    }
                }
                if (hours >= 0)
                    return exFlights;
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
    }
    /**
     * МС №4. Определение списка перелётов, соответствующих правилу.
     */
    public static List<Flight> flightsThatComplyWithTheRule (List<Flight> flights, List<Flight> exFlights) {
        if (flights.isEmpty()) {
            throw new IllegalArgumentException("Данные для обработки не предоставлены.");
        }
        return flights.stream().filter(e -> !exFlights.contains(e)).toList();
    }
    /**
     * МС №5. Вывод в консоль меню правил фильтрации перелётов.
     */
    public static void printMenu() {
        System.out.println("""

                    --- МЕНЮ правил фильтрации ---
                1. Перелёт не должен включать сегмент, где время прилёта раньше даты вылета.
                2. Перелёт не должен включать сегмент с временем вылета до текущего времени.
                3. Общее время на земле у перелёта не должно превышать заданного.
                0. ВЫХОД из модуля фильтрации.
                Выберите правило фильтрации и введите № пункта МЕНЮ:\s""");
    }
}
