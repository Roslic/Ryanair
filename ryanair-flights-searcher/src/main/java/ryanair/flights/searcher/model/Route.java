package ryanair.flights.searcher.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {

	String airportFrom;
	String airportTo;
	String connectingAirport;
	boolean newRoute;
    boolean seasonalRoute;
    String group;
	
    public String getAirportFrom() {
		return airportFrom;
	}
	public void setAirportFrom(String airportFrom) {
		this.airportFrom = airportFrom;
	}
	public String getAirportTo() {
		return airportTo;
	}
	public void setAirportTo(String airportTo) {
		this.airportTo = airportTo;
	}
	public String getConnectingAirport() {
		return connectingAirport;
	}
	public void setConnectingAirport(String connectingAirport) {
		this.connectingAirport = connectingAirport;
	}
	public boolean isNewRoute() {
		return newRoute;
	}
	public void setNewRoute(boolean newRoute) {
		this.newRoute = newRoute;
	}
	public boolean isSeasonalRoute() {
		return seasonalRoute;
	}
	public void setSeasonalRoute(boolean seasonalRoute) {
		this.seasonalRoute = seasonalRoute;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
    
    
}
