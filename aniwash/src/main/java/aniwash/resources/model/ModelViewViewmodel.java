package aniwash.resources.model;

import aniwash.dao.*;
import aniwash.entity.Appointment;
import aniwash.entity.Product;
import aniwash.resources.utilities.ControllerUtilities;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static com.calendarfx.model.CalendarEvent.ENTRY_CALENDAR_CHANGED;
import static com.calendarfx.model.CalendarEvent.ENTRY_INTERVAL_CHANGED;

public class ModelViewViewmodel {
    private static Map<String, Calendar<Product>> calendarMap = new HashMap<>();
    private static Map<String, IDao> daoMap = new HashMap<>();
    private static CalendarSource familyCalendarSource = new CalendarSource("Product");
    private static ObservableList<Appointment> appointmentList;

    public ModelViewViewmodel() {
        daoMap.put("product", new ProductDao());
        daoMap.put("customer", new CustomerDao());
        daoMap.put("animal", new AnimalDao());
        daoMap.put("appointment", new AppointmentDao());
    }

    public void initCalendar() {
/*
        IProductDao productDao = (ProductDao) daoMap.get("product");
        for (Product product : productDao.findAllProduct()) {
            createCalendar(product);
        }
*/
        addCalendarsToCalendarSource();
        addEntriesToCalendar();
        // CalendarSource is a mother to all the calendars
    }

    public void addAppointmentEntry(Entry<Appointment> entry, Calendar<Product> calendar) {
        System.out.println("addAppointmentE: " + calendar.getName() + " " + entry.getTitle() + " " + entry.getInterval() + " " + entry.getId() + " \n");
        calendar.addEntry(entry);
    }
    // This is a test method, it will be removed later

    public CalendarSource getFamilyCalendar() {
        return familyCalendarSource;
    }

    public void createCalendar(Product product) {
        IProductDao productDao = (ProductDao) daoMap.get("product");
        productDao.addProduct(product);
        Calendar<Product> calendar = new Calendar<>(product.getName());
        calendar.setUserObject(product);
        calendar.setStyle(product.getStyle());
        calendar.addEventHandler(getEventHandler());
        calendarMap.put(product.getName(), calendar);
        familyCalendarSource.getCalendars().add(calendarMap.get(product.getName()));
    }

    private static EventHandler<CalendarEvent> getEventHandler() {
        EventHandler<CalendarEvent> eventHandler = calendarEvent -> {
            Calendar calendar = calendarEvent.getEntry().getCalendar();
            if (!calendarEvent.getEntry().getId().startsWith("id")) {
                System.out.println(LocalTime.now().toString() + " " + calendarEvent.getEntry() + " \n");
                return;
            }
            IAppointmentDao appointmentDao = (AppointmentDao) daoMap.get("appointment");
            EventType<? extends Event> eventType = calendarEvent.getEventType();
            if (eventType == ENTRY_CALENDAR_CHANGED) {
                if (calendar == null) {
                    if (appointmentDao.deleteByIdAppointment(ControllerUtilities.longifyStringId(calendarEvent.getEntry().getId()))) {
                        System.out.println(LocalTime.now().toString() + " Appointment deleted: " + calendarEvent.getEntry().getId() + "\n");
                    }
                }
            } else if (eventType == ENTRY_INTERVAL_CHANGED) {
                if (calendar == null) {
                    return;
                }
                Appointment appointment = appointmentDao.findByIdAppointment(ControllerUtilities.longifyStringId(calendarEvent.getEntry().getId()));
                appointment.setStartDate(calendarEvent.getEntry().getStartAsZonedDateTime());
                appointment.setEndDate(calendarEvent.getEntry().getEndAsZonedDateTime());
                if (appointmentDao.updateAppointment(appointment)) {
                    System.out.println(LocalTime.now().toString() + " Appointment date updated");
                }
            }
        };
        return eventHandler;
    }

    public Map<String, Calendar<Product>> getCalendarMap() {
        return calendarMap;
    }

    public Map<String, IDao> getDaoMap() {
        return daoMap;
    }

    private void addEntriesToCalendar() {
        IAppointmentDao aDao = (AppointmentDao) daoMap.get("appointment");
        appointmentList = FXCollections.observableArrayList(aDao.findAllAppointments());
        for (Appointment appointment : appointmentList) {
            Entry<Appointment> entry = new Entry<>();
            entry.setInterval(appointment.getStartDate(), appointment.getEndDate());
            //location = customer name
            entry.setLocation(appointment.getAnimalList().get(0).getName());
            // title = product name
            entry.setTitle(appointment.getProductList().get(0).getName());
            // id = appointment id
            entry.setId("id" + appointment.getId());
            // calendar = product name
            entry.setCalendar(calendarMap.get(appointment.getProductList().get(0).getName()));
            // userObject = appointment, so we can get the appointment from the calendar
            entry.setUserObject(appointment);
            entry.userObjectProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("\n\nuserObjectProperty changed");
            });
            addAppointmentEntry(entry, calendarMap.get(appointment.getProductList().get(0).getName()));
        }
    }

    public void updateCalendar() {
        familyCalendarSource.getCalendars().clear();
        addCalendarsToCalendarSource();
        addEntriesToCalendar();
    }

    private void addCalendarsToCalendarSource() {
        IProductDao productDao = (ProductDao) daoMap.get("product");
        for (Product product : productDao.findAllProduct()) {
            Calendar<Product> calendar = new Calendar<>(product.getName());
            calendar.setUserObject(product);
            calendar.setStyle(product.getStyle());
            calendar.addEventHandler(getEventHandler());
            calendarMap.put(product.getName(), calendar);
            familyCalendarSource.getCalendars().add(calendarMap.get(product.getName()));
        }

    }

}

