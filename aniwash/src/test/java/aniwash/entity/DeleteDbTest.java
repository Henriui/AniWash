package aniwash.entity;

import aniwash.dao.*;
import aniwash.datastorage.DatabaseConnector;
import aniwash.entity.localization.LocalizedAppointment;
import aniwash.entity.localization.LocalizedId;
import aniwash.entity.localization.LocalizedProduct;
import aniwash.entity.testObjects.TestCustomer;
import aniwash.enums.UserType;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Entity delete from database test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeleteDbTest {

    private static IAnimalDao aDao;
    private static ICustomerDao cDao;
    private static IEmployeeDao eDao;

    private static IProductDao pDao;

    private static IAppointmentDao apDao;

    @BeforeAll
    public static void initAll() {
        DatabaseConnector.openDbConnection("com.aniwash.test");
        cDao = new CustomerDao();
        aDao = new AnimalDao();
        eDao = new EmployeeDao();
        pDao = new ProductDao();
        apDao = new AppointmentDao();
    }

/*
    @AfterAll
    public static void tearDownAll() {
        DatabaseConnector.closeDbConnection();
    }
*/

    @Test
    @DisplayName("Create multiple customers and animals test")
    @Order(1)
    public void listTest() {
        for (int i = 0; i < 20; i++) {
            Customer c = TestCustomer.generateTestCustomer();
            Animal a = new Animal("Uli" + i, "Dog", "Husky", "Uliseva rakki");
            cDao.add(c);
            aDao.add(a);
            c.addAnimal(a);
        }

        List<Animal> animals = new ArrayList<>();
        for (int i = 1; i < 20; i++) {
            Animal a = new Animal("Rohmu" + i, "Cat", "Meow", "Maukuva nakki");
            animals.add(a);
        }

        Customer c = cDao.findNewest();

        for (Animal a : animals) {
            aDao.add(a);
            c.addAnimal(a);
        }

        for (int i = 0; i < 10; i++) {
            Employee e = new Employee("John" + i, "password", "John" + i, "john" + i + "@gmail.com", "Employee", UserType.EMPLOYEE);
            eDao.add(e);
        }

        for (int i = 1; i < 9; i++) {
            Appointment ap = new Appointment(ZonedDateTime.parse("2021-0" + i + "-03T10:15:30+02:00"), ZonedDateTime.parse("2021-0" + i + "-03T11:15:30+02:00"));
            LocalizedAppointment localAppointment = new LocalizedAppointment(ap, "Elmo koiran pesu" + i);
            localAppointment.setId(new LocalizedId("en"));
            ap.getLocalizations().put("en", localAppointment);
            Product product = new Product("Iso pesu" + i, "Ison eläimen pesu" + i, 50 + i, "style1");
            LocalizedProduct localizedProduct = new LocalizedProduct(product, "Iso pesu", "Ison eläimen pesu");
            localizedProduct.setId(new LocalizedId("en"));
            product.getLocalizations().put("en", localizedProduct);

            ap.addProduct(product);
            ap.addCustomer(c);
            ap.addAnimal(c.getAnimalList().get(i));
            apDao.add(ap);
        }

        assertEquals(20, cDao.findAll().size(), "Customer list size is not 10");
        assertEquals(20, cDao.findNewest().getAnimalList().size(), "Customers animal list size is not 20");
        assertEquals(39, aDao.findAll().size(), "Animal list size is not 39");
        assertEquals(10, eDao.findAll().size(), "Employee list size is not 10");
        assertEquals(8, pDao.findAll().size(), "Product list size is not 10");
        assertEquals(8, apDao.findAll().size(), "Appointment list size is not 10");
    }

    @Test
    @Order(2)
    @DisplayName("Find all animals from customer test")
    public void findTest() {
        List<Customer> customerList = cDao.findAll();
        List<Animal> animalList = new ArrayList<>();
        for (Customer c : customerList) {
            animalList.addAll(c.getAnimals());
        }
        for (Animal a : animalList) {
            assertEquals(a, aDao.findById(a.getId()), "Animal not found");
        }
    }

    @Test
    @Order(3)
    @DisplayName("Find all appointments from customer test")
    public void findAppointmentTest() {
        List<Customer> customerList = cDao.findAll();
        List<Appointment> appointmentList = new ArrayList<>();
        for (Customer c : customerList) {
            appointmentList.addAll(c.getAppointments());
        }
        for (Appointment ap : appointmentList) {
            assertEquals(ap, apDao.findById(ap.getId()), "Appointment not found");
        }
        assertEquals(8, appointmentList.size(), "Appointment list size is not 10");
    }

    @Test
    @Order(4)
    @DisplayName("Delete all appointments test")
    public void deleteAllAppointmentTest() {

        List<Appointment> appointmentList = apDao.findAll();
        for (Appointment ap : appointmentList) {
            apDao.deleteById(ap.getId());
        }
        appointmentList = apDao.findAll();
        assertEquals(0, appointmentList.size(), "Appointment list size is not 0");
    }

    @Test
    @Order(5)
    @DisplayName("Delete all customers test")
    public void deleteAllCustomerTest() {

        List<Customer> customerList = cDao.findAll();
        for (Customer c : customerList) {

            List<Animal> animalList = c.getAnimalList();
            for (Animal a : animalList) {
                c.removeAnimal(a);
            }
            cDao.deleteById(c.getId());
        }
        assertEquals(0, cDao.findAll().size(), "Customer list size is not 0");
    }

    @Test
    @Order(6)
    @DisplayName("Delete all animals test")
    public void deleteAllAnimalTest() {

        List<Animal> animalList = aDao.findAll();
        for (Animal a : animalList) {
            aDao.deleteById(a.getId());
        }
        animalList = aDao.findAll();
        assertEquals(0, animalList.size(), "Animal list size is not 0");
    }

    @Test
    @Order(7)
    @DisplayName("Delete all employees test")
    public void deleteAllEmployeeTest() {

        List<Employee> employeeList = eDao.findAll();
        for (Employee e : employeeList) {
            eDao.deleteById(e.getId());
        }
        employeeList = eDao.findAll();
        assertEquals(0, employeeList.size(), "Employee list size is not 0");
    }

    @Test
    @Order(8)
    @DisplayName("Delete all products test")
    public void deleteAllProductTest() {

        List<Product> productList = pDao.findAll();
        for (Product p : productList) {
            pDao.deleteById(p.getId());
        }
        productList = pDao.findAll();
        assertEquals(0, productList.size(), "Product list size is not 0");
    }

}
