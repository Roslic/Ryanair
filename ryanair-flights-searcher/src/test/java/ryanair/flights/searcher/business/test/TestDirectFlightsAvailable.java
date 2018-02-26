package ryanair.flights.searcher.business.test;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ryanair.flights.searcher.RyanairFlightsSearcherApplication;
import ryanair.flights.searcher.business.SchedulesHandler;
import ryanair.flights.searcher.model.Schedule;
import ryanair.flights.searcher.model.ScheduleDay;
import ryanair.flights.searcher.model.ScheduleFlight;

@SpringBootTest(classes = RyanairFlightsSearcherApplication.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class TestDirectFlightsAvailable {

	/**
	 * Test that only one route is available according to the flights scheduled
	 */
	@Test
	public void testOnlyOneFlightAvailable() {
		
		//schedule for the first airport flight
		List<ScheduleFlight> scheduledFlights1 = new ArrayList<ScheduleFlight>();
		Calendar schedDep = Calendar.getInstance();
		Calendar schedArr = Calendar.getInstance();
		schedDep.add(Calendar.HOUR_OF_DAY, 1);
		schedArr.add(Calendar.HOUR_OF_DAY, 2);
		
		Calendar depCal = Calendar.getInstance();
		Calendar arrCal = Calendar.getInstance();
		depCal.add(Calendar.HOUR_OF_DAY, -1);
		arrCal.add(Calendar.HOUR_OF_DAY, 5);
		
		scheduledFlights1.add(createScheduleFlight("111", schedDep.get(Calendar.HOUR_OF_DAY), schedArr.get(Calendar.HOUR_OF_DAY)));
		ScheduleDay sd1 = new ScheduleDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH),scheduledFlights1);
		List<ScheduleDay> airportdays = new ArrayList<ScheduleDay>();
		airportdays.add(sd1);
		Schedule schedule = new Schedule(6, airportdays); 
		
		
		SchedulesHandler schedulesHandler = new SchedulesHandler();
		List<ScheduleFlight> flights = schedulesHandler.getDirectScheduleFlights(schedule, depCal,arrCal).collect(Collectors.toList());
		assertTrue(flights.size() == 1);
	}
	
	
	/**
	 * Test that no route is available according to the flights scheduled
	 */
	@Test
	public void testnoFlightAvailable() {
		
		//schedule for the first airport flight
		List<ScheduleFlight> scheduledFlights1 = new ArrayList<ScheduleFlight>();
		Calendar schedDep = Calendar.getInstance();
		Calendar schedArr = Calendar.getInstance();
		schedDep.add(Calendar.HOUR_OF_DAY, -2);
		schedArr.add(Calendar.HOUR_OF_DAY, 2);
		
		Calendar depCal = Calendar.getInstance();
		Calendar arrCal = Calendar.getInstance();
		depCal.add(Calendar.HOUR_OF_DAY, -1);
		arrCal.add(Calendar.HOUR_OF_DAY, 5);
		
		scheduledFlights1.add(createScheduleFlight("111", schedDep.get(Calendar.HOUR_OF_DAY), schedArr.get(Calendar.HOUR_OF_DAY)));
		ScheduleDay sd1 = new ScheduleDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH),scheduledFlights1);
		List<ScheduleDay> airportdays = new ArrayList<ScheduleDay>();
		airportdays.add(sd1);
		Schedule schedule = new Schedule(6, airportdays); 
		
		
		SchedulesHandler schedulesHandler = new SchedulesHandler();
		List<ScheduleFlight> flights = schedulesHandler.getDirectScheduleFlights(schedule, depCal,arrCal).collect(Collectors.toList());
		assertTrue(flights.size() == 0);
	}
	
	private ScheduleFlight createScheduleFlight(String flightNumber, int departureHour, int arrivalHour)
	{
		Calendar airportDepartureTime = Calendar.getInstance();
		airportDepartureTime.set(Calendar.HOUR_OF_DAY, departureHour);
		airportDepartureTime.set(Calendar.MINUTE, 0);
		Calendar airportArrivalTime = Calendar.getInstance();
		airportArrivalTime.set(Calendar.HOUR_OF_DAY, arrivalHour);
		airportArrivalTime.set(Calendar.MINUTE, 0);
		
		
		return new ScheduleFlight(flightNumber,airportDepartureTime,airportArrivalTime);
	}
	
}
