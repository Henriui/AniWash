package aniwash.resources.model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.view.AgendaView;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl.CreateCalendarSourceParameter;

import aniwash.MainApp;
import aniwash.dao.ProductDao;
import aniwash.entity.Product;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class Calendars {
    private Product product;
    private ProductDao productDao;

    private static ArrayList<Calendar> calendars = new ArrayList<Calendar>();

    private static ArrayList<Style> styles = new ArrayList<Style>();
    private static ArrayList<Product> products = new ArrayList<Product>();
    private static CalendarSource familyCalendarSource = new CalendarSource("Product");
    
    public Calendars() {

    }

    // FIXME: This is a test method, it will be removed later

    public void initCalendar() {
      
        styles.add(Style.STYLE1);
        styles.add(Style.STYLE2);
        styles.add(Style.STYLE3);
        styles.add(Style.STYLE4);
        styles.add(Style.STYLE5);
        styles.add(Style.STYLE6);
        styles.add(Style.STYLE7);

        products.add(new Product("Teeth Care", "Caring Teeth", 35));
        products.add(new Product("Nails", "Caring Nails", 30));
        products.add(new Product("Trimming", "Trimming fur", 45));
        products.add(new Product("Washing", "Washing", 15));
        products.add(new Product("Testicle remove", "Removing testicles", 150));
        products.add(new Product("Chiropractice", "Chriropracticing", 80));
        products.add(new Product("Day care", "Day caring", 100));
        
        // These are the calendars where entries will be added to. 
        // Entries are Customers
        // Calendars are Products

        for (Product product : products) {
            //productDao.addProduct(product);

            Calendar calendar = new Calendar(product.getName());
            calendar.setStyle(styles.get(products.indexOf(product)));
            calendars.add(calendar);
        }
        
        // CalendarSource is a mother to all the calendars

        familyCalendarSource.getCalendars().addAll(calendars);
    }

    public void addAppoitmEntry(Entry entry, Calendar calendar) {
        System.out.println("addAppoitmEntry " + entry.getTitle() + ", location " + entry.getLocation() + " " + calendar.getName() + " " + entry.getUserObject());
        calendar.addEntry(entry);
    }

    // Getters and Setters

    public CalendarSource getCalendarss() {
        return familyCalendarSource;
    }

    // This is a test method, it will be removed later

    public Calendar createCalendar(String name) {
        Calendar calendar = new Calendar(name);
        familyCalendarSource.getCalendars().add(calendar);
        return calendar;
    }

    public ArrayList<Calendar> getCalendars() {
        return calendars;
    }

}
