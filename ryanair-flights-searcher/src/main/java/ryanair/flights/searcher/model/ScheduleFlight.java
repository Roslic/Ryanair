package ryanair.flights.searcher.model;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class ScheduleFlight 
{
	private String number;
	@JsonFormat(pattern="HH:mm")
	private Calendar departureTime;
	@JsonFormat(pattern="HH:mm")
	private Calendar arrivalTime;
	
	public ScheduleFlight(String number, Calendar departureTime, Calendar arrivalTime)
	{
		this.number = number;
		this.departureTime=departureTime;
		this.arrivalTime = arrivalTime;
	}
	
	public ScheduleFlight()
	{
		
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Calendar getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(Calendar departureTime) {
		this.departureTime = departureTime;
	}
	public Calendar getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(Calendar arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	
}
