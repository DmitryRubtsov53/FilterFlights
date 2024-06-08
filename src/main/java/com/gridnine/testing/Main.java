package com.gridnine.testing;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.service.FlightBuilder;
import com.gridnine.testing.service.FlightExceptionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.gridnine.testing.service.FlightExceptionService.*;

public class Main {
    static List<Flight> flights = FlightBuilder.createFlights();
    static List<Flight> exFlights = new ArrayList<>();

    public static void main(String[] args) {

        System.out.println("""
                МОДУЛЬ ФИЛЬТРАЦИИ ПЕРЕЛЁТОВ.
                __________________________________________________________________________________
                """);
        System.out.println("Тестовый набор перелётов:");
        System.out.println(flights);
        System.out.println("__________________________________________________________________________________" + "\n");
        System.out.println("ФИЛЬТРАЦИЯ ПЕРЕЛЁТОВ ПО ПРАВИЛАМ:");

        try (Scanner scanner = new Scanner(System.in)) {
            label:
            while (true) {
                FlightExceptionService.printMenu();
                if (scanner.hasNextInt()) {
                    int menu = scanner.nextInt();
                    switch (menu) {
                        case 1:
                            exFlights = arrivalTimeBeforeDepartureDate(flights);
                            System.out.println("Список исключенных перелётов по правилу 1:" + "\n" + exFlights);
                            System.out.println("Список перелётов, соответствующих правилу 1:" + "\n" +
                                    flightsThatComplyWithTheRule(flights,exFlights));
                            break;
                        case 2:
                            exFlights = departureBeforeCurrentTime(flights);
                            System.out.println("Список исключенных перелётов по правилу 2:" + "\n" + exFlights);
                            System.out.println("Список перелётов, соответствующих правилу 2:" + "\n" +
                                    flightsThatComplyWithTheRule(flights,exFlights));
                            break;
                        case 3:
                            exFlights = moreWaitingTimeOnTheGround(flights);
                            System.out.println("Список исключенных перелётов по правилу 3:" + "\n" + exFlights);
                            System.out.println("Список перелётов, соответствующих правилу 3:" + "\n" +
                                    flightsThatComplyWithTheRule(flights,exFlights));
                            break;
                        case 0:
                            break label;
                    }
                } else {
                    scanner.next();
                    System.out.println("Выберите правило фильтрации и введите № пункта МЕНЮ: ");
                }
            }
        }
    }
}