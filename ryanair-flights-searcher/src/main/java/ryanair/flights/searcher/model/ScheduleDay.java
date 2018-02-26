package ryanair.flights.searcher.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class ScheduleDay 
{
	int day;
	List<ScheduleFlight> flights;
	
	public ScheduleDay(int day , List<ScheduleFlight> flights)
	{
		this.day = day;
		this.flights = flights;
	}
	
	public ScheduleDay()
	{
		
	}
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public List<ScheduleFlight> getFlights() {
		return flights;
	}
	public void setFlights(List<ScheduleFlight> flights) {
		this.flights = flights;
	}
	
	

}
