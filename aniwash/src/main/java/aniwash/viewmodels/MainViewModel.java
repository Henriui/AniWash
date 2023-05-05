package aniwash.viewmodels;

import aniwash.MainApp;
import aniwash.dao.*;
import aniwash.datastorage.DatabaseConnector;
import aniwash.entity.*;
import aniwash.entity.localization.LocalizedAppointment;
import aniwash.entity.localization.LocalizedId;
import aniwash.view.utilities.ControllerUtilities;
import com.calendarfx.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.TextArea;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.calendarfx.model.CalendarEvent.ENTRY_CALENDAR_CHANGED;
import static com.calendarfx.model.CalendarEvent.ENTRY_INTERVAL_CHANGED;

/**
 * The MainViewModel class manages the calendar and database connections for a scheduling application.
 */
public class MainViewModel {

    private static final Map<String, Calendar<Product>> calendarMap = new HashMap<>();
    private static final Map<String, IDao> daoMap = new HashMap<>();
    private static final CalendarSource familyCalendarSource = new CalendarSource("Product");

    private static boolean updateTrigger = false;

    // This is the constructor for the `MainViewModel` class. It initializes the database connection
    // using `DatabaseConnector.openDbConnection("com.aniwash.test")`, creates instances of the DAO
    // classes (`ProductDao`, `CustomerDao`, `AnimalDao`, and `AppointmentDao`) and adds them to the
    // `daoMap`, and then calls the `updateCalendar` method with a `true` parameter to update the
    // calendar.
    public MainViewModel() {
        DatabaseConnector.openDbConnection("com.aniwash");
        daoMap.put("product", new ProductDao());
        daoMap.put("customer", new CustomerDao());
        daoMap.put("animal", new AnimalDao());
        daoMap.put("appointment", new AppointmentDao());
        updateCalendar(true);
    }

    public void updateCalendar(boolean isInterAction) {
        if (!updateTrigger && !isInterAction) {
            updateTrigger = true;
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

    /**
     * The function creates a calendar for a product and adds it to a map of calendars.
     * 
     * @param product The product object that needs to be added to the calendar. It contains
     * information about the product such as its name, style, and other details.
     */
    public void createCalendar(Product product) {
        IProductDao productDao = (ProductDao) daoMap.get("product");
        productDao.add(product);
        Calendar<Product> calendar = new Calendar<>(product.getName(MainApp.getLocale().getLanguage()), product);
        calendar.setStyle(product.getStyle());
        calendar.addEventHandler(getEventHandler());
        calendarMap.put(product.getName(MainApp.getLocale().getLanguage()), calendar);
        familyCalendarSource.getCalendars().add(calendarMap.get(product.getName(MainApp.getLocale().getLanguage())));
    }

    /**
     * The function adds calendars for each product in the productDao to the familyCalendarSource.
     */
    private void addCalendarsToCalendarSource() {
        IProductDao productDao = (ProductDao) daoMap.get("product");
        for (Product product : productDao.findAll()) {
            Calendar<Product> calendar = new Calendar<>(product.getName(MainApp.getLocale().getLanguage()), product);
            calendar.setStyle(product.getStyle());
            calendar.addEventHandler(getEventHandler());
            calendarMap.put(product.getName(MainApp.getLocale().getLanguage()), calendar);
            familyCalendarSource.getCalendars()
                    .add(calendarMap.get(product.getName(MainApp.getLocale().getLanguage())));
        }

    }

    /**
     * - Location is the name of the Animal in the entry
     * and not the location of the appointment
     * - Title is the name of the Product/Calendar in the entry
     *
     * @author rasmushy
     */
    private void addEntriesToCalendar() {
        IAppointmentDao aDao = (AppointmentDao) daoMap.get("appointment");
        List<Appointment> appointmentList = new ArrayList<>(aDao.fetchAppointments());
        for (Appointment appointment : appointmentList) {
            Product mainProduct = getMainProduct(appointment.getMainProductId(), appointment.getProductList());
            assert mainProduct != null;
            if (mainProduct == null)
                continue;
            Entry<Appointment> entry = new Entry<>(mainProduct.getName(MainApp.getLocale().getLanguage()),
                    new Interval(appointment.getStartDate(), appointment.getEndDate()), "id" + appointment.getId());
            entry.setLocation(appointment.getAnimalList().get(0).getName());
            entry.setCalendar(calendarMap.get(mainProduct.getName(MainApp.getLocale().getLanguage())));
            entry.setUserObject(appointment);
        }
    }

    /**
     * This function creates an appointment with a start and end time, adds a customer and animal to
     * it, adds products with discounts and a total price, sets a description if provided, and adds it
     * to the appointment database.
     * 
     * @param zdtStart The start date and time of the appointment in the form of a ZonedDateTime
     * object.
     * @param zdtEnd The end date and time of the appointment in the form of a ZonedDateTime object.
     * @param selectedCustomer The customer selected for the appointment.
     * @param animal An object of the Animal class representing the animal for which the appointment is
     * being created.
     * @param mainProductId The ID of the main product associated with the appointment.
     * @param p A map that contains products as keys and discounts as values.
     * @param descriptionText A TextArea object that contains the description of the appointment.
     * @return An Appointment object is being returned.
     */
    public Appointment createAppointment(ZonedDateTime zdtStart, ZonedDateTime zdtEnd, Customer selectedCustomer,
            Animal animal, long mainProductId, Map<Product, Discount> p, TextArea descriptionText) {
        IAppointmentDao appointmentDao = (AppointmentDao) daoMap.get("appointment");
        Appointment appointment = new Appointment(zdtStart, zdtEnd);
        LocalizedAppointment localAppointment = new LocalizedAppointment(appointment,
                "Appointment for " + selectedCustomer.getName());
        localAppointment.setId(new LocalizedId("en"));
        appointment.addCustomer(selectedCustomer);
        appointment.addAnimal(animal);
        // Add all products prices together
        double totalPrice = 0;
        for (Product product : p.keySet()) {
            appointment.addProduct(product, p.get(product));
            totalPrice += product.getPrice();
        }
        appointment.setTotalPrice(totalPrice);
        appointment.getLocalizations().put(MainApp.getLocale().getLanguage(), localAppointment);

        if (descriptionText.getText() != null && !descriptionText.getText().isEmpty()) {
            appointment.getLocalizations().get(MainApp.getLocale().getLanguage())
                    .setDescription(descriptionText.getText());
        }
        appointmentDao.add(appointment);
        appointment.setMainProductId(mainProductId);

        updateCalendar(true);
        return appointment;
    }

    /**
     * This function updates an appointment with new start and end times, customers, animals, products,
     * and a description.
     * 
     * @param zdtStart The start date and time of the appointment in the form of a ZonedDateTime
     * object.
     * @param zdtEnd A ZonedDateTime object representing the end date and time of the appointment.
     * @param appointment An object of the Appointment class representing the appointment to be
     * updated.
     * @param c Customer object representing the customer associated with the appointment
     * @param a Animal object
     * @param p A Map that contains Product objects as keys and Discount objects as values.
     * @param mainProduct A Product object representing the main product associated with the
     * appointment.
     * @param descriptionText A TextArea object that contains the description of the appointment.
     */
    public void updateAppointment(ZonedDateTime zdtStart, ZonedDateTime zdtEnd, Appointment appointment, Customer c,
            Animal a, Map<Product, Discount> p, Product mainProduct, TextArea descriptionText) {
        IAppointmentDao appointmentDao = (AppointmentDao) daoMap.get("appointment");
        appointment.setStartDate(zdtStart);
        appointment.setEndDate(zdtEnd);

        for (Product product : p.keySet()) {
            if (!(appointment.getProductList().contains(product))) {
                appointment.addProduct(product, p.get(product));
            } else if (appointment.getProductList().contains(product)) {
                appointment.removeProduct(product, appointment.getDiscount(product));
                appointment.addProduct(product, p.get(product));
            }
        }

        if (!(appointment.getCustomers().contains(c))) {
            appointment.removeCustomer(appointment.getCustomerList().get(0));
            appointment.addCustomer(c);
        }
        if (!(appointment.getAnimals().contains(a))) {
            appointment.removeAnimal(appointment.getAnimalList().get(0));
            appointment.addAnimal(a);
        }
        appointment.setMainProductId(mainProduct.getId());
        // Database update for appointment
        appointmentDao.update(appointment);
        if (descriptionText.getText() != null && !descriptionText.getText().isEmpty()) {
            appointment.getLocalizations().get(MainApp.getLocale().getLanguage())
                    .setDescription(descriptionText.getText());
        }

        updateCalendar(true);
    }

    public Customer newestCustomer() {
        ICustomerDao customerDao = (CustomerDao) daoMap.get("customer");
        return customerDao.findNewest();
    }

    public Animal newestPet() {
        IAnimalDao animalDao = (AnimalDao) daoMap.get("animal");
        return animalDao.findNewest();
    }

    public Product newestProduct() {
        IProductDao productDao = (ProductDao) daoMap.get("product");
        return productDao.findNewest();
    }

    private Product getMainProduct(long id, List<Product> productList) {
        for (Product product : productList) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public ObservableList<Customer> getPeople() {
        ICustomerDao customerDao = (CustomerDao) daoMap.get("customer");
        return FXCollections.observableList(customerDao.findAll());
    }

    /**
     * This function returns an event handler that updates or deletes appointments based on changes to
     * a calendar event.
     * 
     * @return A static EventHandler that takes a CalendarEvent as input and performs certain actions
     * based on the type of event and the state of the associated calendar and appointment data.
     */
    private static EventHandler<CalendarEvent> getEventHandler() {
        return calendarEvent -> {
            Calendar calendar = calendarEvent.getEntry().getCalendar();
            if (!calendarEvent.getEntry().getId().startsWith("id")) {
                return;
            }
            IAppointmentDao appointmentDao = (AppointmentDao) daoMap.get("appointment");
            EventType<? extends Event> eventType = calendarEvent.getEventType();
            if (eventType == ENTRY_CALENDAR_CHANGED) {
                if (calendar == null) {
                    if (appointmentDao
                            .deleteById(ControllerUtilities.removeStringFromId(calendarEvent.getEntry().getId()))) {
                        // System.out.println(LocalTime.now().toString() + " Appointment deleted: " +
                        // calendarEvent.getEntry().getId() + "\n");
                        updateTrigger = true;
                    }
                }
            } else if (eventType == ENTRY_INTERVAL_CHANGED) {
                if (calendar == null) {
                    return;
                }
                Appointment appointment = appointmentDao
                        .findById(ControllerUtilities.removeStringFromId(calendarEvent.getEntry().getId()));
                appointment.setStartDate(calendarEvent.getEntry().getStartAsZonedDateTime());
                appointment.setEndDate(calendarEvent.getEntry().getEndAsZonedDateTime());
                if (appointmentDao.update(appointment)) {
                    // System.out.println(LocalTime.now().toString() + " Appointment updated: " +
                    // calendarEvent.getEntry().getId() + "\n");
                    updateTrigger = true;
                }
            }
        };
    }

}
