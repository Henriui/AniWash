package aniwash.view.controllers;

import aniwash.MainApp;
import aniwash.view.model.CreatePopUp;
import aniwash.viewmodels.MainViewModel;
import com.calendarfx.view.CalendarView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.time.LocalTime;

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
    private MainViewModel mainViewModel = new MainViewModel();

    public void initialize() {
        calendarView.getCalendarSources().addAll(mainViewModel.getFamilyCalendar());
        calendarView.setShowDeveloperConsole(true);
        calendarView.setRequestedTime(LocalTime.now());
        calendarView.setEntryDetailsCallback(new CreatePopUp());
/*
        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (calendarView.isVisible()) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                        mainViewModel.updateCalendar(false);
                    });
                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
*/
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
/*
        mainViewModel.updateCalendar(true);
*/
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
/*
        mainViewModel.updateCalendar(true);
*/
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
/*
        mainViewModel.updateCalendar(true);
*/
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
