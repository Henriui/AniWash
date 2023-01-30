package aniwash.view;

import com.calendarfx.view.CalendarView;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.PopupWindow.AnchorLocation;

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
