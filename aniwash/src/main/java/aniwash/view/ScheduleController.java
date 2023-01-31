package aniwash.view;

import java.io.IOException;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.view.CalendarView;

import aniwash.MainApp;
import aniwash.resources.model.PopUp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class ScheduleController {
    
    @FXML
    private CalendarView calendarView;
    @FXML
    private AnchorPane background;
    @FXML
    private Button dashboardButton;

    public void initialize() {
        Calendar holidays = new Calendar("Holidays");

        holidays.setStyle(Style.STYLE2);
        CalendarSource myCalendarSource = new CalendarSource("My Calendars");
        myCalendarSource.getCalendars().addAll(holidays);
        calendarView.getCalendarSources().addAll(myCalendarSource);

        //calendarView.setEntryDetailsCallback(new PopUp());
        calendarView.getCalendars();

       
        //background.getChildren().add(calendarView);
    }

    @FXML
    private void dashBoard() throws IOException {
        MainApp.setRoot("mainView");
    }

}
