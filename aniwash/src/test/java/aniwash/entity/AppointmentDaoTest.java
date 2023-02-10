package aniwash.entity;

import aniwash.dao.AppointmentDao;
import aniwash.dao.IAppointmentDao;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AppointmentDAO: tietokantatoimintojen (CRUD) testaus")
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
    @DisplayName("Tässä kaikki testit yhdessä metodissa.")
    public void testDAOAppointment() {

    }

    @Test
    @Order(1)
    @DisplayName("Lisää uusi ajanvaraus")
    public void testAddAppointment() {
        assertTrue(appointmentDao.addAppointment(appointment), "addAppointment(): Uuden ajanvarauksen lisääminen ei onnistu.");
    }

    @Test
    @Order(2)
    @DisplayName("Samaa ajanvarausta ei voi lisätä uudestaan")
    public void testAddSameAppointment() {
        assertTrue(appointmentDao.addAppointment(appointment), "addAppointment(): Uuden ajanvarauksen lisääminen ei onnistu.");
        assertFalse(appointmentDao.addAppointment(appointment), "addAppointment(): Saman ajanvarauksen pystyy lisäämään kahteen kertaan.");
    }

    @Test
    @Order(3)
    @DisplayName("Etsitään lisätty ajanvaraus tietokannasta")
    public void testFindAppointment() {
        assertTrue(appointmentDao.addAppointment(appointment), "addAppointment(): Uuden ajanvarauksen lisääminen ei onnistu.");
        appointment = appointmentDao.findByIdAppointment(appointment.getId());
        assertNotNull(appointment, "findByIdAppointment(): Etsittyä ajanvarausta ei löydy.");
        assertEquals(appointment.getDate(), ZonedDateTime.parse(date), "findByIdAppointment(): Etsittyn ajanvarauksen päivä ei vastaa lisättyä.");
        assertEquals(appointment.getDescription(), description, "findByIdAppointment(): Etsittyn ajanvarauksen kuvaus ei vastaa lisättyä.");
    }

}
