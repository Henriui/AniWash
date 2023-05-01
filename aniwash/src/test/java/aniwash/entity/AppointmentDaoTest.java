package aniwash.entity;

import aniwash.dao.AppointmentDao;
import aniwash.dao.IAppointmentDao;
import aniwash.datastorage.DatabaseConnector;
import aniwash.entity.localization.LocalizedAppointment;
import aniwash.entity.localization.LocalizedId;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AppointmentDAO: CRUD testings")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppointmentDaoTest {

    private static IAppointmentDao appointmentDao;
    private final String startDate = "2021-12-03T10:15:30+02:00";
    private final String endDate = "2021-12-03T11:15:30+02:00";
    private final String description = "Koiran pesu";

    private Appointment appointment;

    @BeforeAll
    public static void initAll() {
        DatabaseConnector.openDbConnection("com.aniwash.test");
        appointmentDao = new AppointmentDao();
    }

    @BeforeEach
    public void setUp() {
        appointment = new Appointment(ZonedDateTime.parse(startDate), ZonedDateTime.parse(endDate));
        LocalizedAppointment localAppointment = new LocalizedAppointment(appointment, description);
        localAppointment.setId(new LocalizedId("en"));
        appointment.getLocalizations().put("en", localAppointment);
    }

    @AfterEach
    public void tearDown() {
        for (Appointment a : appointmentDao.findAll()) {
            appointmentDao.deleteById(a.getId());
        }
    }

    @Test
    @Order(1)
    @DisplayName("Add new appointment")
    public void testAddAppointment() {
        assertTrue(appointmentDao.add(appointment), "addAppointment(): Add new appointment failed.");
        assertNotNull(appointment = appointmentDao.findById(appointment.getId()), "addAppointment(): Cant find added appointment.");
        assertEquals(appointment.getStartDate().toString(), startDate, "addAppointment(): Date of added appointment does not match.");
        assertEquals(appointment.getDescription("en"), description, "addAppointment(): Description of added appointment does not match.");
    }

    @Test
    @Order(2)
    @DisplayName("Add same appointment twice")
    public void testAddSameAppointment() {
        assertTrue(appointmentDao.add(appointment), "addAppointment(): Add new appointment failed.");
        assertFalse(appointmentDao.add(appointment), "addAppointment(): Same appointment can be added twice.");
    }

    @Test
    @Order(3)
    @DisplayName("Fetch added appointment by id")
    public void testFindAppointment() {
        assertTrue(appointmentDao.add(appointment), "addAppointment(): Add new appointment failed.");
        appointment = appointmentDao.findById(appointment.getId());
        assertNotNull(appointment, "findByIdAppointment(): Cant find added appointment.");
        assertEquals(appointment.getStartDate(), ZonedDateTime.parse(startDate), "findByIdAppointment(): Date of found appointment does not match added.");
        assertEquals(appointment.getDescription("en"), description, "findByIdAppointment(): Description of found appointment does not match added.");
    }

    @Test
    @Order(4)
    @DisplayName("Fetch added appointment by start date")
    public void testFindByStartDateAppointment() {
        assertTrue(appointmentDao.add(appointment), "addAppointment(): Add new appointment failed.");
        appointment = appointmentDao.findByStartDate(ZonedDateTime.parse(startDate));
        assertNotNull(appointment, "findByDateAppointment(): Cant find added appointment.");
        assertEquals(appointment.getStartDate(), ZonedDateTime.parse(startDate), "findByDateAppointment(): Date of found appointment does not match added.");
        assertEquals(appointment.getDescription("en"), description, "findByDateAppointment(): Description of found appointment does not match added.");
    }

    @Test
    @Order(5)
    @DisplayName("Delete added appointment by id")
    public void testDeleteByIdAppointment() {
        assertTrue(appointmentDao.add(appointment), "addAppointment(): Add new appointment failed.");
        assertTrue(appointmentDao.deleteById(appointment.getId()), "deleteByIdAppointment(): Delete added appointment failed.");
        assertNull(appointmentDao.findById(appointment.getId()), "deleteByIdAppointment(): Deleted appointment can still be found.");
    }

    @Test
    @Order(6)
    @DisplayName("Update added appointment")
    public void testUpdateAppointment() {
        assertTrue(appointmentDao.add(appointment), "addAppointment(): Add new appointment failed.");
        appointment = appointmentDao.findById(appointment.getId());
        appointment.setStartDate(ZonedDateTime.parse("2011-11-09T10:15:30+02:00"));
        appointment.getLocalizations().get("en").setDescription("Koiran pesu ja leikkaus");
        assertTrue(appointmentDao.update(appointment), "updateAppointment(): Update added appointment failed.");
        appointment = appointmentDao.findById(appointment.getId());
        assertEquals(appointment.getDescription("en"), "Koiran pesu ja leikkaus", "updateAppointment(): Description of updated appointment does not match.");
        assertEquals(appointment.getStartDate(), ZonedDateTime.parse("2011-11-09T10:15:30+02:00"), "updateAppointment(): Date of updated appointment does not match.");
    }

    @Test
    @Order(7)
    @DisplayName("Update non-existing appointment should retun false")
    public void testUpdateNonExistingAppointment() {
        assertFalse(appointmentDao.update(appointment), "updateAppointment(): Update non-existing appointment should return false.");
    }

    @Test
    @Order(8)
    @DisplayName("Delete non-existing appointment should return false")
    public void testDeleteNonExistingAppointment() {
        assertFalse(appointmentDao.deleteById(appointment.getId()), "deleteByIdAppointment(): Delete non-existing appointment should return false.");
    }

    @Test
    @Order(9)
    @DisplayName("Find non-existing appointment should return null")
    public void testFindNonExistingAppointment() {
        assertNull(appointmentDao.findById(appointment.getId()), "findByIdAppointment(): Find non-existing appointment should return null.");
    }

    @Test
    @Order(10)
    @DisplayName("Find non-existing appointment by start date should return null")
    public void testFindNonExistingAppointmentByStartDate() {
        assertNull(appointmentDao.findByStartDate(ZonedDateTime.parse(startDate)), "findByStartDateAppointment(): Find non-existing appointment should return null.");
    }

    @Test
    @Order(11)
    @DisplayName("Set appointment end date and fetch it")
    public void testSetEndDate() {
        assertTrue(appointmentDao.add(appointment), "addAppointment(): Add new appointment failed.");
        appointment = appointmentDao.findById(appointment.getId());
        appointment.setEndDate(ZonedDateTime.parse("2011-11-09T10:15:30+02:00"));
        assertTrue(appointmentDao.update(appointment), "updateAppointment(): Update added appointment failed.");
        appointment = appointmentDao.findById(appointment.getId());
        assertEquals(appointment.getEndDate(), ZonedDateTime.parse("2011-11-09T10:15:30+02:00"), "updateAppointment(): End date of updated appointment does not match.");
    }

    @Test
    @Order(12)
    @DisplayName("Set appointment as deleted and fetch it")
    public void testSetDeleted() {
        assertTrue(appointmentDao.add(appointment), "addAppointment(): Add new appointment failed.");
        appointment = appointmentDao.findById(appointment.getId());
        appointment.setDeleted();
        assertEquals(1, appointment.isDeleted(), "updateAppointment(): Deleted status of updated appointment does not match.");
        assertTrue(appointmentDao.update(appointment), "updateAppointment(): Update added appointment failed.");
        appointment = appointmentDao.findById(appointment.getId());
        assertNull(appointment, "updateAppointment(): Deleted appointment can still be found.");
    }

}
