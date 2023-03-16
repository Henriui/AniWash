package aniwash.entity;

import aniwash.dao.AppointmentDao;
import aniwash.dao.CustomerDao;
import aniwash.dao.IAppointmentDao;
import aniwash.dao.ICustomerDao;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("AppointmentCustomerDAO: CRUD testings")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppointmentCustomerDbTest {
    private final IAppointmentDao aDao = new AppointmentDao();
    private final ICustomerDao cDao = new CustomerDao();

    private final ZonedDateTime startDate = ZonedDateTime.parse("2021-12-03T10:15:30+02:00");
    private final ZonedDateTime endDate = ZonedDateTime.parse("2021-12-03T11:15:30+02:00");

    private Appointment appointment = new Appointment(startDate, endDate, "Elmo koiran pesu");

    @BeforeEach
    public void setUp() {
        appointment = new Appointment(startDate, endDate, "Elmo koiran pesu");
    }

    @AfterEach
    public void tearDown() {
        for (Appointment a : aDao.findAllAppointments()) {
            aDao.deleteByIdAppointment(a.getId());
        }
    }

    @Test
    @DisplayName("Create multiple appointments and customers test")
    @Order(1)
    public void testCreateMultipleAppointmentsAndProducts() {
        System.out.println("Cutomers: " + cDao.findAllCustomer().size());
        for (int i = 1; i < 4; i++) {
            Appointment a = new Appointment(ZonedDateTime.parse("2021-0" + i + "-03T10:15:30+02:00"), ZonedDateTime.parse("2021-0" + i + "-03T11:15:30+02:00"), "Elmo koiran pesu" + i);
            Customer c = new Customer("Elmo Pohjonen" + i, "044355667" + i, "elmo.pohjonen" + i + "@gmail.com", "Kalakuja " + i, "0032" + i);
            aDao.addAppointment(a);
            cDao.addCustomer(c);
            a.addCustomer(c);
        }
        List<Customer> customers = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Customer c = new Customer("Tatu Pohjonen" + i, "044355662" + i, "tatu.pohjonen" + i + "@gmail.com", "Tuulikuja " + i, "0031" + i);
            customers.add(c);
        }
        for (Customer c : customers) {
            Appointment a = aDao.findByStartDateAppointment(ZonedDateTime.parse("2021-01-03T10:15:30+02:00"));
            cDao.addCustomer(c);
            a.addCustomer(c);
        }
        assertEquals(3, aDao.findAllAppointments().size(), "Appointment size should be 3");
        assertEquals(6, cDao.findAllCustomer().size(), "Customer size should be 6");
        assertEquals(4, aDao.findByStartDateAppointment(ZonedDateTime.parse("2021-01-03T10:15:30+02:00")).getCustomerList().size(), "Appointment size should be 4");
    }

    @Test
    @DisplayName("Find all customers from appointment test")
    @Order(2)
    public void findTest() {
        System.out.println("Find test");
        List<Appointment> appointmentList = aDao.findAllAppointments();
        List<Customer> customerList = new ArrayList<>();
        for (Appointment a : appointmentList) {
            customerList.addAll(a.getCustomerList());
        }
        assertEquals(6, customerList.size(), "Customer size should be 6");
        for (Customer c : customerList) {
            System.out.println("Found customer: " + c.toString());
        }
    }

    @Test
    @DisplayName("Delete all appointments test")
    @Order(3)
    public void deleteAllAppointmentsTest() {
        System.out.println("Delete test");
        List<Appointment> appointmentList = aDao.findAllAppointments();
        for (Appointment a : appointmentList) {
            aDao.deleteByIdAppointment(a.getId());
        }
        assertEquals(0, aDao.findAllAppointments().size(), "Appointment size should be 0");
    }

    @Test
    @DisplayName("Delete all customers test")
    @Order(4)
    public void deleteAllCustomersTest() {
        System.out.println("Delete test");
        List<Customer> customerList = cDao.findAllCustomer();
        for (Customer c : customerList) {
            cDao.deleteByIdCustomer(c.getId());
        }
        assertEquals(0, cDao.findAllCustomer().size(), "Customer size should be 0");
    }

}
