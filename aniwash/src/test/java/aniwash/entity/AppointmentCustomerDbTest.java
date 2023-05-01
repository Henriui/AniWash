package aniwash.entity;

import aniwash.dao.AppointmentDao;
import aniwash.dao.CustomerDao;
import aniwash.dao.IAppointmentDao;
import aniwash.dao.ICustomerDao;
import aniwash.datastorage.DatabaseConnector;
import aniwash.entity.localization.LocalizedAppointment;
import aniwash.entity.localization.LocalizedId;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("AppointmentCustomerDAO: CRUD testings")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppointmentCustomerDbTest {

    private static IAppointmentDao aDao;
    private static ICustomerDao cDao;

    @BeforeAll
    public static void initAll() {
        DatabaseConnector.openDbConnection("com.aniwash.test");
        aDao = new AppointmentDao();
        cDao = new CustomerDao();
    }

    @AfterEach
    public void tearDown() {
        for (Appointment a : aDao.findAll()) {
            aDao.deleteById(a.getId());
        }
    }

    @Test
    @DisplayName("Create multiple appointments and customers test")
    @Order(1)
    public void testCreateMultipleAppointmentsAndProducts() {
        System.out.println("Cutomers: " + cDao.findAll().size());
        for (int i = 1; i < 4; i++) {
            Appointment a = new Appointment(ZonedDateTime.parse("2021-0" + i + "-03T10:15:30+02:00"), ZonedDateTime.parse("2021-0" + i + "-03T11:15:30+02:00"));
            LocalizedAppointment localAppointment = new LocalizedAppointment(a, "Elmo koiran pesu" + i);
            localAppointment.setId(new LocalizedId("en"));
            a.getLocalizations().put("en", localAppointment);

            Customer c = new Customer("Elmo Pohjonen" + i, "044355667" + i, "elmo.pohjonen" + i + "@gmail.com", "Kalakuja " + i, "0032" + i);
            aDao.add(a);
            cDao.add(c);
            a.addCustomer(c);
        }
        List<Customer> customers = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Customer c = new Customer("Tatu Pohjonen" + i, "044355662" + i, "tatu.pohjonen" + i + "@gmail.com", "Tuulikuja " + i, "0031" + i);
            customers.add(c);
        }
        for (Customer c : customers) {
            Appointment a = aDao.findNewest();
            cDao.add(c);
            a.addCustomer(c);
        }

        assertEquals(3, aDao.findAll().size(), "Appointment size should be 3");
        assertEquals(6, cDao.findAll().size(), "Customer size should be 6");
        assertEquals(4, aDao.findNewest().getCustomerList().size(), "Appointment size should be 4");
    }

    @Test
    @DisplayName("Find all customers from appointment test")
    @Order(2)
    public void findTest() {
        System.out.println("Find test");
        List<Appointment> appointmentList = aDao.findAll();
        List<Customer> customerList = new ArrayList<>();
        for (Appointment a : appointmentList) {
            customerList.addAll(a.getCustomerList());
        }
        assertEquals(0, customerList.size(), "Customer size should be 0");
        for (Customer c : customerList) {
            System.out.println("Found customer: " + c.toString());
        }
    }

    @Test
    @DisplayName("Delete all appointments test")
    @Order(3)
    public void deleteAllAppointmentsTest() {
        System.out.println("Delete test");
        List<Appointment> appointmentList = aDao.findAll();
        for (Appointment a : appointmentList) {
            aDao.deleteById(a.getId());
        }
        assertEquals(0, aDao.findAll().size(), "Appointment size should be 0");
    }

    @Test
    @DisplayName("Delete all customers test")
    @Order(4)
    public void deleteAllCustomersTest() {
        System.out.println("Delete test");
        List<Customer> customerList = cDao.findAll();
        for (Customer c : customerList) {
            cDao.deleteById(c.getId());
        }
        assertEquals(0, cDao.findAll().size(), "Customer size should be 0");
    }

}
