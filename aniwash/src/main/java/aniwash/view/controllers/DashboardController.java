package aniwash.view.controllers;

import aniwash.MainApp;
import aniwash.dao.AppointmentDao;
import aniwash.entity.Appointment;
import aniwash.entity.Product;
import aniwash.viewmodels.MainViewModel;
import com.calendarfx.view.AgendaView;
import com.calendarfx.view.CalendarView;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.Month;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
    private BarChart<String, Double> barChart;
    @FXML
    private Text thisMonth;
    @FXML
    private Text thisYear;

    private final MainViewModel mainViewModel = new MainViewModel();
    @FXML
    private Text welcometext;

    public void initialize() {

        agendaView.getCalendarSources().addAll(mainViewModel.getFamilyCalendar());
        /*
         * try {
         * // Write welcome text based on time of day (good morning, afternoon, evening)
         * int time = LocalDateTime.now().getHour();
         * System.out.println(time);
         * if (time >= 0 && time < 12) {
         * welcometext.setText("Good morning, " +
         * MainApp.getBiscuit().getUser().getName() + "!");
         * } else if (time >= 12 && time < 18) {
         * welcometext.setText("Good afternoon, " +
         * MainApp.getBiscuit().getUser().getName() + "!");
         * } else {
         * welcometext.setText("Good evening, " +
         * MainApp.getBiscuit().getUser().getName() + "!");
         * }
         * } catch (BiscuitExeption e) {
         * System.out.println("Biscuit fuked up");
         * try {
         * logout();
         * } catch (IOException e1) {
         * e1.printStackTrace();
         * }
         * }
         */
        // agendaView.setCalendarSourceFactory(new Calendars());

        loadDataHistory();
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

    private void loadDataHistory() {
        Calendar now = Calendar.getInstance();
        AppointmentDao apptDao = new AppointmentDao();
        HashMap<Month, Double> monthlyDataMap = new HashMap<>();
        // Create series for monthly data
        XYChart.Series<String, Double> monthlyData = new XYChart.Series();

        // Set chart name to current year
        monthlyData.setName(String.valueOf(now.get(Calendar.YEAR)));

        List<Appointment> appt = apptDao.fetchAppointments();

        for (Appointment a : appt) {
            // Show only current year.
            if (a.getEndDate().getYear() != now.get(Calendar.YEAR))
                break;

            // go through this years appointments products and calculate monthly revenue.
            for (Product p : a.getProducts()) {
                // if the month is already in the map, add the price to the value.
                Month m = a.getEndDate().getMonth();
                if (!monthlyDataMap.containsKey(m))
                    monthlyDataMap.put(m, p.getPrice());

                else
                    monthlyDataMap.put(m, monthlyDataMap.get(m) + p.getPrice());
            }
        }
        // If yearly data is empty, return.
        if(monthlyDataMap.isEmpty())
            return;

        // Add data to series
        for (Month m : monthlyDataMap.keySet()) {
            monthlyData.getData().add(new XYChart.Data(m.toString(), monthlyDataMap.get(m)));
        }

        barChart.getData().add(monthlyData);

        // set text of revenue from this month and year.
        thisMonth.setText(String.valueOf(monthlyDataMap.get(now.get(Calendar.MONTH))));
        
        double total = 0;
        // Go through the hashmap and count total revenue for year
        for(Month m : monthlyDataMap.keySet()){
            total += monthlyDataMap.get(m);
        }
        thisYear.setText(String.valueOf(total));
    }
}
