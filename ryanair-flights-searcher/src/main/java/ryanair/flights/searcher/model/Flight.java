package ryanair.flights.searcher.model;

import java.util.List;


/**
 * @author Roberto Peral castro
 * Flight defines the flights that will have take place to arrive from an origin to a destiny. 
 */
public class Flight 
{
	public Flight(int stops, List<Leg> legs)
	{
		this.stops = stops;
		this.legs = legs;
	}
	
	public Flight()
	{
		
	}
	/**
	 * number of stops of the route
	 */
	private int stops;
	/**
	 * list of flights of the route
	 */
	private List<Leg> legs;
	
	public int getStops() {
		return stops;
	}
	public void setStops(int stops) {
		this.stops = stops;
	}
	public List<Leg> getFlights() {
		return legs;
	}
	public void setFlights(List<Leg> flights) {
		this.legs = flights;
	}
	
	
}
