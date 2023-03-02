package aniwash.view;

import java.io.IOException;
import java.time.LocalDateTime;

import com.calendarfx.view.AgendaView;
import com.calendarfx.view.CalendarView;

import aniwash.MainApp;
import aniwash.datastorage.BiscuitExeption;
import aniwash.resources.model.Calendars;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

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
    @FXML
    private Text welcometext;

    public void initialize() {

        agendaView.getCalendarSources().addAll(calendars.getCalendarss());
        try {
            // Write welcome text based on time of day (good morning, afternoon, evening)
            int time = LocalDateTime.now().getHour();
            System.out.println(time);
            if (time >= 0 && time < 12) {
                welcometext.setText("Good morning, " + MainApp.getBiscuit().getUser().getName() + "!");
            } else if (time >= 12 && time < 18) {
                welcometext.setText("Good afternoon, " + MainApp.getBiscuit().getUser().getName() + "!");
            } else {
                welcometext.setText("Good evening, " + MainApp.getBiscuit().getUser().getName() + "!");
            }
        } catch (BiscuitExeption e) {
            System.out.println("Biscuit fuked up");
            try {logout();} catch (IOException e1){e1.printStackTrace();}
        }
        // agendaView.setCalendarSourceFactory(new Calendars());
    }

    private void logout() throws IOException {
        MainApp.setRoot("login");
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
