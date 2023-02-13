package aniwash.resources.model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.view.AgendaView;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl.CreateCalendarSourceParameter;

import aniwash.MainApp;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class Calendars {

    private static ArrayList<Calendar> calendars = new ArrayList<Calendar>();
    private static ArrayList<Entry> Teehtentries = new ArrayList<Entry>();
    private static ArrayList<Entry> Nailentries = new ArrayList<Entry>();
    private static CalendarSource familyCalendarSource = new CalendarSource("Product");
    
    public Calendars() {

    }

    // FIXME: This is a test method, it will be removed later

    public void initCalendar() {

        // These are the calendars where entries will be added to. 
        // Entries are Customers
        // Calendars are Products

        Calendar TeethCare = new Calendar("Teeth Care");
        Calendar Nails = new Calendar("Nails");

        calendars.add(TeethCare);
        calendars.add(Nails);

        Teehtentries.add(new Entry<>("Dentist"));
        Teehtentries.add(new Entry<>("asdasd"));
        Nailentries.add(new Entry<>("asdadasd"));
        Nailentries.add(new Entry<>("ddddd"));
        Nailentries.add(new Entry<>("Something"));

        // Adds all the entries to the calendar

        TeethCare.addEntries(Teehtentries);
        Nails.addEntries(Nailentries);

        // Sets the style of the calendar from style 1 to 7

        TeethCare.setStyle(Style.STYLE7);
        Nails.setStyle(Style.STYLE2);
        
        // CalendarSource is a mother to all the calendars

        familyCalendarSource.getCalendars().addAll(calendars);
    }

    // Getters and Setters

    public CalendarSource getCalendarss() {
        System.out.println("getCalendarss" + familyCalendarSource.getCalendars());
        return familyCalendarSource;
    }

    // If we want to add a new product, we can use this method

    public void CreateProduct(String name) {
        Calendar calendar = new Calendar(name);
        calendars.add(calendar);
    }

    // This is a test method, it will be removed later

    public void CreateEntry(Calendar calendarName, String name, String description, LocalDate date, LocalTime time) {
        Entry entry = new Entry<>(name);
        entry.setInterval(new Interval(date, time, date, time));
        calendarName.addEntry(entry);
        calendars.add(calendarName);
        // entries.add(entry);
    }

    // This is a test method, it will be removed later

    public Calendar createCalendar(String name) {
        Calendar calendar = new Calendar(name);
        familyCalendarSource.getCalendars().add(calendar);
        return calendar;
    }

    public ArrayList<Calendar> getCalendars() {
        return calendars;
    }

}
