package aniwash.resources.model;

import aniwash.dao.*;
import aniwash.entity.*;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.calendarfx.model.CalendarEvent.*;
import static java.lang.String.valueOf;

public class Calendars {
    private static Map<String, Calendar> calendars = new HashMap<>();
    private static ArrayList<Style> styles = new ArrayList<Style>();
    private static ArrayList<Product> products = new ArrayList<Product>();
    private static CalendarSource familyCalendarSource = new CalendarSource("Product");

    private IProductDao productDao;
    private IAnimalDao animalDao;
    private ICustomerDao customerDao;
    private IAppointmentDao appointmentDao;
    private final EventHandler<CalendarEvent> eventHandler = calendarEvent -> {
        System.out.println("Event: " + calendarEvent);
        EventType<? extends Event> eventType = calendarEvent.getEventType();
        Calendar calendar = calendarEvent.getEntry().getCalendar();
        if (ENTRY_CALENDAR_CHANGED.equals(eventType)) {
            System.out.println("Entry: " + calendarEvent.getEntry());
            if (calendar == null) {
                if (appointmentDao.deleteByIdAppointment(Long.valueOf(calendarEvent.getEntry().getId()))) {
                    System.out.println("Appointment id deleted: " + calendarEvent.getEntry().getId());
                }
            }
        } else if (ENTRY_INTERVAL_CHANGED.equals(eventType)) {
            if (calendar != null) {
                Appointment appointment = appointmentDao.findByIdAppointment(Long.valueOf(calendarEvent.getEntry().getId()));
                appointment.setDate(calendarEvent.getEntry().getStartAsZonedDateTime());
                if (appointmentDao.updateAppointment(appointment)) {
                    System.out.println("Appointment date updated: " + appointment.getDate());
                }
            }

        } else if (ENTRY_TITLE_CHANGED.equals(eventType)) {
            if (calendar != null) {
                Appointment appointment = appointmentDao.findByIdAppointment(Long.valueOf(calendarEvent.getEntry().getId()));
                Product product = productDao.findByNameProduct(calendarEvent.getEntry().getTitle());
                appointment.removeProduct(appointment.getProducts().toArray(new Product[appointment.getProducts().size()])[0]);
                appointment.addProduct(product);
                if (appointmentDao.updateAppointment(appointment)) {
                    System.out.println("Appointment product updated: " + appointment.getProducts().toArray(new Product[appointment.getProducts().size()])[0].getName());
                }
            }
        } else if (ENTRY_LOCATION_CHANGED.equals(eventType)) {
            if (calendar != null) {
                Appointment appointment = appointmentDao.findByIdAppointment(Long.valueOf(calendarEvent.getEntry().getId()));
                appointment.setDescription(calendarEvent.getEntry().getLocation());
                if (appointmentDao.updateAppointment(appointment)) {
                    System.out.println("Appointment description updated: " + appointment.getDescription());
                }
            }
        } else if (ENTRY_USER_OBJECT_CHANGED.equals(eventType)) {
            if (calendar != null) {
                Appointment appointment = appointmentDao.findByIdAppointment(Long.valueOf(calendarEvent.getEntry().getId()));
                Customer customer = (Customer) calendarEvent.getEntry().getUserObject();
                appointment.removeCustomer(appointment.getCustomers().toArray(new Customer[appointment.getCustomers().size()])[0]);
                appointment.addCustomer(customer);
                if (appointmentDao.updateAppointment(appointment)) {
                    System.out.println("Appointment customer updated: " + appointment.getCustomers().toArray(new Customer[appointment.getCustomers().size()])[0].getName());
                }
            }

        }
    };
    private IEmployeeDao employeeDao;

    // FIXME: This is a test method, it will be removed later

    public Calendars() {

    }

    public void initCalendar() {
        styles.add(Style.STYLE1);
        styles.add(Style.STYLE2);
        styles.add(Style.STYLE3);
        styles.add(Style.STYLE4);
        styles.add(Style.STYLE5);
        styles.add(Style.STYLE6);
        styles.add(Style.STYLE7);

        String style = "style8";
        productDao = new ProductDao();
        animalDao = new AnimalDao();
        customerDao = new CustomerDao();
        appointmentDao = new AppointmentDao();
        employeeDao = new EmployeeDao();

        createDbDataTest();

        Customer customer = customerDao.findByIdCustomer(1);
        for (int i = 0; i < 10; ++i) {
            Animal animal = new Animal("EXTRAANIMALS" + i, "doggo" + i, "sekarekku", 1 + i, "uliuli");
            customer.addAnimal(animal);
            animal.addOwner(customer);
        }


        List<Product> productList = productDao.findAllProduct();
        products.addAll(productList);

/*
        products.add(new Product("Teeth Care", "Caring Teeth", 35));
        products.add(new Product("Nails", "Caring Nails", 30));
        products.add(new Product("Trimming", "Trimming fur", 45));
        products.add(new Product("Washing", "Washing", 15));
        products.add(new Product("Testicle remove", "Removing testicles", 150));
        products.add(new Product("Chiropractice", "Chriropracticing", 80));
        products.add(new Product("Day care", "Day caring", 100));
*/

        // These are the calendars where entries will be added to.
        // Entries are Customers
        // Calendars are Products

        for (Product product : products) {
            //productDao.addProduct(product);
            Calendar calendar = new Calendar(product.getName());
            calendar.setStyle(style);
            calendar.addEventHandler(eventHandler);
            calendars.put(product.getName(), calendar);
        }

        // CalendarSource is a mother to all the calendars

        addEntriesToCalendar();

        System.out.println("initCalendar style: " + calendars.get("Product 0").getStyle());
        familyCalendarSource.getCalendars().addAll(calendars.values());
    }

    // Getters and Setters

    public void addAppoitmEntry(Entry entry, Calendar calendar) {
        System.out.println("addAppoitmEntry " + entry.getTitle() + ", location " + entry.getLocation() + " " + calendar.getName() + " " + entry.getUserObject());
        calendar.addEntry(entry);
    }

    // This is a test method, it will be removed later

    public CalendarSource getCalendarss() {
        return familyCalendarSource;
    }

    public Calendar createCalendar(String name) {
        Calendar calendar = new Calendar(name);
        familyCalendarSource.getCalendars().add(calendar);
        return calendar;
    }

    public Map<String, Calendar> getCalendars() {
        return calendars;
    }

    private void createDbDataTest() {

        for (int i = 0; i < 10; i++) {
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            Product product = new Product("Product " + i, "Description " + i, 10 + i);
            Animal animal = new Animal("AnimalName" + i, "koiru" + i, "huskie", 1 + i, "Murmur");
            Customer customer = new Customer("Customer " + i, "04012345 " + i, "customer@email.com", "Address " + i, "00123");
            Employee employee = new Employee("Employee " + i, "passy" + i, "Keijo" + i, "Kekkonen" + i, "Ulisija");
            Appointment appointment = new Appointment(zonedDateTime, "Description " + i);

            customer.addAnimal(animal);
            appointment.addCustomer(customer);
            appointment.addEmployee(employee);
            appointment.addProduct(product);
            appointment.addAnimal(animal);

            productDao.addProduct(product);
            animalDao.addAnimal(animal);
            customerDao.addCustomer(customer);
            employeeDao.addEmployee(employee);
            appointmentDao.addAppointment(appointment);
        }
    }

    private void addEntriesToCalendar() {
        List<Appointment> appointmentArrayList = appointmentDao.findAllAppointment();
        for (Appointment appointment : appointmentArrayList) {
            Entry entry = new Entry();
            entry.changeStartDate(appointment.getDate().toLocalDate());
            entry.changeStartTime(appointment.getDate().toLocalTime());
            entry.changeEndDate(appointment.getDate().toLocalDate());
            entry.changeEndTime(appointment.getDate().toLocalTime().plus(1L, ChronoUnit.HOURS));
            entry.setLocation(appointment.getCustomers().toArray(new Customer[appointment.getCustomers().size()])[0].getName());
            entry.setTitle(appointment.getProducts().toArray(new Product[appointment.getProducts().size()])[0].getName());
            entry.setId(valueOf(appointment.getId()));
            entry.setUserObject(appointment.getCustomers().toArray(new Customer[appointment.getCustomers().size()])[0]);
            addAppoitmEntry(entry, calendars.get(appointment.getProducts().toArray(new Product[appointment.getProducts().size()])[0].getName()));
        }
    }

}

