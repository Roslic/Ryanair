package ryanair.flights.searcher.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ryanair.flights.searcher.business.CalendarUtils;
import ryanair.flights.searcher.business.SchedulesHandler;
import ryanair.flights.searcher.model.Flight;
import ryanair.flights.searcher.model.Leg;
import ryanair.flights.searcher.model.Schedule;
import ryanair.flights.searcher.model.ScheduleFlight;

/**
 * @author Roberto Peral Castro
 * This API returns the available flights given the following parameters:
 * 1 - departure and arrival airports
 * 2 - departure and arrival dates and times
 * 
 * It is supposed that a flight will never start in one day and end in a different day, 
 * as the API for requesting schedules does not specify how this case would be represented.
 *
 */
@RestController
@RequestMapping("/interconnections")
public class RyanairFlightsSearcherService 
{
	@Autowired
	SchedulesHandler schedulesHandler;

	@RequestMapping(method=RequestMethod.GET)
	public List<Flight> getAvailablePaths(@RequestParam("departure")  String departure, @RequestParam("arrival")  String arrival, @RequestParam("departureDateTime")  String departureDateTime, @RequestParam("arrivalDateTime")  String arrivalDateTime) 
			throws ParseException
	{
		
		Calendar departureCalendar;
		Calendar arrivalCalendar;
		departureCalendar = CalendarUtils.toCalendar(departureDateTime);
		arrivalCalendar = CalendarUtils.toCalendar(arrivalDateTime);
		
		List<Flight> flights = new ArrayList<Flight>();
		flights.addAll(getDirectFlights(departure, arrival, departureCalendar, arrivalCalendar));
		flights.addAll(schedulesHandler.getOneStopFlights(departure, arrival, departureCalendar, arrivalCalendar));
		return flights;
	}

	
	/**
	 * @param departure departure airport code
	 * @param arrival arrival airport code
	 * @param departureCalendar date of the departure
	 * @param arrivalCalendar date of the arrival
	 * @return List of possible direct Flight between the airports passed as parameters
	 */
	public List<Flight> getDirectFlights(String departure, String arrival, Calendar departureCalendar, Calendar arrivalCalendar)
    {

		Schedule schedule = schedulesHandler.getSchedule(departure, arrival, departureCalendar.get(Calendar.YEAR), departureCalendar.get(Calendar.MONTH) + 1);
    	
    	Stream<ScheduleFlight> scheduleFlights = schedulesHandler.getDirectScheduleFlights(schedule, departureCalendar, arrivalCalendar);
    	return scheduleFlights.map(shcFlight -> {
    		List<Leg> legs = new ArrayList<Leg>();
    		legs.add(new Leg(departure, arrival, shcFlight.getDepartureTime(), shcFlight.getArrivalTime()));
    		return new Flight(0,legs);
    	}
    	).collect(Collectors.toList());
    }   
}
