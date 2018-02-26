package ryanair.flights.searcher.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Service;

public class CalendarUtils 
{
	 /**
     * transform calendar date so that the year, month and date are 0, and the hour and minutes are conserved
     * @param cal
     */
   public static void completeCalendarWithYearMonthAndDay(Calendar timeCalendar, Calendar dateCalendar)
   {
	   timeCalendar.set(Calendar.YEAR, dateCalendar.get(Calendar.YEAR));
	   timeCalendar.set(Calendar.MONTH,dateCalendar.get(Calendar.MONTH));
	   timeCalendar.set(Calendar.DAY_OF_MONTH, dateCalendar.get(Calendar.DAY_OF_MONTH));
   }
    
    
    /** Transform ISO string to Calendar. */
    public static Calendar toCalendar(final String isoDate)
            throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(isoDate);
        calendar.setTime(date);
        return calendar;
    }
}
