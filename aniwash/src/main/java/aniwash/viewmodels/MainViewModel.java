package aniwash.viewmodels;

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

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.tool.schema.spi.SourceDescriptor;

import static com.calendarfx.model.CalendarEvent.ENTRY_CALENDAR_CHANGED;
import static com.calendarfx.model.CalendarEvent.ENTRY_INTERVAL_CHANGED;

public class MainViewModel {

    private static final Map<String, Calendar<Product>> calendarMap = new HashMap<>();
    private static final Map<String, IDao> daoMap = new HashMap<>();
    private static final CalendarSource familyCalendarSource = new CalendarSource("Product");

    private static boolean updateTrigger = false;

    public MainViewModel() {
        // TODO: Test db connection, will be removed later
        DatabaseConnector.openDbConnection("com.aniwash.test");
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
        Calendar<Product> calendar = new Calendar<>(product.getName("en"), product);
        calendar.setStyle(product.getStyle());
        calendar.addEventHandler(getEventHandler());
        calendarMap.put(product.getName("en"), calendar);
        familyCalendarSource.getCalendars().add(calendarMap.get(product.getName("en")));
    }

    private void addCalendarsToCalendarSource() {
        IProductDao productDao = (ProductDao) daoMap.get("product");
        for (Product product : productDao.findAllProducts()) {
            Calendar<Product> calendar = new Calendar<>(product.getName("en"), product);
            calendar.setStyle(product.getStyle());
            calendar.addEventHandler(getEventHandler());
            calendarMap.put(product.getName("en"), calendar);
            familyCalendarSource.getCalendars().add(calendarMap.get(product.getName("en")));
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
            if (mainProduct == null) {
                System.out.println("mainProduct is null");
                continue;
            }
            Entry<Appointment> entry = new Entry<>(mainProduct.getName("en"),
                    new Interval(appointment.getStartDate(), appointment.getEndDate()), "id" + appointment.getId());
            entry.setLocation(appointment.getAnimalList().get(0).getName());
            entry.setCalendar(calendarMap.get(mainProduct.getName("en")));
            entry.setUserObject(appointment);
        }
    }

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
        appointment.getLocalizations().put("en", localAppointment);

        if( descriptionText.getText() != null && !descriptionText.getText().isEmpty() ) {
            appointment.getLocalizations().get("en").setDescription(descriptionText.getText());
        }
        appointmentDao.addAppointment(appointment);
        // System.out.println("addAppointmentE: " + " " + zdtStart.toString() + " " +
        // product.getName("en") + " " + appointment.getId() + " \n");
        appointment.setMainProductId(mainProductId);

        updateCalendar(true);
        return appointment;
    }

    public void updateAppointment(ZonedDateTime zdtStart, ZonedDateTime zdtEnd, Appointment appointment, Customer c,
            Animal a, Map<Product, Discount> p,
            Product mainProduct, TextArea descriptionText) {
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
        appointmentDao.updateAppointment(appointment);
        if (descriptionText.getText() != null && !descriptionText.getText().isEmpty()) {
            appointment.getLocalizations().get("en").setDescription(descriptionText.getText());
        }
    
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
        return FXCollections.observableList(customerDao.findAllCustomer());
    }

    private static EventHandler<CalendarEvent> getEventHandler() {
        return calendarEvent -> {
            Calendar calendar = calendarEvent.getEntry().getCalendar();
            if (!calendarEvent.getEntry().getId().startsWith("id")) {
                // System.out.println(LocalTime.now().toString() + " " +
                // calendarEvent.getEntry() + " \n");
                return;
            }
            IAppointmentDao appointmentDao = (AppointmentDao) daoMap.get("appointment");
            EventType<? extends Event> eventType = calendarEvent.getEventType();
            if (eventType == ENTRY_CALENDAR_CHANGED) {
                if (calendar == null) {
                    if (appointmentDao.deleteByIdAppointment(
                            ControllerUtilities.removeStringFromId(calendarEvent.getEntry().getId()))) {
                        System.out.println(LocalTime.now().toString() + " Appointment deleted: "
                                + calendarEvent.getEntry().getId() + "\n");
                    }
                }
            } else if (eventType == ENTRY_INTERVAL_CHANGED) {
                if (calendar == null) {
                    return;
                }
                Appointment appointment = appointmentDao
                        .findByIdAppointment(ControllerUtilities.removeStringFromId(calendarEvent.getEntry().getId()));
                appointment.setStartDate(calendarEvent.getEntry().getStartAsZonedDateTime());
                appointment.setEndDate(calendarEvent.getEntry().getEndAsZonedDateTime());
                if (appointmentDao.updateAppointment(appointment)) {
                    System.out.println(LocalTime.now().toString() + " Appointment date updated");
                }
            }
        };
    }

}
