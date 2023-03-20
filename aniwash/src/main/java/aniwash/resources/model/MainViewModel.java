package aniwash.resources.model;

import aniwash.dao.*;
import aniwash.entity.Animal;
import aniwash.entity.Appointment;
import aniwash.entity.Customer;
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
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.calendarfx.model.CalendarEvent.ENTRY_CALENDAR_CHANGED;
import static com.calendarfx.model.CalendarEvent.ENTRY_INTERVAL_CHANGED;

public class MainViewModel {
    private static final Map<String, Calendar<Product>> calendarMap = new HashMap<>();
    private static final Map<String, IDao> daoMap = new HashMap<>();
    private static final CalendarSource familyCalendarSource = new CalendarSource("Product");

    private static boolean updateTrigger = false;

    public MainViewModel() {
        daoMap.put("product", new ProductDao());
        daoMap.put("customer", new CustomerDao());
        daoMap.put("animal", new AnimalDao());
        daoMap.put("appointment", new AppointmentDao());
        updateCalendar(true);
    }
    public void updateCalendar(boolean isInterAction) {
        if (!updateTrigger && !isInterAction) {
            updateTrigger = true;
            System.out.println("updateTrigger = true, updateCalendar is skipped");
            return;
        }
        familyCalendarSource.getCalendars().clear();
        addCalendarsToCalendarSource();
        addEntriesToCalendar();
        updateTrigger = false;
    }
    public CalendarSource getFamilyCalendar() {
        return familyCalendarSource;
    }
    public Map<String, Calendar<Product>> getCalendarMap() {
        return calendarMap;
    }

    public Map<String, IDao> getDaoMap() {
        return daoMap;
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

    //location = animal name
    // title = product name
    // id = appointment id with "id" in front
    // calendar = product
    // userObject = appointment, so we can get the appointment from the calendar
    private void addEntriesToCalendar() {
        IAppointmentDao aDao = (AppointmentDao) daoMap.get("appointment");
        List<Appointment> appointmentList = new ArrayList<>(aDao.findAllAppointments());
        for (Appointment appointment : appointmentList) {
            Entry<Appointment> entry = new Entry<>();
            entry.setInterval(appointment.getStartDate(), appointment.getEndDate());
            entry.setLocation(appointment.getAnimalList().get(0).getName());
            entry.setTitle(appointment.getProductList().get(0).getName());
            entry.setId("id" + appointment.getId());
            entry.setCalendar(calendarMap.get(appointment.getProductList().get(0).getName()));
            entry.setUserObject(appointment);
            entry.userObjectProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("\n\nuserObjectProperty changed"); //useless atm
            });
            Calendar<Product> calendar = calendarMap.get(appointment.getProductList().get(0).getName());
            calendar.addEntry(entry);
        }
    }

    public Appointment createAppointment(ZonedDateTime zdtStart, ZonedDateTime zdtEnd, Customer selectedCustomer, Animal animal, Product product) {
        IAppointmentDao appointmentDao = (AppointmentDao) daoMap.get("appointment");
        Appointment appointment = new Appointment(zdtStart, zdtEnd, selectedCustomer.getName() + " " + product.getName());
        appointment.addCustomer(selectedCustomer);
        appointment.addAnimal(animal);
        appointment.addProduct(product);
        appointmentDao.addAppointment(appointment);
        System.out.println("addAppointmentE: " + " " + zdtStart.toString() + " " + product.getName() + " " + appointment.getId() + " \n");
        updateCalendar(true);
        return appointment;
    }
    public void updateAppointment(ZonedDateTime zdtStart, ZonedDateTime zdtEnd, Appointment appointment, Customer c, Animal a, Product p) {
        IAppointmentDao appointmentDao = (AppointmentDao) daoMap.get("appointment");
        appointment.setStartDate(zdtStart);
        appointment.setEndDate(zdtEnd);
        if (!(appointment.getProducts().contains(p))) {
            appointment.removeProduct(appointment.getProductList().get(0));
            appointment.addProduct(p);
        }
        if (!(appointment.getCustomers().contains(c))) {
            appointment.removeCustomer(appointment.getCustomerList().get(0));
            appointment.addCustomer(c);
        }
        if (!(appointment.getAnimals().contains(a))) {
            appointment.removeAnimal(appointment.getAnimalList().get(0));
            appointment.addAnimal(a);
        }
        // Database update for appointment
        appointmentDao.updateAppointment(appointment);
        updateCalendar(true);
    }

    public Customer newestCustomer() {
        ICustomerDao customerDao = (CustomerDao) daoMap.get("customer");
        return customerDao.findNewestCustomer();
    }

    public Animal newestPet() {
        IAnimalDao animalDao = (AnimalDao) daoMap.get("animal");
        return animalDao.findNewestAnimal();
    }

    public Product newestProduct() {
        IProductDao productDao = (ProductDao) daoMap.get("product");
        return productDao.findNewestProduct();
    }

    public ObservableList<Customer> getPeople() {
        ICustomerDao customerDao = (CustomerDao) daoMap.get("customer");
        return FXCollections.observableList(customerDao.findAllCustomer());
    }

    private static EventHandler<CalendarEvent> getEventHandler() {
        return calendarEvent -> {
            Calendar calendar = calendarEvent.getEntry().getCalendar();
            if (!calendarEvent.getEntry().getId().startsWith("id")) {
                System.out.println(LocalTime.now().toString() + " " + calendarEvent.getEntry() + " \n");
                return;
            }
            IAppointmentDao appointmentDao = (AppointmentDao) daoMap.get("appointment");
            EventType<? extends Event> eventType = calendarEvent.getEventType();
            if (eventType == ENTRY_CALENDAR_CHANGED) {
                if (calendar == null) {
                    if (appointmentDao.deleteByIdAppointment(ControllerUtilities.removeStringFromId(calendarEvent.getEntry().getId()))) {
                        System.out.println(LocalTime.now().toString() + " Appointment deleted: " + calendarEvent.getEntry().getId() + "\n");
                    }
                }
            } else if (eventType == ENTRY_INTERVAL_CHANGED) {
                if (calendar == null) {
                    return;
                }
                Appointment appointment = appointmentDao.findByIdAppointment(ControllerUtilities.removeStringFromId(calendarEvent.getEntry().getId()));
                appointment.setStartDate(calendarEvent.getEntry().getStartAsZonedDateTime());
                appointment.setEndDate(calendarEvent.getEntry().getEndAsZonedDateTime());
                if (appointmentDao.updateAppointment(appointment)) {
                    System.out.println(LocalTime.now().toString() + " Appointment date updated");
                }
            }
        };
    }
}

