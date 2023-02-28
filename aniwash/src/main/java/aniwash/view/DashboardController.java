package aniwash.view;

import java.io.IOException;

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

    @FXML
    private void customers() throws IOException {
        MainApp.setRoot("customerView");
    }

    @FXML
    private void products() throws IOException {
        MainApp.setRoot("productsView");
    }
}