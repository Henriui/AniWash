package aniwash.entity;

import aniwash.dao.AppointmentDao;
import aniwash.dao.IAppointmentDao;
import aniwash.dao.IProductDao;
import aniwash.dao.ProductDao;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("AppointmentProductDAO: CRUD testings")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppointmentProductDbTest {
    private final IAppointmentDao aDao = new AppointmentDao();
    private final IProductDao pDao = new ProductDao();

    private final ZonedDateTime startDate = ZonedDateTime.parse("2021-12-03T10:15:30+02:00");
    private final ZonedDateTime endDate = ZonedDateTime.parse("2021-12-03T11:15:30+02:00");

    private Appointment appointment = new Appointment(startDate, endDate, "Elmo koiran pesu");
    private Product product = new Product("Iso pesu", "Ison eläimen pesu", 50, "basic");

    @BeforeEach
    public void setUp() {
        appointment = new Appointment(startDate, endDate, "Elmo koiran pesu");
        product = new Product("Iso pesu", "Ison eläimen pesu", 50, "basic");
    }

    @AfterEach
    public void tearDown() {
        for (Appointment a : aDao.findAllAppointment()) {
            aDao.deleteByIdAppointment(a.getId());
        }

        for (Product p : pDao.findAllProduct()) {
            pDao.deleteByIdProduct(p.getId());
        }
    }

    @Test
    @DisplayName("Create multiple appointments and products")
    @Order(1)
    public void testCreateMultipleAppointmentsAndProducts() {
        for (int i = 1; i < 10; i++) {
            Appointment a = new Appointment(ZonedDateTime.parse("2021-0" + i + "-03T10:15:30+02:00"), ZonedDateTime.parse("2021-0" + i + "-03T11:15:30+02:00"), "Elmo koiran pesu" + i);
            Product p = new Product("Iso pesu" + i, "Ison eläimen pesu" + i, 50 + i, "basic");

            aDao.addAppointment(a);
            pDao.addProduct(p);
            a.addProduct(p);
        }

        List<Product> products = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            Product p = new Product("Pieni pesu" + i, "Pienen eläimen pesu" + i, 30 + i, "basic");
            products.add(p);
        }

        for (Product p : products) {
            Appointment a = aDao.findByStartDateAppointment(ZonedDateTime.parse("2021-01-03T10:15:30+02:00"));
            pDao.addProduct(p);
            a.addProduct(p);
        }

        assertEquals(9, aDao.findAllAppointment().size(), "findAllAppointments(): Wrong amount of appointments. Should be 9.");
        assertEquals(10, aDao.findByStartDateAppointment(ZonedDateTime.parse("2021-01-03T10:15:30+02:00")).findAllProducts().size(), "findAllProducts(): Wrong amount of products. Should be 10.");
        assertEquals(1, aDao.findByStartDateAppointment(ZonedDateTime.parse("2021-02-03T10:15:30+02:00")).findAllProducts().size(), "findAllProducts(): Wrong amount of products. Should be 1.");
        assertEquals(18, pDao.findAllProduct().size(), "findAllProducts(): Wrong amount of products. Should be 19.");
    }
}
