package aniwash.entity;

import aniwash.dao.AnimalDao;
import aniwash.dao.CustomerDao;
import aniwash.dao.IAnimalDao;

import aniwash.dao.ICustomerDao;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Customer and Animal class CRUD testings")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerAnimalDbTest {
    IAnimalDao aDao = new AnimalDao();
    ICustomerDao cDao = new CustomerDao();

    @Test
    @DisplayName("Rammus test")
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

        List<Animal> animals = new ArrayList<Animal>();
        for (int i = 0; i < 10; i++) {
            Animal a = new Animal("Rohmu" + i, "Cat", "Meow", 1 + i, "Maukuva nakki");
            animals.add(a);
        }

        for (Animal a : animals) {
            Customer c = cDao.findByIdCustomer(1L);
            aDao.addAnimal(a);
            c.addAnimal(a);
        }

        assertEquals(10, cDao.findAllCustomer().size(), "Customer list size is not 10");
        assertEquals(11, cDao.findAllAnimalsByOwnerId(1L).size(), "Customer 1 animal list size is not 11");
        assertEquals(20, aDao.findAllAnimal().size(), "Animal list size is not 21");
    }

/*
    @Test
    @DisplayName("Delete test")
    private void deleteTest() {
        System.out.println("Delete test");
        Customer c = cDao.findByIdCustomer(1L);
        cDao.deleteByIdCustomer(c.getId());
        for (Animal a : c.getAnimals()) {
            aDao.deleteByIdAnimal(a.getId());
        }
    }
*/

/*
    @Test
    @DisplayName("Find all customers test")
    private void findAllCustomerTest() {
        List<Customer> customers = cDao.findAllCustomer();
        for (Customer c : customers) {
            System.out.println("Found customer: " + c.toString());
        }
    }
*/

/*
    @Test
    @DisplayName("Find all animals from customer test")
    private void findTest() {
        System.out.println("Find test");
        for (Animal a : cDao.findAllAnimalsByOwnerId(1L)) {
            System.out.println("Customer has animal: " + a.toString());
        }
    }
*/


}
