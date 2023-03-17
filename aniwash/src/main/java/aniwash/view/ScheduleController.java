package aniwash.view;

import aniwash.MainApp;
import aniwash.resources.model.CreatePopUp;
import aniwash.resources.model.ModelViewViewmodel;
import com.calendarfx.view.CalendarView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;

public class ScheduleController {
    private CreatePopUp popup = new CreatePopUp();
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
    private ModelViewViewmodel modelViewViewmodel = new ModelViewViewmodel();

    public void initialize() {
        calendarView.getCalendarSources().addAll(modelViewViewmodel.getFamilyCalendar());
        calendarView.setEntryDetailsCallback(new CreatePopUp());
    }

    @FXML
    private void dashBoard() throws IOException {
        MainApp.setRoot("mainView");
    }

    @FXML
    private void customers() throws IOException {
        MainApp.setRoot("customerView");
    }
    @FXML
    private void products() throws IOException {
        MainApp.setRoot("productsView");
    }
    @FXML
    private void admin() throws IOException {
        MainApp.setRoot("AdminPanel");
    }

    @FXML
    private void testWeekView() {
        calendarView.showWeekPage();
        modelViewViewmodel.updateCalendar();
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
        modelViewViewmodel.updateCalendar();
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
        modelViewViewmodel.updateCalendar();
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
