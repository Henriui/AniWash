package aniwash.entity;

import aniwash.dao.AnimalDao;
import aniwash.dao.AppointmentDao;
import aniwash.dao.IAnimalDao;
import aniwash.dao.IAppointmentDao;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("AppointmentAnimalDAO: CRUD testings")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppointmentAnimalTestDbTest {
    private final IAppointmentDao aDao = new AppointmentDao();
    private final IAnimalDao anDao = new AnimalDao();

    private Appointment appointment = new Appointment(ZonedDateTime.parse("2021-12-03T10:15:30+02:00"), ZonedDateTime.parse("2021-12-03T11:15:30+02:00"), "Elmo koiran pesu");

    @BeforeEach
    public void setUp() {
        appointment = new Appointment(ZonedDateTime.parse("2021-12-03T10:15:30+02:00"), ZonedDateTime.parse("2021-12-03T11:15:30+02:00"), "Elmo koiran pesu");
    }

    @AfterEach
    public void tearDown() {
        for (Appointment a : aDao.findAllAppointments()) {
            aDao.deleteByIdAppointment(a.getId());
        }
    }

    @Test
    @DisplayName("Create multiple appointments and animals test")
    @Order(1)
    public void testCreateMultipleAppointmentsAndAnimals() {
        for (int i = 1; i < 4; i++) {
            Appointment a = new Appointment(ZonedDateTime.parse("2021-0" + i + "-03T10:15:30+02:00"), ZonedDateTime.parse("2021-0" + i + "-03T11:15:30+02:00"), "Elmo koiran pesu" + i);
            Animal an = new Animal("Milla" + i, "Kissa", "Miaw", i, "Vilkas, mutta kiltti");
            aDao.addAppointment(a);
            anDao.addAnimal(an);
            a.addAnimal(an);
        }
        List<Animal> animals = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Animal an = new Animal("Elmo" + i, "Koira", "Labradori", i, "Rauhallinen ja kiltti");
            animals.add(an);
        }
        for (Animal an : animals) {
            Appointment a = aDao.findByStartDateAppointment(ZonedDateTime.parse("2021-01-03T10:15:30+02:00"));
            anDao.addAnimal(an);
            a.addAnimal(an);
        }
        assertEquals(3, aDao.findAllAppointments().size(), "Appointment list size count should be 3");
        assertEquals(4, aDao.findByStartDateAppointment(ZonedDateTime.parse("2021-01-03T10:15:30+02:00")).getAnimalList().size(), "Appointment animal list size count should be 4");
        assertEquals(6, anDao.findAllAnimal().size(), "Animal list size count should be 6");
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
        assertEquals(6, animalList.size(), "Animal list size count should be 6");
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
        List<Animal> animalList = anDao.findAllAnimal();
        for (Animal an : animalList) {
            anDao.deleteByIdAnimal(an.getId());
        }
        assertEquals(0, anDao.findAllAnimal().size(), "Animal list size count should be 0");
    }
}
