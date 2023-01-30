package aniwash.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.view.AgendaView;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.YearMonthView;
import com.calendarfx.view.DateControl.CreateCalendarSourceParameter;
import com.calendarfx.view.page.DayPage;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.util.Callback;

public class AsdController {
    @FXML
    private AnchorPane backGround;
    @FXML
    private CalendarView calendarview;
    @FXML
    private AgendaView asd;
  
    @FXML
    private BarChart dddas;
    public void initialize() {
        Calendar katja = new Calendar("Katja");
        Calendar dirk = new Calendar("Dirk");

        CalendarSource familyCalendarSource = new CalendarSource("Family");
        familyCalendarSource.getCalendars().addAll(katja, dirk);
        asd.getCalendarSources().setAll(familyCalendarSource);

        Calendar birthdays = new Calendar("Birthdays");
        Calendar holidays = new Calendar("Holidays");

        birthdays.setStyle(Style.STYLE1);
        holidays.setStyle(Style.STYLE2);
        CalendarSource myCalendarSource = new CalendarSource("My Calendars");
        myCalendarSource.getCalendars().addAll(birthdays, holidays);
        asd.getCalendarSources().addAll(myCalendarSource);

        Entry<String> dentistAppointment = new Entry<>("Dentist");
        Entry<String> d = new Entry<>("asdasd");
        Entry<String> a = new Entry<>("asdadasd");
        Entry<String> c = new Entry<>("ddddd");
        Entry entry = new Entry("BENIS");
        
        Interval interval = new Interval(LocalDate.of(2023, Month.JANUARY, 31), LocalTime.of(23, 0),LocalDate.of(2023, Month.JANUARY, 31) , LocalTime.of(23, 30));
        entry.setInterval(interval);
    
        Calendar calendar = new Calendar("Heatl Benis");
        holidays.addEntry(entry);

        asd.showEntry(dentistAppointment);
        asd.showEntry(a);
        asd.showEntry(c);
        asd.showEntry(d);
        katja.addEntry(dentistAppointment);
        holidays.addEntry(a);
        dirk.addEntry(d);
        katja.addEntry(c);
        
        System.out.println(asd.getCalendars());
    }
    private Object foo(CalendarEvent evt) {
        return null;
    }
    
}