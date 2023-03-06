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
    private static Map<String, Calendar> calendarMap = new HashMap<>();
    private static final ArrayList<Product> products = new ArrayList<Product>();
    private static CalendarSource familyCalendarSource = new CalendarSource("Product");
    private IProductDao productDao = new ProductDao();
    private IAnimalDao animalDao = new AnimalDao();
    private ICustomerDao customerDao = new CustomerDao();
    private IAppointmentDao appointmentDao = new AppointmentDao();
    private IEmployeeDao employeeDao = new EmployeeDao();
    private EventHandler<CalendarEvent> eventHandler = calendarEvent -> {
        System.out.println("Event: " + calendarEvent);
        Calendar calendar = calendarEvent.getEntry().getCalendar();
        System.out.println("Entry: " + calendarEvent.getEntry());

        if (!calendarEvent.getEntry().getId().startsWith("id")) {
            System.out.println("Entry id modified: " + calendarEvent.getEntry().getId());
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
            if (calendar == null) {
                return;
            }
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

    public void createCalendar(Product product) {
        productDao.addProduct(product);
        Calendar calendar = new Calendar(product.getName());
        calendar.setStyle(product.getStyle());
        calendar.addEventHandler(eventHandler);
        calendarMap.put(product.getName(), calendar);
        familyCalendarSource.getCalendars().add(calendar);
    }

    public Map<String, Calendar> getCalendarMap() {
        return calendarMap;
    }

    private void createDbDataTest() {

        ZonedDateTime startTime = ZonedDateTime.now();
        ZonedDateTime endTime = ZonedDateTime.now().plusHours(1);


        Product product = new Product("Animal wash", "Washing :) ", 50, "style1");
        Product product1 = new Product("Fur trimming", "Full fur trimming", 100, "style2");
        Product product2 = new Product("Nail Clipping", "Clip nails ", 70, "style3");

        Animal animal = new Animal("Pekka", "Kissa", "Maatiainen", 1, "Grau olen lehmäkissa");
        Animal animal1 = new Animal("Ulla", "Tiikeri", "Tiibetin", 1, "Miau");
        Animal animal2 = new Animal("Keijo", "Koira", "Husky", 1, "Wuff");
        Animal animal3 = new Animal("Urpo", "Koira", "Seinään juossut", 3, "Very lyttä yes");
        Animal animal4 = new Animal("Ransu", "Koira", "Amstaff", 9, "Uli uli");

        Customer customer = new Customer("Rasmus", "04012346", "rasmus@metropolia.fi", "Oikeekatu 5", "00565");
        Customer customer1 = new Customer("Lassi", "04012345", "lassi@metropolia.fi", "Jokutie 5", "00123");
        Customer customer2 = new Customer("Henri", "04012345", "henri@metropolia.fi", "Vakavapolku 8", "00144");
        Customer customer4 = new Customer("Jonne", "04012345", "jonne@metropolia.fi", "Pallotie 2", "00444");
        Customer customer3 = new Customer("Martti", "04012345", "masa@metropolia.fi", "Märinäkatu 9", "00666");
        Employee employee = new Employee("giga", "passy", "Gigachad", "Kekkonen@lepposa.fi", "Manageri", UserType.EMPLOYEE);
        Appointment appointment = new Appointment(startTime, endTime, "Hullingolla");
        Appointment appointment1 = new Appointment(startTime.plusDays(1), endTime.plusDays(1), "On");
        Appointment appointment2 = new Appointment(startTime.plusDays(2), endTime.plusDays(2), "Kiva olla");


        customer.addAnimal(animal);
        appointment.addCustomer(customer);
        appointment.addEmployee(employee);
        appointment.addProduct(product);
        appointment.addAnimal(animal);
        customer1.addAnimal(animal1);
        appointment1.addCustomer(customer1);
        appointment1.addEmployee(employee);
        appointment1.addProduct(product1);
        appointment1.addAnimal(animal1);
        customer2.addAnimal(animal2);
        appointment2.addCustomer(customer2);
        appointment2.addEmployee(employee);
        appointment2.addProduct(product2);
        appointment2.addAnimal(animal2);

        productDao.addProduct(product);
        productDao.addProduct(product1);
        productDao.addProduct(product2);

        animalDao.addAnimal(animal);
        animalDao.addAnimal(animal1);
        animalDao.addAnimal(animal2);
        animalDao.addAnimal(animal3);
        animalDao.addAnimal(animal4);
        customerDao.addCustomer(customer);
        customerDao.addCustomer(customer1);
        customerDao.addCustomer(customer2);
        customerDao.addCustomer(customer3);
        customerDao.addCustomer(customer4);

        employeeDao.addEmployee(employee);
        appointmentDao.addAppointment(appointment);
        appointmentDao.addAppointment(appointment1);
        appointmentDao.addAppointment(appointment2);
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

