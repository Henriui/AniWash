package aniwash.entity;

import aniwash.dao.AnimalDao;
import aniwash.dao.AppointmentDao;
import aniwash.dao.IAnimalDao;
import aniwash.dao.IAppointmentDao;
import aniwash.datastorage.DatabaseConnector;
import aniwash.entity.localization.LocalizedAppointment;
import aniwash.entity.localization.LocalizedId;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("AppointmentAnimalDAO: CRUD testings")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppointmentAnimalTestDbTest {

    private static IAppointmentDao aDao;
    private static IAnimalDao anDao;

    private Appointment appointment = new Appointment(ZonedDateTime.parse("2021-12-03T10:15:30+02:00"), ZonedDateTime.parse("2021-12-03T11:15:30+02:00"));

    @BeforeAll
    public static void initAll() {
        DatabaseConnector.openDbConnection("com.aniwash.test");
        aDao = new AppointmentDao();
        anDao = new AnimalDao();
    }

    @BeforeEach
    public void setUp() {
        appointment = new Appointment(ZonedDateTime.parse("2021-12-03T10:15:30+02:00"), ZonedDateTime.parse("2021-12-03T11:15:30+02:00"));
        LocalizedAppointment localAppointment = new LocalizedAppointment(appointment, "Elmo koiran pesu");
        localAppointment.setId(new LocalizedId("en"));
        appointment.getLocalizations().put("en", localAppointment);
    }

    @AfterEach
    public void tearDown() {
        for (Appointment a : aDao.findAllAppointments()) {
            aDao.deleteByIdAppointment(a.getId());
        }
    }

/*
    @AfterAll
    public static void tearDownAll() {
        DatabaseConnector.closeDbConnection();
    }
*/

    @Test
    @DisplayName("Create multiple appointments and animals test")
    @Order(1)
    public void testCreateMultipleAppointmentsAndAnimals() {
        for (int i = 1; i < 4; i++) {
            Appointment a = new Appointment(ZonedDateTime.parse("2021-0" + i + "-03T10:15:30+02:00"), ZonedDateTime.parse("2021-0" + i + "-03T11:15:30+02:00"));
            LocalizedAppointment localAppointment = new LocalizedAppointment(a, "Elmo koiran pesu" + i);
            localAppointment.setId(new LocalizedId("en"));
            a.getLocalizations().put("en", localAppointment);
            aDao.addAppointment(a);
            Animal an = new Animal("Milla" + i, "Kissa", "Miaw", "Vilkas, mutta kiltti");
            a.addAnimal(an);
            anDao.addAnimal(an);
        }
        List<Animal> animals = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Animal an = new Animal("Elmo" + i, "Koira", "Labradori", "Rauhallinen ja kiltti");
            animals.add(an);
        }
        for (Animal an : animals) {
            Appointment a = aDao.findNewestAppointment();
            a.addAnimal(an);
            anDao.addAnimal(an);
        }

        assertEquals(3, aDao.findAllAppointments().size(), "Appointment list size count should be 3");
        assertEquals(4, aDao.findNewestAppointment().getAnimalList().size(), "Appointment animal list size count should be 4");
        assertEquals(6, anDao.findAllAnimals().size(), "Animal list size count should be 6");
    }

    @Test
    @DisplayName("Find all animals from appointment test")
    @Order(2)
    public void findTest() {
        System.out.println("Find test");
        List<Appointment> appointmentList = aDao.findAllAppointments();
        List<Animal> animalList = new ArrayList<>();
        for (Appointment a : appointmentList) {
            animalList.addAll(a.getAnimalList());
        }
        assertEquals(0, animalList.size(), "Animal list size count should be 0");
        for (Animal an : animalList) {
            System.out.println("Found animal: " + an.toString());
        }
    }

    @Test
    @DisplayName("Delete all appointments test")
    @Order(3)
    public void deleteAllAppointmentsTest() {
        System.out.println("Delete all appointments test");
        List<Appointment> appointmentList = aDao.findAllAppointments();
        for (Appointment a : appointmentList) {
            aDao.deleteByIdAppointment(a.getId());
        }
        assertEquals(0, aDao.findAllAppointments().size(), "Appointment list size count should be 0");
    }

    @Test
    @DisplayName("Delete all animals test")
    @Order(4)
    public void deleteAllAnimalsTest() {
        System.out.println("Delete all animals test");
        List<Animal> animalList = anDao.findAllAnimals();
        for (Animal an : animalList) {
            anDao.deleteByIdAnimal(an.getId());
        }
        assertEquals(0, anDao.findAllAnimals().size(), "Animal list size count should be 0");
    }

}
