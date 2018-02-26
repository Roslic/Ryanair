package ryanair.flights.searcher.model;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Roberto Peral castro
 * Leg defines the attributes of a flight from one airport to an other 
 */
public class Leg 
{
	String departureAirport;
	String arrivalAirport;
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
	Calendar departureDateTime;
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
	Calendar arrivalDateTime;
	
	public Leg(String departureAirPort, String arrivalAirport, Calendar departureDateTime, Calendar arrivalDateTime)
	{
		this.departureAirport = departureAirPort;
		this.arrivalAirport = arrivalAirport;
		this.departureDateTime = departureDateTime;
		this.arrivalDateTime = arrivalDateTime;
	}
	public Leg()
	{
		
	}
	
	public String getDepartureAirport() {
		return departureAirport;
	}
	public void setDepartureAirport(String departureAirport) {
		this.departureAirport = departureAirport;
	}
	public String getArrivalAirport() {
		return arrivalAirport;
	}
	public void setArrivalAirport(String arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}
	public Calendar getDepartureDateTime() {
		return departureDateTime;
	}
	public void setDepartureDateTime(Calendar departureDateTime) {
		this.departureDateTime = departureDateTime;
	}
	public Calendar getArrivalDateTime() {
		return arrivalDateTime;
	}
	public void setArrivalDateTime(Calendar arrivalDateTime) {
		this.arrivalDateTime = arrivalDateTime;
	}
	
	
}
