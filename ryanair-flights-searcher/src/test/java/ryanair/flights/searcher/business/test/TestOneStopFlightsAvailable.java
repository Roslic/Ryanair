package ryanair.flights.searcher.business.test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import ryanair.flights.searcher.business.SchedulesHandler;
import ryanair.flights.searcher.model.Flight;
import ryanair.flights.searcher.model.Schedule;
import ryanair.flights.searcher.model.ScheduleDay;
import ryanair.flights.searcher.model.ScheduleFlight;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestOneStopFlightsAvailable {

	/**
	 * Test that only one route is available according to the flights scheduled
	 */
	@Test
	public void testOnlyOneRouteAvailable() {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		//schedule for the first airport flight
		List<ScheduleFlight> scheduledFlights1 = new ArrayList<ScheduleFlight>();
		scheduledFlights1.add(createScheduleFlight("111", 9, 13, day, day));
		scheduledFlights1.add(createScheduleFlight("111", 10, 17, day, day));
		ScheduleDay sd1 = new ScheduleDay(day,scheduledFlights1);
		List<ScheduleDay> firstAirportdays = new ArrayList<ScheduleDay>();
		firstAirportdays.add(sd1);
		Schedule firstAirportRouteSchedule = new Schedule(6, firstAirportdays); 
		//schedule for the second airport flight
		List<ScheduleFlight> scheduledFlights2 = new ArrayList<ScheduleFlight>();
		scheduledFlights2.add(createScheduleFlight("111", 18, 19, day, day));
		scheduledFlights2.add(createScheduleFlight("111", 10, 14, day, day));
		ScheduleDay sd2 = new ScheduleDay(day,scheduledFlights2);
		List<ScheduleDay> secondAirportdays = new ArrayList<ScheduleDay>();
		secondAirportdays.add(sd2);
		Schedule secondAirportRouteSchedule = new Schedule(6, secondAirportdays); 
		
		SchedulesHandler schedulesHandler = new SchedulesHandler();
		List<Flight> flights = schedulesHandler.checkScheduleCompatibility(firstAirportRouteSchedule, secondAirportRouteSchedule, "MAD","BCN", "BCN","CDG",Calendar.getInstance(),Calendar.getInstance());
		assertTrue(flights.size() == 1);
	}
	
	/**
	 * Test that three routes are available according to the flights scheduled
	 */
	@Test
	public void testThreeRoutesAvailable() {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

		//schedule for the first airport flight
		List<ScheduleFlight> scheduledFlights1 = new ArrayList<ScheduleFlight>();
		scheduledFlights1.add(createScheduleFlight("111", 9, 13, day, day));
		scheduledFlights1.add(createScheduleFlight("111", 10, 17, day, day));
		ScheduleDay sd1 = new ScheduleDay(day,scheduledFlights1);
		List<ScheduleDay> firstAirportdays = new ArrayList<ScheduleDay>();
		firstAirportdays.add(sd1);
		Schedule firstAirportRouteSchedule = new Schedule(6, firstAirportdays); 
		//schedule for the second airport flight
		List<ScheduleFlight> scheduledFlights2 = new ArrayList<ScheduleFlight>();
		scheduledFlights2.add(createScheduleFlight("111", 18, 19, day, day));
		scheduledFlights2.add(createScheduleFlight("111", 23, 24, day, day));
		ScheduleDay sd2 = new ScheduleDay(day,scheduledFlights2);
		List<ScheduleDay> secondAirportdays = new ArrayList<ScheduleDay>();
		secondAirportdays.add(sd2);
		Schedule secondAirportRouteSchedule = new Schedule(6, secondAirportdays); 
		
		SchedulesHandler schedulesHandler = new SchedulesHandler();
		List<Flight> flights = schedulesHandler.checkScheduleCompatibility(firstAirportRouteSchedule, secondAirportRouteSchedule, "MAD","BCN", "BCN","CDG",Calendar.getInstance(),Calendar.getInstance());
		assertTrue(flights.size() == 3);
	}
	
	/**
	 * Test that no route is available according to the flights scheduled
	 */
	@Test
	public void testNoRouteAvailable() {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

		//schedule for the first airport flight
		List<ScheduleFlight> scheduledFlights1 = new ArrayList<ScheduleFlight>();
		scheduledFlights1.add(createScheduleFlight("111", 9, 13, day, day));
		scheduledFlights1.add(createScheduleFlight("111", 10, 17, day, day));
		ScheduleDay sd1 = new ScheduleDay(day,scheduledFlights1);
		List<ScheduleDay> firstAirportdays = new ArrayList<ScheduleDay>();
		firstAirportdays.add(sd1);
		Schedule firstAirportRouteSchedule = new Schedule(6, firstAirportdays); 
		//schedule for the second airport flight
		List<ScheduleFlight> scheduledFlights2 = new ArrayList<ScheduleFlight>();
		scheduledFlights2.add(createScheduleFlight("111", 11, 18, day, day));
		scheduledFlights2.add(createScheduleFlight("111", 10, 14, day, day));
		ScheduleDay sd2 = new ScheduleDay(day,scheduledFlights2);
		List<ScheduleDay> secondAirportdays = new ArrayList<ScheduleDay>();
		secondAirportdays.add(sd2);
		Schedule secondAirportRouteSchedule = new Schedule(6, secondAirportdays); 
		
		SchedulesHandler schedulesHandler = new SchedulesHandler();
		List<Flight> flights = schedulesHandler.checkScheduleCompatibility(firstAirportRouteSchedule, secondAirportRouteSchedule, "MAD","BCN", "BCN","CDG",Calendar.getInstance(),Calendar.getInstance());
		assertFalse(flights.size() > 0);
	}
	
	@Test
	public void testNoRouteAvailableNoConnection() {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

		//schedule for the first airport flight
		List<ScheduleFlight> scheduledFlights1 = new ArrayList<ScheduleFlight>();
		scheduledFlights1.add(createScheduleFlight("111", 9, 13, day, day));
		scheduledFlights1.add(createScheduleFlight("111", 10, 17, day, day));
		ScheduleDay sd1 = new ScheduleDay(day,scheduledFlights1);
		List<ScheduleDay> firstAirportdays = new ArrayList<ScheduleDay>();
		firstAirportdays.add(sd1);
		Schedule firstAirportRouteSchedule = new Schedule(6, firstAirportdays); 
		//schedule for the second airport flight
		List<ScheduleFlight> scheduledFlights2 = new ArrayList<ScheduleFlight>();
		scheduledFlights2.add(createScheduleFlight("111", 11, 18, day, day));
		scheduledFlights2.add(createScheduleFlight("111", 10, 14, day, day));
		ScheduleDay sd2 = new ScheduleDay(day,scheduledFlights2);
		List<ScheduleDay> secondAirportdays = new ArrayList<ScheduleDay>();
		secondAirportdays.add(sd2);
		Schedule secondAirportRouteSchedule = new Schedule(6, secondAirportdays); 
		
		SchedulesHandler schedulesHandler = new SchedulesHandler();
		List<Flight> flights = schedulesHandler.checkScheduleCompatibility(firstAirportRouteSchedule, secondAirportRouteSchedule, "MAD","BCN", "BCN","CDG",Calendar.getInstance(),Calendar.getInstance());
		assertFalse(flights.size() > 0);
	}
	
	private ScheduleFlight createScheduleFlight(String flightNumber, int departureHour, int arrivalHour, int departureDay, int arrivalDay)
	{
		Calendar airportDepartureTime = Calendar.getInstance();
		airportDepartureTime.set(Calendar.HOUR_OF_DAY, departureHour);
		airportDepartureTime.set(Calendar.MINUTE, 0);
		airportDepartureTime.set(Calendar.DAY_OF_MONTH, departureDay);
		Calendar airportArrivalTime = Calendar.getInstance();
		airportArrivalTime.set(Calendar.HOUR_OF_DAY, arrivalHour);
		airportArrivalTime.set(Calendar.MINUTE, 0);
		airportArrivalTime.set(Calendar.DAY_OF_MONTH, arrivalDay);

		
		
		return new ScheduleFlight(flightNumber,airportDepartureTime,airportArrivalTime);
	}
	
}
