package aniwash.entity;

import aniwash.dao.AppointmentDao;
import aniwash.dao.IAppointmentDao;
import aniwash.dao.IProductDao;
import aniwash.dao.ProductDao;
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

    @BeforeEach
    public void setUp() {
        appointment = new Appointment(startDate, endDate, "Elmo koiran pesu");
    }

    @AfterEach
    public void tearDown() {
        for (Appointment a : aDao.findAllAppointments()) {
            aDao.deleteByIdAppointment(a.getId());
        }

        for (Product p : pDao.findAllProduct()) {
            pDao.deleteByIdProduct(p.getId());
        }
    }

    @Test
    @DisplayName("Create multiple appointments and products test")
    @Order(1)
    public void testCreateMultipleAppointmentsAndProducts() {
        for (int i = 1; i < 4; i++) {
            Appointment a = new Appointment(ZonedDateTime.parse("2021-0" + i + "-03T10:15:30+02:00"), ZonedDateTime.parse("2021-0" + i + "-03T11:15:30+02:00"), "Elmo koiran pesu" + i);
            Product p = new Product("Iso pesu" + i, "Ison eläimen pesu" + i, 50 + i, "basic");

            aDao.addAppointment(a);
            pDao.addProduct(p);
            a.addProduct(p);
        }

        List<Product> products = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Product p = new Product("Pieni pesu" + i, "Pienen eläimen pesu" + i, 30 + i, "basic");
            products.add(p);
        }

        for (Product p : products) {
            Appointment a = aDao.findByStartDateAppointment(ZonedDateTime.parse("2021-01-03T10:15:30+02:00"));
            pDao.addProduct(p);
            a.addProduct(p);
        }

        assertEquals(3, aDao.findAllAppointments().size(), "findAllAppointments(): Wrong amount of appointments. Should be 3.");
        assertEquals(4, aDao.findByStartDateAppointment(ZonedDateTime.parse("2021-01-03T10:15:30+02:00")).getProductList().size(), "findAllProducts(): Wrong amount of products. Should be 4.");
        assertEquals(1, aDao.findByStartDateAppointment(ZonedDateTime.parse("2021-02-03T10:15:30+02:00")).getProductList().size(), "findAllProducts(): Wrong amount of products. Should be 1.");
        assertEquals(6, pDao.findAllProduct().size(), "findAllProducts(): Wrong amount of products. Should be 6.");

    }

    @Test
    @DisplayName("Find all products from appointments")
    @Order(2)
    public void findAllProductsFromAppointment() {
        System.out.println("Find test");
        List<Appointment> appointmentsList = aDao.findAllAppointments();
        List<Product> productsList = new ArrayList<>();
        for (Appointment a : appointmentsList) {
            productsList.addAll(a.getProducts());
        }
        System.out.println(productsList.size());
        for (Product p : productsList) {
            System.out.println("Found product: " + p.toString());
        }
    }

    @Test
    @DisplayName("Delete all Appointments")
    @Order(3)
    public void deleteAllAppointments() {
        System.out.println("Delete all appointments test");
        List<Appointment> appointmentsList = aDao.findAllAppointments();
        for (Appointment a : appointmentsList) {
            aDao.deleteByIdAppointment(a.getId());
        }
        appointmentsList = aDao.findAllAppointments();
        assertEquals(0, appointmentsList.size(), "findAllAppointments(): Wrong amount of appointments. Should be 0.");
    }

    @Test
    @DisplayName("Delete all Products")
    @Order(4)
    public void deleteAllProducts() {
        System.out.println("Delete all products test");
        List<Product> productsList = pDao.findAllProduct();
        for (Product p : productsList) {
            pDao.deleteByIdProduct(p.getId());
        }
        productsList = pDao.findAllProduct();
        assertEquals(0, productsList.size(), "findAllProducts(): Wrong amount of products. Should be 0.");
    }
}
