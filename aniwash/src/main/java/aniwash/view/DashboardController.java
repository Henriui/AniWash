package aniwash.view;

import aniwash.MainApp;
import aniwash.viewmodels.MainViewModel;
import com.calendarfx.view.AgendaView;
import com.calendarfx.view.CalendarView;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Locale;

public class DashboardController {
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
    private final MainViewModel mainViewModel = new MainViewModel();
    @FXML
    private Text welcometext;

    public void initialize() {

        agendaView.getCalendarSources().addAll(mainViewModel.getFamilyCalendar());
/*
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
            try {
                logout();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
*/
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

    @FXML
    private void admin() throws IOException {
        MainApp.setRoot("AdminPanel");
    }

    @FXML
    private void changeLanguage() throws IOException {
        if (MainApp.getLocale().getLanguage().equals("en")) {
            MainApp.setLocale(new Locale.Builder().setLanguage("fr").setRegion("FR").build());
        } else {
            MainApp.setLocale(new Locale.Builder().setLanguage("en").setRegion("US").build());
        }
        MainApp.setRoot("mainView");
    }
}

