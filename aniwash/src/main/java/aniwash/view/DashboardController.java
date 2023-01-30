package aniwash.view;

import java.io.IOException;
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

import aniwash.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class DashboardController {
    MainApp mainApp;
    @FXML
    private AnchorPane backGround;
    @FXML
    private CalendarView calendarview;
    @FXML
    private AgendaView agendaView;
    @FXML
    private Button scheduleButton;
    @FXML
    private BarChart barChart;
    private PopUp popup;

    public void initialize() {
        Calendar katja = new Calendar("Katja");
        Calendar dirk = new Calendar("Dirk");

        CalendarSource familyCalendarSource = new CalendarSource("Family");
        familyCalendarSource.getCalendars().addAll(katja, dirk);
        agendaView.getCalendarSources().setAll(familyCalendarSource);

        Calendar birthdays = new Calendar("Birthdays");
        Calendar holidays = new Calendar("Holidays");

        birthdays.setStyle(Style.STYLE1);
        holidays.setStyle(Style.STYLE2);
        CalendarSource myCalendarSource = new CalendarSource("My Calendars");
        myCalendarSource.getCalendars().addAll(birthdays, holidays);
        agendaView.getCalendarSources().addAll(myCalendarSource);

        Entry<String> dentistAppointment = new Entry<>("Dentist");
        Entry<String> d = new Entry<>("asdasd");
        Entry<String> a = new Entry<>("asdadasd");
        Entry<String> c = new Entry<>("ddddd");

        Entry entry = new Entry("BENIS");
        Interval interval = new Interval(LocalDate.of(2023, Month.JANUARY, 31), LocalTime.of(23, 0),LocalDate.of(2023, Month.JANUARY, 31) , LocalTime.of(23, 30));
        entry.setInterval(interval);
        Calendar calendar = new Calendar("Heatl Benis");
    
        holidays.addEntry(entry);

        agendaView.showEntry(dentistAppointment);
        agendaView.showEntry(a);
        agendaView.showEntry(c);
        agendaView.showEntry(d);
        katja.addEntry(dentistAppointment);
        holidays.addEntry(a);
        dirk.addEntry(d);
        katja.addEntry(c);
        

        System.out.println(agendaView.getCalendars());
    }

    @FXML
    private void mySchedule() throws IOException {
        MainApp.setRoot("schedule");
    }

}