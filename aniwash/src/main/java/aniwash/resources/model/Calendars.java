package aniwash.resources.model;

import aniwash.dao.*;
import aniwash.entity.*;
import aniwash.resources.utilities.ControllerUtilities;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.calendarfx.model.CalendarEvent.ENTRY_CALENDAR_CHANGED;
import static com.calendarfx.model.CalendarEvent.ENTRY_INTERVAL_CHANGED;
import static java.lang.String.valueOf;

public class Calendars {
    private static final Map<String, Calendar> calendarMap = new HashMap<>();
    private static final ArrayList<Product> products = new ArrayList<Product>();
    private static final CalendarSource familyCalendarSource = new CalendarSource("Product");
    private IProductDao productDao;
    private IAnimalDao animalDao;
    private ICustomerDao customerDao;
    private IAppointmentDao appointmentDao;
    private IEmployeeDao employeeDao;
    private final EventHandler<CalendarEvent> eventHandler = calendarEvent -> {
        System.out.println("Event: " + calendarEvent);
        Calendar calendar = calendarEvent.getEntry().getCalendar();
        System.out.println("Entry: " + calendarEvent.getEntry());

        if (!calendarEvent.getEntry().getId().startsWith("id")) {
            return;
        }

        EventType<? extends Event> eventType = calendarEvent.getEventType();
        if (eventType == ENTRY_CALENDAR_CHANGED) {
            if (calendar == null) {
                if (appointmentDao.deleteByIdAppointment(ControllerUtilities.longifyStringId(calendarEvent.getEntry().getId()))) {
                    System.out.println("Appointment id deleted: " + calendarEvent.getEntry().getId());
                }
            }
        } else if (eventType == ENTRY_INTERVAL_CHANGED) {
            Appointment appointment = appointmentDao.findByIdAppointment(ControllerUtilities.longifyStringId(calendarEvent.getEntry().getId()));
            appointment.setStartDate(calendarEvent.getEntry().getStartAsZonedDateTime());
            appointment.setEndDate(calendarEvent.getEntry().getEndAsZonedDateTime());
            if (appointmentDao.updateAppointment(appointment)) {
                System.out.println("Appointment date updated: " + appointment.getStartDate() + " - " + appointment.getEndDate());
            }
        }
    };


    public Calendars() {

    }

    public void initCalendar() {
        productDao = new ProductDao();
        animalDao = new AnimalDao();
        customerDao = new CustomerDao();
        appointmentDao = new AppointmentDao();
        employeeDao = new EmployeeDao();

        createDbDataTest();

        products.addAll(productDao.findAllProduct());
        // These are the calendars where entries will be added to.
        // Entries are Customers
        // Calendars are Products

        for (Product product : products) {
            Calendar calendar = new Calendar(product.getName());
            calendar.setStyle(product.getStyle());
            calendar.addEventHandler(eventHandler);
            calendarMap.put(product.getName(), calendar);
        }

        addEntriesToCalendar();

        // CalendarSource is a mother to all the calendars
        familyCalendarSource.getCalendars().addAll(calendarMap.values());
    }

    // Getters and Setters

    public void addAppointmentEntry(Entry entry, Calendar calendar) {
        System.out.println("addAppointmentEntry " + entry.getTitle() + ", location " + entry.getLocation() + " " + calendar.getName() + " " + entry.getUserObject());
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

    public Map<String, Calendar> getCalendarMap() {
        return calendarMap;
    }

    private void createDbDataTest() {

        for (int i = 1; i < 10; i++) {
            ZonedDateTime startTime = ZonedDateTime.now();
            ZonedDateTime endTime = ZonedDateTime.now().plusHours(1);

            Product product = new Product("Product " + i, "Description " + i, 10 + i, "style" + i);
            Animal animal = new Animal("AnimalName" + i, "koiru" + i, "huskie", 1 + i, "Murmur");
            Customer customer = new Customer("Customer " + i, "04012345 " + i, "customer@email.com", "Address " + i, "00123");
            Employee employee = new Employee("Employee " + i, "passy" + i, "Keijo" + i, "Kekkonen" + i, "Ulisija");
            Appointment appointment = new Appointment(startTime, endTime, "Description " + i);

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

        Customer customer = customerDao.findByIdCustomer(1);
        for (int i = 0; i < 5; ++i) {
            Animal animal = new Animal("c1EXTRAANIMAL" + i, "doggo" + i, "sekarekku", 1 + i, "uliuli");
            customer.addAnimal(animal);
            animalDao.addAnimal(animal);
        }
    }

    private void addEntriesToCalendar() {
        List<Appointment> appointmentList = appointmentDao.findAllAppointment();
        for (Appointment appointment : appointmentList) {
            Entry entry = new Entry();
            entry.changeStartDate(appointment.getStartDate().toLocalDate());
            entry.changeStartTime(appointment.getStartDate().toLocalTime());
            entry.changeEndDate(appointment.getEndDate().toLocalDate());
            entry.changeEndTime(appointment.getEndDate().toLocalTime());
            //location = customer name
            entry.setLocation(appointment.findAllAnimals().get(0).getName());
            // title = product name
            entry.setTitle(appointment.findAllProducts().get(0).getName());
            // id = appointment id
            entry.setId("id" + valueOf(appointment.getId()));
            // userObject = customer
            entry.setUserObject(appointment.findAllCustomers().get(0));
            addAppointmentEntry(entry, calendarMap.get(appointment.findAllProducts().get(0).getName()));
        }
    }

}

