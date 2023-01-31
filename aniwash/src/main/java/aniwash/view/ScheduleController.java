package aniwash.view;

import com.calendarfx.view.CalendarView;

import aniwash.resources.model.PopUp;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class ScheduleController {
    @FXML
    CalendarView calendarView;
    @FXML
    AnchorPane background;

    public void initialize() {
        calendarView.setEntryDetailsCallback(new PopUp());
        calendarView.getCalendars();
        //background.getChildren().add(calendarView);
    }

}
