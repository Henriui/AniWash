package aniwash.view;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.view.AllDayView;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl;
import com.calendarfx.view.VirtualGrid;
import com.calendarfx.view.DateControl.CreateEntryParameter;
import com.calendarfx.view.DateControl.EntryContextMenuParameter;
import com.calendarfx.view.DateControl.EntryDetailsParameter;
import com.calendarfx.view.DateControl.EntryDetailsPopOverContentParameter;

import aniwash.MainApp;
import aniwash.resources.model.Calendars;
import aniwash.resources.model.CreatePopUp;
import aniwash.resources.model.EditPopUp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ScheduleController {
    private CreatePopUp popup =  new CreatePopUp();
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
        calendarView.setEntryDetailsCallback(new CreatePopUp());
    }

    @FXML
    private void dashBoard() throws IOException {
        MainApp.setRoot("mainView");
    }
    
    @FXML
    private void testWeekView() {
        calendarView.showWeekPage();
        Color selected = new Color(1, 1, 1, 1).rgb(255, 255, 255);
        testbutton.setTextFill(selected);
        testbutton.styleProperty().set("-fx-background-color: #1485ff");
        
        Color unselected = new Color(1, 1, 1, 1).rgb(117, 117, 117);
        listButton.setTextFill(unselected);
        listButton.styleProperty().set("-fx-background-color: transparent");
        
        monthButton.setTextFill(unselected);
        monthButton.styleProperty().set("-fx-background-color: transparent");
        
    }
    
    @FXML
    private void testListView() {
        calendarView.showDayPage();
        Color selected = new Color(1, 1, 1, 1).rgb(255, 255, 255);
        listButton.setTextFill(selected);
        listButton.styleProperty().set("-fx-background-color: #1485ff");
        Color unselected = new Color(1, 1, 1, 1).rgb(117, 117, 117);
        testbutton.setTextFill(unselected);
        testbutton.styleProperty().set("-fx-background-color: transparent");
        
        monthButton.setTextFill(unselected);
        monthButton.styleProperty().set("-fx-background-color: transparent");
    }
    
    @FXML
    private void testMonthlyView() {
        calendarView.showMonthPage();
        Color selected = new Color(1, 1, 1, 1).rgb(255, 255, 255);
        monthButton.styleProperty().set("-fx-background-color: #1485ff");
        monthButton.setTextFill(selected);
        
        Color unselected = new Color(1, 1, 1, 1).rgb(117, 117, 117);
        listButton.setTextFill(unselected);
        listButton.styleProperty().set("-fx-background-color: transparent");
        testbutton.setTextFill(unselected);
        testbutton.styleProperty().set("-fx-background-color: transparent");
    }

}
