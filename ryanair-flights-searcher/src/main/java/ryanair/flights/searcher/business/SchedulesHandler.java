package ryanair.flights.searcher.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ryanair.flights.searcher.model.Flight;
import ryanair.flights.searcher.model.Leg;
import ryanair.flights.searcher.model.Route;
import ryanair.flights.searcher.model.Schedule;
import ryanair.flights.searcher.model.ScheduleDay;
import ryanair.flights.searcher.model.ScheduleFlight;

/**
 * @author Roberto Peral Castro
 * this class provides operations with flights schedules
 */
@Service
public class SchedulesHandler {
	
	/**
	 * @param departureSchedule departure date and time
	 * @param arrivalSchedule arrival date and time
	 * @param firstAirportDeparture first route departure airport code
	 * @param firstAirportArrival first route arrival airport code
	 * @param secondAirportDeparture second route departure airport code
	 * @param secondAirportArrival second route arrival airport code
	 * @param tripDepartureCalendar departure date and time to be fullfilled by the retrieved flights
	 * @param tripArrivalCalendar arrival date and time to be fullfilled by the retrieved flights
	 * @return a list of available flights with a departure, an arrival, and one stop between them, that fullfill the given schedules
	 */
	public List<Flight> checkScheduleCompatibility(Schedule departureSchedule, Schedule arrivalSchedule, String firstAirportDeparture,String firstAirportArrival, String secondAirportDeparture,String secondAirportArrival, Calendar tripDepartureCalendar , Calendar tripArrivalCalendar)
    {
    	List<Flight> result = new ArrayList<Flight>();

		List<ScheduleFlight> possibleFirstAirportFlights = getDayFlights(departureSchedule, tripDepartureCalendar.get(Calendar.DAY_OF_MONTH));
		List<ScheduleFlight> possibleSecondAirportFlights = getDayFlights(arrivalSchedule, tripArrivalCalendar.get(Calendar.DAY_OF_MONTH));
		
		for(ScheduleFlight scheduleFirstFlight : possibleFirstAirportFlights)
		{
			Calendar twoHoursMarginTime = scheduleFirstFlight.getArrivalTime();
			CalendarUtils.completeCalendarWithYearMonthAndDay(twoHoursMarginTime, tripDepartureCalendar);
			twoHoursMarginTime.add(Calendar.HOUR_OF_DAY, 2);
			List<ScheduleFlight> timeMatchedFlights = getTimeMatchedFlights(possibleSecondAirportFlights, scheduleFirstFlight,  tripDepartureCalendar,  tripArrivalCalendar);
			result.addAll(timeMatchedFlights.stream().map(possibleSecondFlight -> 
			{
				List<Leg> legs = new ArrayList<Leg>();
				//Schedule json does not provide Date, only time, so we add the date as we now it from the request
				CalendarUtils.completeCalendarWithYearMonthAndDay(scheduleFirstFlight.getDepartureTime(), tripDepartureCalendar);
				CalendarUtils.completeCalendarWithYearMonthAndDay(scheduleFirstFlight.getArrivalTime(), tripDepartureCalendar);
				CalendarUtils.completeCalendarWithYearMonthAndDay(possibleSecondFlight.getDepartureTime(), tripArrivalCalendar);
				CalendarUtils.completeCalendarWithYearMonthAndDay(possibleSecondFlight.getArrivalTime(), tripArrivalCalendar);
				legs.add(new Leg(firstAirportDeparture, firstAirportArrival, scheduleFirstFlight.getDepartureTime(), scheduleFirstFlight.getArrivalTime()));
				legs.add(new Leg(secondAirportDeparture, secondAirportArrival, possibleSecondFlight.getDepartureTime(), possibleSecondFlight.getArrivalTime()));
				return new Flight(1, legs);
			}).collect(Collectors.toList()));
		}


    	return result;
    }
	
	
	/**
	 * @param possibleAirportFlights
	 * @param scheduleFirstFlight
	 * @param tripDepartureCalendar
	 * @param tripArrivalCalendar
	 * @return returns a list of flights that can match a previous flight as an interconnection to reach an arrival airport at a given date and time
	 */
	private List<ScheduleFlight> getTimeMatchedFlights(List<ScheduleFlight> possibleAirportFlights, ScheduleFlight scheduleFirstFlight,  Calendar tripDepartureCalendar,  Calendar tripArrivalCalendar)
	{
		Calendar twoHoursMarginTime = scheduleFirstFlight.getArrivalTime();
		CalendarUtils.completeCalendarWithYearMonthAndDay(twoHoursMarginTime, tripDepartureCalendar);
		twoHoursMarginTime.add(Calendar.HOUR_OF_DAY, 2);
		return possibleAirportFlights.stream().filter(flight->
		{
			Calendar flightDepartureCalendar = flight.getDepartureTime();
			CalendarUtils.completeCalendarWithYearMonthAndDay(flightDepartureCalendar, tripArrivalCalendar);
			return flightDepartureCalendar.after(twoHoursMarginTime);
		}).collect(Collectors.toList());
	}
	
	/**
	 * @param schedule schedule with the scheduled days
	 * @param day number of day to be returned from the schedule
	 * @return a list of scheduled flight from a day in a schedule.
	 */
	public List<ScheduleFlight> getDayFlights(Schedule schedule, int day)
	{
		Optional<ScheduleDay> sDay = schedule.getDays().stream().filter(d -> {
			return d.getDay() == day;
		}).findFirst();
		if (sDay.isPresent())
		{
			return sDay.get().getFlights();
		}
		return new ArrayList<ScheduleFlight>();		
	}
	
	/**
	 * @param departure departure airport code
	 * @param arrival arrival airport code
	 * @param year year of the schedule
	 * @param month month of the schedule
	 * @return a schedule from ryanair API
	 */
	public Schedule getSchedule(String departure, String arrival, int year, int month)
	{
		RestTemplate restTemplate = new RestTemplate();
		try
		{
			String url = "https://api.ryanair.com/timetable/3/schedules/"+departure+"/"+arrival+"/years/"+ year + "/months/" + month; 
			Schedule schedule = restTemplate.getForObject(url, Schedule.class);
			//remove the offset that the JSON mapper introduces automatically depending on the timezone the service is executed
			removeOffset(schedule);
			return schedule;
		} 
		catch (Exception e)
		{
			Schedule sched = new Schedule(0, new ArrayList<ScheduleDay>());
			return sched;
		}
	}
    
    
    
    /**
     * @param departure departure airport code
     * @param arrival arrival airport code
     * @param departureCalendar departure calendar to be fullfilled by the returned flights
     * @param arrivalCalendar arrival calendar to be fullfilled by the returned flights
     * @return a list of available flights with a departure, an arrival, and one stop between them
     */
    public List<Flight> getOneStopFlights(String departure, String arrival, Calendar departureCalendar, Calendar arrivalCalendar)
    {
    	
    	RoutesHandler routesHandler = new RoutesHandler();
		List<Route> routes = routesHandler.getRoutes();

    	List<Flight> oneStopFlights = new ArrayList<Flight>();
    	//First get the posible routes whose arrivals and departures are connected
    	Map<Optional<Route>,Optional<Route>> possibleRoutes = routesHandler.getRoutesWithOneStop(routes, departure, arrival);
    	
    	//Now check that the the difference between the arrival and the next departure should be 2h or greater  
		//for each Key of the map get its scheduled day and arrival time
    	
    	for (Map.Entry<Optional<Route>, Optional<Route>> pair : possibleRoutes.entrySet()) 
    	{
    		if(pair.getKey().isPresent() && pair.getValue().isPresent())
    		{
    			
	    		Route possibleDepartureRoute = pair.getKey().get();
	    	    Route possibleArrivalRoute = pair.getValue().get();
	    	    Schedule departureSchedule = getSchedule(possibleDepartureRoute.getAirportFrom(), possibleDepartureRoute.getAirportTo(), departureCalendar.get(Calendar.YEAR), departureCalendar.get(Calendar.MONTH) + 1);
	    	    Schedule possibleArrivalSchedule = getSchedule(possibleArrivalRoute.getAirportFrom(), possibleArrivalRoute.getAirportTo(), departureCalendar.get(Calendar.YEAR), departureCalendar.get(Calendar.MONTH) + 1);
	    	    if(departureSchedule != null && possibleArrivalSchedule != null)
	    	    {
	    	    	oneStopFlights.addAll(checkScheduleCompatibility(departureSchedule, possibleArrivalSchedule, possibleDepartureRoute.getAirportFrom(), possibleDepartureRoute.getAirportTo(), possibleArrivalRoute.getAirportFrom(), possibleArrivalRoute.getAirportTo(), departureCalendar, arrivalCalendar));
	    	    }
    		}
    	}
    	return oneStopFlights;
    }
	
	/**
     * 
     * @param schedule, schedule for 2 specified airports, departure and arrival
     * @param departureTime returned flights must be at or after this hour
     * @param arrivalTime returned flights must be at or after this hour
     * @return the list of scheduled flights which are valid for the departure time passed as parameter
     */
    public Stream<ScheduleFlight> getDirectScheduleFlights(Schedule schedule, Calendar departureTime, Calendar arrivalTime)
    {
    	//select the day of the flight from the schedule

    	Optional<ScheduleDay> sDepartureDay = schedule.getDays().stream().filter(day -> 
    	{
    		return day.getDay() == departureTime.get(Calendar.DAY_OF_MONTH);
    	}).findFirst();
    	
    	Optional<ScheduleDay> sArrivalDay = schedule.getDays().stream().filter(day ->day.getDay() == arrivalTime.get(Calendar.DAY_OF_MONTH)).findFirst();
    	//we just want to compare the time, so we set date to 0
    	//calendarUtils.normalizeDate(departureTime);
    	
    	//calendarUtils.normalizeDate(arrivalTime);
		
    	//if the arrival day is the same as the departure day
    	if(sDepartureDay.isPresent() && sArrivalDay.isPresent())
    	{
    		
			//from the selected day, select the flights whose departure time is equal or after the departure time and whose arrival time is previous or equal to the arrival time    		
    		return sDepartureDay.get().getFlights().stream().filter(sFlight ->  
    		{
    			Calendar depCalendar = sFlight.getDepartureTime();
    			Calendar arrCalendar = sFlight.getArrivalTime();
    			CalendarUtils.completeCalendarWithYearMonthAndDay(depCalendar, departureTime);
    			CalendarUtils.completeCalendarWithYearMonthAndDay(arrCalendar, arrivalTime);
    			boolean  departureMatches = (depCalendar.getTime().equals(departureTime.getTime()) || depCalendar.after(departureTime));
    			boolean arrivalMatches = (arrCalendar.getTime().equals(arrivalTime.getTime()) || arrCalendar.before(arrivalTime));
    			return ( departureMatches && arrivalMatches );
    		});
    	}
    	return new ArrayList<ScheduleFlight>().stream();
    }
    
	/**
     * Removes the offset from the dates of a schedule 
     * @param schedule
     */
    private void removeOffset(Schedule schedule)
    {
    	schedule.setDays(schedule.getDays().stream().map(day -> {
    		List<ScheduleFlight> flights = day.getFlights();
    		//substract the offset from the departure and arrival times
    		day.setFlights(flights.stream().map(flight -> {
    			flight.getDepartureTime().add(Calendar.HOUR_OF_DAY, flight.getDepartureTime().getTime().getTimezoneOffset()/60);
    			flight.getArrivalTime().add(Calendar.HOUR_OF_DAY, flight.getArrivalTime().getTime().getTimezoneOffset()/60);
    			return flight;
    		}).collect(Collectors.toList()));
    				
    	return day;
    	}).collect(Collectors.toList()));
    }
}
