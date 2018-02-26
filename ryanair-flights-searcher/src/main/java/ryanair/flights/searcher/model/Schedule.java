package ryanair.flights.searcher.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Schedule 
{

	int month;
	List<ScheduleDay> days;
	
	public Schedule(int month, List<ScheduleDay> days)
	{
		this.month = month;
		this.days = days;
	}
	
	public Schedule()
	{
		
	}
	
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public List<ScheduleDay> getDays() {
		return days;
	}
	public void setDays(List<ScheduleDay> days) {
		this.days = days;
	}
	
	
	
}
