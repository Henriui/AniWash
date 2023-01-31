package aniwash.view;

import java.io.IOException;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.view.CalendarView;

import aniwash.MainApp;
import aniwash.resources.model.Calendars;
import aniwash.resources.model.PopUp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ScheduleController {

    @FXML
    private CalendarView calendarView;
    @FXML
    private AnchorPane background;
    @FXML
    private Button dashboardButton;
    @FXML
    private Button testbutton;
    @FXML
    private Button listButton;
    @FXML
    private Button monthButton;
    private Calendars calendars = new Calendars();

    public void initialize() {
        calendarView.getCalendarSources().addAll(calendars.getCalendarss());
        Color selected = new Color(1, 1, 1, 1).rgb(97, 143, 250);
        listButton.setTextFill(selected);
        // Calendar asd = calendars.creatCalendar("NewFun");
        // calendars.CreateEntry(asd, "NULLISATI", "NULLI", LocalDate.now(),
        // LocalTime.now());
        // calendarView.setEntryDetailsCallback(new PopUp());
        // background.getChildren().add(calendarView);
    }

    @FXML
    private void dashBoard() throws IOException {
        MainApp.setRoot("mainView");
    }

    @FXML
    private void testWeekView() {
        calendarView.showWeekPage();
        Color selected = new Color(1, 1, 1, 1).rgb(97, 143, 250);
        testbutton.setTextFill(selected);
        
        Color unselected = new Color(1, 1, 1, 1).rgb(117, 117, 117);
        listButton.setTextFill(unselected);
        monthButton.setTextFill(unselected);
    }

    @FXML
    private void testListView() {
        calendarView.showDayPage();
        Color selected = new Color(1, 1, 1, 1).rgb(97, 143, 250);
        listButton.setTextFill(selected);

        Color unselected = new Color(1, 1, 1, 1).rgb(117, 117, 117);
        testbutton.setTextFill(unselected);
        monthButton.setTextFill(unselected);
    }

    @FXML
    private void testMonthlyView() {
        calendarView.showMonthPage();
        Color selected = new Color(1, 1, 1, 1).rgb(97, 143, 250);
        monthButton.setTextFill(selected);

        Color unselected = new Color(1, 1, 1, 1).rgb(117, 117, 117);
        listButton.setTextFill(unselected);
        testbutton.setTextFill(unselected);
    }

}
