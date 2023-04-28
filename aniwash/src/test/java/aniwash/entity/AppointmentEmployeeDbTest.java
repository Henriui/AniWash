package aniwash.entity;

import aniwash.dao.AppointmentDao;
import aniwash.dao.EmployeeDao;
import aniwash.dao.IAppointmentDao;
import aniwash.dao.IEmployeeDao;
import aniwash.datastorage.DatabaseConnector;
import aniwash.entity.localization.LocalizedAppointment;
import aniwash.entity.localization.LocalizedId;
import aniwash.enums.UserType;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("AppointmentEmployeeDAO: CRUD testings")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppointmentEmployeeDbTest {

    private static IAppointmentDao aDao;
    private static IEmployeeDao eDao;

    @BeforeAll
    public static void initAll() {
        DatabaseConnector.openDbConnection("com.aniwash.test");
        aDao = new AppointmentDao();
        eDao = new EmployeeDao();
    }

    @AfterEach
    public void tearDown() {
        for (Appointment a : aDao.findAll()) {
            aDao.deleteById(a.getId());
        }
    }

    @Test
    @DisplayName("Create multiple appointments and employees then remove appointments and employees")
    @Order(1)
    public void testCreateMultipleAppointmentsAndEmployees() {
        for (int i = 1; i < 4; i++) {
            Appointment a = new Appointment(ZonedDateTime.parse("2021-0" + i + "-03T10:15:30+02:00"), ZonedDateTime.parse("2021-0" + i + "-03T11:15:30+02:00"));
            LocalizedAppointment localAppointment = new LocalizedAppointment(a, "Elmo koiran pesu" + i);
            localAppointment.setId(new LocalizedId("en"));
            a.getLocalizations().put("en", localAppointment);
            Employee e = new Employee("kimmoka" + i, "12345" + i, "Kimmo Kala", "kimmo.kala" + i + "@gmail.com", "Työntekijä", UserType.EMPLOYEE);
            aDao.add(a);
            eDao.add(e);
        }
        List<Employee> employees = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Employee e = new Employee("tainatui" + i, "12345" + i, "Taina Tuima", "taina.tuima" + i + "@gmail.com", "Työntekijä", UserType.EMPLOYEE);
            employees.add(e);
        }
        for (Employee e : employees) {
            Appointment a = aDao.findNewest();
            eDao.add(e);
        }
        assertEquals(3, aDao.findAll().size(), "Appointment list size should be 3");
        assertEquals(6, eDao.findAll().size(), "Employee list size should be 6");
    }

    @Test
    @DisplayName("Delete all appointments and employees")
    @Order(3)
    public void deleteAllAppointments() {
        System.out.println("Delete all appointments test");
        List<Appointment> appointmentList = aDao.findAll();
        for (Appointment a : appointmentList) {
            aDao.deleteById(a.getId());
        }
        assertEquals(0, aDao.findAll().size(), "Appointment list size should be 0");
    }

    @Test
    @DisplayName("Delete all employees")
    @Order(4)
    public void deleteAllEmployees() {
        System.out.println("Delete all employees test");
        List<Employee> employeeList = eDao.findAll();
        for (Employee e : employeeList) {
            eDao.deleteById(e.getId());
        }
        assertEquals(0, eDao.findAll().size(), "Employee list size should be 0");
    }

}
