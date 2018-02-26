package ryanair.flights.searcher.business;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ryanair.flights.searcher.model.Route;
@Service
public class RoutesHandler {
	
	
	
    
    
    /**
     * @param routes, all available routes
     * @param departure, departure airoprt code
     * @param arrival, arrival airport code
     * @return A map with an origin route and all the routes that can be arrivals for that route
     */
	public Map<Optional<Route>,Optional<Route>> getRoutesWithOneStop(List<Route> routes, String departure, String arrival)
    {
    	//first remove the direct routes
    	List<Route> nonDirectRoutes = routes.stream().filter(route -> !(route.getAirportFrom().equals(departure) && route.getAirportTo().equals(arrival))).collect(Collectors.toList());
    	List<Route> departureRoutes = nonDirectRoutes.stream().filter(route -> route.getAirportFrom().equals(departure)).collect(Collectors.toList());
    	List<Route> arrivalRoutes = nonDirectRoutes.stream().filter(route -> route.getAirportTo().equals(arrival)).collect(Collectors.toList());
    	return departureRoutes.stream().collect(Collectors.toMap(depRoute -> Optional.of(depRoute), depRoute -> getArrivalRoutesForDepartureRoute(depRoute, arrivalRoutes, arrival)));
    }
    
    public Optional<Route> getArrivalRoutesForDepartureRoute(Route  departure, List<Route> arrivals, String arrival)
    {
    	return arrivals.stream().filter(arrivalRoute -> arrivalRoute.getAirportFrom().equals(departure.getAirportTo()) && arrivalRoute.getAirportTo().equals(arrival)).findFirst();
    }
    
    
	/**
	 * 
	 * @return routes with connecting airports set to null.
	 */
    public List<Route> getRoutes()
	{
		RestTemplate restTemplate = new RestTemplate();
	    Route[] routes = restTemplate.getForObject("https://api.ryanair.com/core/3/routes", Route[].class);
	    return Arrays.asList(routes).stream().filter(r -> r.getConnectingAirport() == null).collect(Collectors.toList());
	}

}
