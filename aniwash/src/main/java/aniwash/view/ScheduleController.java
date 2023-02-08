package aniwash.view;

import java.io.IOException;

import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl.CreateEntryParameter;

import aniwash.MainApp;
import aniwash.resources.model.Calendars;
import aniwash.resources.model.CreatePopUp;
import aniwash.resources.model.EditPopUp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

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
        System.out.println("CalendarView initialized");
        calendarView.setEntryDetailsCallback(new CreatePopUp());

        //calendarView.entryDetailsCallbackProperty();

        calendarView.entryFactoryProperty().set(new Callback<CreateEntryParameter, Entry<?>>() {
            
            @Override
            public Entry<?> call(CreateEntryParameter param) {
                System.out.println("Entry created");
                Entry<?> entry = new Entry<>();
                FXMLLoader loader;
                Scene scene;
                try {
                    loader = loadFXML("asd");
                    scene = new Scene((Parent) loader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Henri oot gay");
                    //stage.initStyle(StageStyle.TRANSPARENT);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.show();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return entry;
            }
        });
       
        
        // Calendar asd = calendars.creatCalendar("NewFun");
        // calendars.CreateEntry(asd, "NULLISATI", "NULLI", LocalDate.now(),
        // LocalTime.now());
        // calendarView.setEntryDetailsCallback(new PopUp());
        // background.getChildren().add(calendarView);
    }

    private static FXMLLoader loadFXML(String fxml) throws IOException {
        // Finds fxml file from the resources folder.
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/" + fxml + ".fxml"));
        return fxmlLoader;
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
