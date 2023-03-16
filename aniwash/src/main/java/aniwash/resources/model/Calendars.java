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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.calendarfx.model.CalendarEvent.ENTRY_CALENDAR_CHANGED;
import static com.calendarfx.model.CalendarEvent.ENTRY_INTERVAL_CHANGED;

public class Calendars {
    private static Map<String, Calendar<Product>> calendarMap = new HashMap<>();
    private static Map<String, IDao> daoMap = new HashMap<>();
    private static CalendarSource familyCalendarSource = new CalendarSource("Product");

    public Calendars() {
        daoMap.put("product", new ProductDao());
        daoMap.put("customer", new CustomerDao());
        daoMap.put("animal", new AnimalDao());
        daoMap.put("appointment", new AppointmentDao());
    }

    public void initCalendar() {
        //    createDbDataTest();
        IProductDao productDao = (ProductDao) daoMap.get("product");
        for (Product product : productDao.findAllProduct()) {
            createCalendar(product);
        }
        addEntriesToCalendar();
        // CalendarSource is a mother to all the calendars
    }

    public void addAppointmentEntry(Entry<Appointment> entry, Calendar<Product> calendar) {
        System.out.println("addAppointmentEntry " + entry.getTitle() + ", location " + entry.getLocation() + " " + calendar.getName() + " " + entry.getUserObject());
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
            System.out.println("Entry: " + calendarEvent.getEntry() + "\n");
            if (!calendarEvent.getEntry().getId().startsWith("id")) {
                return;
            }
            IAppointmentDao appointmentDao = (AppointmentDao) daoMap.get("appointment");
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
        return eventHandler;
    }

    public Map<String, Calendar<Product>> getCalendarMap() {
        return calendarMap;
    }

    public Map<String, IDao> getDaoMap() {
        return daoMap;
    }

    private void createDbDataTest() {
        IProductDao productDao = (ProductDao) daoMap.get("product");
        ICustomerDao customerDao = (CustomerDao) daoMap.get("customer");
        IAnimalDao animalDao = (AnimalDao) daoMap.get("animal");
        IAppointmentDao appointmentDao = (AppointmentDao) daoMap.get("appointment");
        //TODO: remove this test data and create a test class for it
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
        Appointment appointment = new Appointment(startTime, endTime, "Hullingolla");
        Appointment appointment1 = new Appointment(startTime.plusDays(1), endTime.plusDays(1), "On");
        Appointment appointment2 = new Appointment(startTime.plusDays(2), endTime.plusDays(2), "Kiva olla");
        customer.addAnimal(animal);
        appointment.addCustomer(customer);
        appointment.addProduct(product);
        appointment.addAnimal(animal);
        customer1.addAnimal(animal1);
        appointment1.addCustomer(customer1);
        appointment1.addProduct(product1);
        appointment1.addAnimal(animal1);
        customer2.addAnimal(animal2);
        appointment2.addCustomer(customer2);
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
        appointmentDao.addAppointment(appointment);
        appointmentDao.addAppointment(appointment1);
        appointmentDao.addAppointment(appointment2);
    }

    private void addEntriesToCalendar() {
        IAppointmentDao appointmentDao = (AppointmentDao) daoMap.get("appointment");
        List<Appointment> appointmentList = appointmentDao.findAllAppointments();
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
            // add customer, product and animal to the entry, as properties
/*
            entry.getProperties().put("customer", appointment.getCustomerList().get(0));
            entry.getProperties().put("product", appointment.getProductList().get(0));
            entry.getProperties().put("animal", appointment.getAnimalList().get(0));
*/
            addAppointmentEntry(entry, calendarMap.get(appointment.getProductList().get(0).getName()));
        }
    }

}

