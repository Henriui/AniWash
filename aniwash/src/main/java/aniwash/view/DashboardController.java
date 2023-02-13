package aniwash.view;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.view.AgendaView;
import com.calendarfx.view.CalendarView;

import aniwash.MainApp;
import aniwash.resources.model.Calendars;

import javafx.fxml.FXML;
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
    private Calendars calendars = new Calendars();

    public void initialize() {

        agendaView.getCalendarSources().addAll(calendars.getCalendarss());
        // agendaView.setCalendarSourceFactory(new Calendars());
    }

    @FXML
    private void mySchedule() throws IOException {
        MainApp.setRoot("schedule");
    }

}