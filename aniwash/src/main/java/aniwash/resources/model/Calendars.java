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

public class Calendars implements Callback<CreateCalendarSourceParameter, CalendarSource> {

    private static ArrayList<Calendar> calendars = new ArrayList<Calendar>();
    private static ArrayList<Entry> entries = new ArrayList<Entry>();

    public Calendars() {

    }

    public void initCalendar() {
        Calendar TeethCare = new Calendar("Teeth Care");
        Calendar Nails = new Calendar("Nails");

        calendars.add(TeethCare);
        calendars.add(Nails);

        entries.add(new Entry<>("Dentist"));
        entries.add(new Entry<>("asdasd"));
        entries.add(new Entry<>("asdadasd"));
        entries.add(new Entry<>("ddddd"));
        entries.add(new Entry<>("Something"));
        TeethCare.addEntries(entries);
        TeethCare.setStyle(Style.STYLE7);
        Nails.setStyle(Style.STYLE2);

        CalendarSource familyCalendarSource = new CalendarSource("Appointments");
        familyCalendarSource.getCalendars().addAll(TeethCare, Nails);

        Interval interval = new Interval(LocalDate.of(2023, Month.JANUARY, 31), LocalTime.of(23, 0),
                LocalDate.of(2023, Month.JANUARY, 31), LocalTime.of(23, 30));
    }

    public CalendarSource getCalendarss() {
        CalendarSource familyCalendarSource = new CalendarSource("Product");
        familyCalendarSource.getCalendars().addAll(calendars);
        return familyCalendarSource;
    }

    public void CreateProduct(String name) {
        Calendar calendar = new Calendar(name);
        calendars.add(calendar);
    }

    public void CreateEntry(Calendar calendarName, String name, String description, LocalDate date, LocalTime time) {
        Entry entry = new Entry<>(name);
        entry.setInterval(new Interval(date, time, date, time));
        calendarName.addEntry(entry);
        calendars.add(calendarName);
        // entries.add(entry);
    }

    public Calendar creatCalendar(String name) {
        Calendar calendar = new Calendar(name);
        return calendar;
    }

    @Override
    public CalendarSource call(CreateCalendarSourceParameter arg0) {
        // TODO Auto-generated method stub
        return null;
    }

}
