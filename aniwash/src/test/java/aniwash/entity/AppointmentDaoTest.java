package aniwash.entity;

import aniwash.dao.AppointmentDao;
import aniwash.dao.IAppointmentDao;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AppointmentDAO: CRUD testings")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppointmentDaoTest {

    private final IAppointmentDao appointmentDao = new AppointmentDao();
    private final String date = "2021-12-03T10:15:30+02:00";
    private final String description = "Koiran pesu";

    private Appointment appointment = new Appointment(ZonedDateTime.parse(date), description);

    @BeforeEach
    public void setUp() {
        appointment = new Appointment(ZonedDateTime.parse(date), description);
    }

    @AfterEach
    public void tearDown() {
        appointmentDao.deleteByIdAppointment(appointment.getId());
    }


    @Test
    @Order(1)
    @DisplayName("Add new appointment")
    public void testAddAppointment() {
        assertTrue(appointmentDao.addAppointment(appointment), "addAppointment(): Add new appointment failed.");
        assertNotNull(appointment = appointmentDao.findByIdAppointment(appointment.getId()), "addAppointment(): Cant find added appointment.");
        assertEquals(appointment.getDate().toString(), date, "addAppointment(): Date of added appointment does not match.");
        assertEquals(appointment.getDescription(), description, "addAppointment(): Description of added appointment does not match.");
    }

    @Test
    @Order(2)
    @DisplayName("Add same appointment twice")
    public void testAddSameAppointment() {
        assertTrue(appointmentDao.addAppointment(appointment), "addAppointment(): Add new appointment failed.");
        assertFalse(appointmentDao.addAppointment(appointment), "addAppointment(): Same appointment can be added twice.");
    }

    @Test
    @Order(3)
    @DisplayName("Fetch added appointment by id")
    public void testFindAppointment() {
        assertTrue(appointmentDao.addAppointment(appointment), "addAppointment(): Add new appointment failed.");
        appointment = appointmentDao.findByIdAppointment(appointment.getId());
        assertNotNull(appointment, "findByIdAppointment(): Cant find added appointment.");
        assertEquals(appointment.getDate(), ZonedDateTime.parse(date), "findByIdAppointment(): Date of found appointment does not match added.");
        assertEquals(appointment.getDescription(), description, "findByIdAppointment(): Description of found appointment does not match added.");
    }

    @Test
    @Order(4)
    @DisplayName("Fetch added appointment by date")
    public void testFindByDateAppointment() {
        assertTrue(appointmentDao.addAppointment(appointment), "addAppointment(): Add new appointment failed.");
        appointment = appointmentDao.findByDateAppointment(ZonedDateTime.parse(date));
        assertNotNull(appointment, "findByDateAppointment(): Cant find added appointment.");
        assertEquals(appointment.getDate(), ZonedDateTime.parse(date), "findByDateAppointment(): Date of found appointment does not match added.");
        assertEquals(appointment.getDescription(), description, "findByDateAppointment(): Description of found appointment does not match added.");
    }

    @Test
    @Order(5)
    @DisplayName("Delete added appointment by id")
    public void testDeleteByIdAppointment() {
        assertTrue(appointmentDao.addAppointment(appointment), "addAppointment(): Add new appointment failed.");
        assertTrue(appointmentDao.deleteByIdAppointment(appointment.getId()), "deleteByIdAppointment(): Delete added appointment failed.");
        assertNull(appointmentDao.findByIdAppointment(appointment.getId()), "deleteByIdAppointment(): Deleted appointment can still be found.");
    }

    @Test
    @Order(6)
    @DisplayName("Update added appointment")
    public void testUpdateAppointment() {
        assertTrue(appointmentDao.addAppointment(appointment), "addAppointment(): Add new appointment failed.");
        appointment = appointmentDao.findByIdAppointment(appointment.getId());
        appointment.setDate(ZonedDateTime.parse("2011-11-09T10:15:30+02:00"));
        appointment.setDescription("Koiran pesu ja leikkaus");
        assertTrue(appointmentDao.updateAppointment(appointment), "updateAppointment(): Update added appointment failed.");
        appointment = appointmentDao.findByIdAppointment(appointment.getId());
        assertEquals(appointment.getDescription(), "Koiran pesu ja leikkaus", "updateAppointment(): Description of updated appointment does not match.");
        assertEquals(appointment.getDate(), ZonedDateTime.parse("2011-11-09T10:15:30+02:00"), "updateAppointment(): Date of updated appointment does not match.");
    }

    @Test
    @Order(7)
    @DisplayName("Update non-existing appointment should retun false")
    public void testUpdateNonExistingAppointment() {
        assertFalse(appointmentDao.updateAppointment(appointment), "updateAppointment(): Update non-existing appointment should return false.");
    }

    @Test
    @Order(8)
    @DisplayName("Delete non-existing appointment should return false")
    public void testDeleteNonExistingAppointment() {
        assertFalse(appointmentDao.deleteByIdAppointment(appointment.getId()), "deleteByIdAppointment(): Delete non-existing appointment should return false.");
    }
}
