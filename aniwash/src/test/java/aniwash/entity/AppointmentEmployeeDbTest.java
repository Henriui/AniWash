package aniwash.entity;

import aniwash.dao.AppointmentDao;
import aniwash.dao.EmployeeDao;
import aniwash.dao.IAppointmentDao;
import aniwash.dao.IEmployeeDao;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("AppointmentEmployeeDAO: CRUD testings")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class
AppointmentEmployeeDbTest {
    private final IAppointmentDao aDao = new AppointmentDao();
    private final IEmployeeDao eDao = new EmployeeDao();

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
    @DisplayName("Create multiple appointments and employees then remove appointments and employees")
    @Order(1)
    public void testCreateMultipleAppointmentsAndEmployees() {
        for (int i = 1; i < 4; i++) {
            Appointment a = new Appointment(ZonedDateTime.parse("2021-0" + i + "-03T10:15:30+02:00"), ZonedDateTime.parse("2021-0" + i + "-03T11:15:30+02:00"), "Elmo koiran pesu" + i);
            Employee e = new Employee("kimmoka" + i, "12345" + i, "Kimmo Kala", "kimmo.kala" + i + "@gmail.com", "Työntekijä", UserType.EMPLOYEE);
            aDao.addAppointment(a);
            eDao.addEmployee(e);
        }
        List<Employee> employees = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Employee e = new Employee("tainatui" + i, "12345" + i, "Taina Tuima", "taina.tuima" + i + "@gmail.com", "Työntekijä", UserType.EMPLOYEE);
            employees.add(e);
        }
        for (Employee e : employees) {
            Appointment a = aDao.findByStartDateAppointment(ZonedDateTime.parse("2021-01-03T10:15:30+02:00"));
            eDao.addEmployee(e);
        }
        assertEquals(3, aDao.findAllAppointments().size(), "Appointment list size should be 3");
        assertEquals(6, eDao.findAllEmployee().size(), "Employee list size should be 6");
        //assertEquals(4, aDao.findByStartDateAppointment(ZonedDateTime.parse("2021-01-03T10:15:30+02:00")).getEmployeeList().size(), "Appointment employee list size should be 4");
    }

/*
    @Test
    @DisplayName("Find all employees from appointment")
    @Order(2)
    public void findTest() {
        List<Appointment> appointmentList = aDao.findAllAppointments();
        List<Employee> employeeList = new ArrayList<>();
        for (Appointment a : appointmentList) {
            employeeList.addAll(a.getEmployees());
        }
        assertEquals(6, employeeList.size(), "Employee list size should be 6");
        for (Employee e : employeeList) {
            System.out.println("Found employee: " + e.toString());
        }
    }
*/

    @Test
    @DisplayName("Delete all appointments and employees")
    @Order(3)
    public void deleteAllAppointments() {
        System.out.println("Delete all appointments test");
        List<Appointment> appointmentList = aDao.findAllAppointments();
        for (Appointment a : appointmentList) {
            aDao.deleteByIdAppointment(a.getId());
        }
        assertEquals(0, aDao.findAllAppointments().size(), "Appointment list size should be 0");
    }

    @Test
    @DisplayName("Delete all employees")
    @Order(4)
    public void deleteAllEmployees() {
        System.out.println("Delete all employees test");
        List<Employee> employeeList = eDao.findAllEmployee();
        for (Employee e : employeeList) {
            eDao.deleteByIdEmployee(e.getId());
        }
        assertEquals(0, eDao.findAllEmployee().size(), "Employee list size should be 0");
    }
}
