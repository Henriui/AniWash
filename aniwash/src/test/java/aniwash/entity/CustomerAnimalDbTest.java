package aniwash.entity;

import aniwash.dao.*;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Customer and Animal class CRUD testings")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerAnimalDbTest {
    private final IAnimalDao aDao = new AnimalDao();
    private final ICustomerDao cDao = new CustomerDao();

    private final IEmployeeDao eDao = new EmployeeDao();

    private Customer customer = new Customer("John", "+358 - 0", "rammus" + "@gmail.com");

    @BeforeEach
    public void beforeEach() {
        customer = new Customer("John", "+358 - 0", "rammus" + "@gmail.com");
        customer.setAddress("Kuusikko");
        customer.setPostalCode("12345");
    }

    @AfterEach
    public void afterEach() {
        cDao.deleteByIdCustomer(customer.getId());
    }

    @Test
    @DisplayName("Create multiple customers and animals test")
    @Order(1)
    public void rammusTest() {
        System.out.println("Rammus is the best");
        for (int i = 0; i < 10; i++) {
            Customer c = new Customer("John" + i, "+358 - 0" + i, "rammus" + i + "@gmail.com");
            Animal a = new Animal("Uli" + i, "Dog", "Husky", 1 + i, "Uliseva rakki");
            cDao.addCustomer(c);
            aDao.addAnimal(a);
            c.addAnimal(a);
        }

        List<Animal> animals = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Animal a = new Animal("Rohmu" + i, "Cat", "Meow", 1 + i, "Maukuva nakki");
            animals.add(a);
        }

        for (Animal a : animals) {
            Customer c = cDao.findByNameCustomer("John1");
            aDao.addAnimal(a);
            c.addAnimal(a);
        }

        for (int i = 0; i < 10; i++) {
            Employee e = new Employee("John" + i, "password", "John" + i, "john" + i + "@gmail.com", "Employee");
            eDao.addEmployee(e);
        }

        assertEquals(10, cDao.findAllCustomer().size(), "Customer list size is not 10");
        assertEquals(11, cDao.findByEmailCustomer("rammus1@gmail.com").findAllAnimals().size(), "Customer 1 animal list size is not 11");
        assertEquals(20, aDao.findAllAnimal().size(), "Animal list size is not 21");
    }


    @Test
    @Order(2)
    @DisplayName("Find all animals from customer test")
    public void findTest() {
        System.out.println("Find test");
        List<Customer> customerList = cDao.findAllCustomer();
        List<Animal> animalList = new ArrayList<>();
        for (Customer c : customerList) {
            animalList.addAll(c.getAnimals());
        }
        for (Animal a : animalList) {
            System.out.println("Found animal: " + a.toString());
        }
    }

    @Test
    @Order(3)
    @DisplayName("Delete all animals test")
    public void deleteAllAnimalTest() {
        System.out.println("Delete all animal test");
        List<Animal> animalList = aDao.findAllAnimal();
        for (Animal a : animalList) {
            aDao.deleteByIdAnimal(a.getId());
        }
        animalList = aDao.findAllAnimal();
        assertEquals(0, animalList.size(), "Animal list size is not 0");
    }

    @Test
    @Order(4)
    @DisplayName("Delete all customers test")
    public void deleteAllCustomerTest() {
        System.out.println("Delete all customer test");
        List<Customer> customerList = cDao.findAllCustomer();
        for (Customer c : customerList) {
            cDao.deleteByIdCustomer(c.getId());
        }
        customerList = cDao.findAllCustomer();
        assertEquals(0, customerList.size(), "Customer list size is not 0");
    }

    @Test
    @Order(5)
    @DisplayName("Delete all employees test")
    public void deleteAllEmployeeTest() {
        System.out.println("Delete all employee test");
        List<Employee> employeeList = eDao.findAllEmployee();
        for (Employee e : employeeList) {
            eDao.deleteByIdEmployee(e.getId());
        }
        employeeList = eDao.findAllEmployee();
        assertEquals(0, employeeList.size(), "Employee list size is not 0");
    }
}
