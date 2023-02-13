package aniwash.entity;

import aniwash.dao.CustomerDao;
import aniwash.dao.ICustomerDao;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("CustomerDAO: tietokantatoimintojen (CRUD) testaus")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerDaoTest {
    private final ICustomerDao customerDao = new CustomerDao();

    private final String name = "John";
    private final String phone = "0401234567";
    private final String email = "john.johna@email.com";
    private final String address = "John street 1";
    private final String postalcode = "12345";

    private Customer customer = new Customer(name, phone, email);


    @BeforeEach
    public void beforeEach() {
        customer = new Customer(name, phone, email);
        customer.setAddress(address);
        customer.setPostalcode(postalcode);
    }

    @AfterEach
    public void endOfEach() {
        customerDao.deleteByIdCustomer(customer.getId());
    }

    /*
    ================================================================================================================
    ===============================================================================================================
    */
    @Test
    @DisplayName("Tässä kaikki testit yhdessä metodissa.")
    public void testDAOCustomer() {
        // Jos joku testi ei mene läpi, tämän metodin suoritus päättyy heti.

        // Lisää asiakas
        assertTrue(customerDao.addCustomer(customer), "addCustomer(): Uuden customerin lisääminen ei onnistu.");
        assertFalse(customerDao.addCustomer(customer), "addCustomer(): Saman customerin pystyy lisäämään kahteen kertaan.");
        long id = customer.getId();

        // Nyt haun tulee onnistua ja kenttien tulee tietenkin olla asetettu oikein
        assertNotNull((customer = customerDao.findByIdCustomer(id)), "findCustomerById(): Juuri lisätyn asiakkaan hakeminen ei onnistunut");
        assertEquals(id, customer.getId(), "getId(): customer id tunnus väärin.");
        assertEquals(email, customer.getEmail(), "getEmail(): emailarvo väärin.");
        assertEquals(name, customer.getName(), "getName(): customerin nimi väärin.");

        // CustomerUpdate() muutoksen tulee onnistua
        customer.setName("Jonna");
        assertTrue(customerDao.updateCustomer(customer), "updateCustomer(): Juuri lisätyn asiakkaan sukupuolen päivitys ei onnistunut.");
        System.out.println("customer.getId() = " + customer.getId());
        System.out.println("id: " + id);
        customer = customerDao.findByIdCustomer(id);
        assertEquals(customer.getName(), "Jonna", "updateCustomer(): Asiakkaan nimi arvo väärin.");
        customer.setAddress("Kuusikatu 2");
        assertTrue(customerDao.updateCustomer(customer), "updateCustomer(): Juuri lisätyn asiakkaan osoite päivitys ei onnistunut.");
        customer = customerDao.findByIdCustomer(id);
        assertEquals("Kuusikatu 2", customer.getAddress(), "getAddress(): customerin osoite arvo on väärin.");
        customer.setPostalcode("00100");
        assertTrue(customerDao.updateCustomer(customer), "updateCustomer(): Juuri lisätyn asiakkaan postinumeron päivitys ei onnistunut.");
        customer = customerDao.findByIdCustomer(id);
        assertEquals("00100", customer.getPostalcode(), "getPostalcode(): customerin postinumero arvo on väärin.");
        customer.setPhone("0401234567");
        assertTrue(customerDao.updateCustomer(customer), "updateCustomer(): Juuri lisätyn asiakkaan puhelinnumeron päivitys ei onnistunut.");
        customer = customerDao.findByIdCustomer(id);
        assertEquals("0401234567", customer.getPhone(), "getPhone(): customerin puhelinnumero arvo on väärin.");

        // Testissä lisätyn customerin poiston tulee onnistua
        assertTrue(customerDao.deleteByIdCustomer(id), "deleteCustomerById(): Asiakkaan poisto ei onnistunut.");
        assertNull(customerDao.findByIdCustomer(id), "deleteCustomerById(): Asiakkaan poisto ei onnistunut - asiakan voitiin hakea tietokannasta.");

        // Olemattoman customerin poiston tulee "epäonnistua"
        assertFalse(customerDao.deleteByIdCustomer(id), "deleteCustomerById(): Väittää poistaneensa olemattoman asiakkaan.");
    }

    @Test
    @DisplayName("Asiakkaan lisäämisen tulee onnistua")
    @Order(1)
    public void testAddCustomer() {
        assertTrue(customerDao.addCustomer(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        // Nyt haun tulee onnistua ja jokaisen kentän tulee tietenkin olla asetettu oikein
        assertNotNull((customer = customerDao.findByNameCustomer("John")), "addCustomer(): Juuri lisätyn asiakkaan hakeminen ei onnistunut");
        assertEquals(name, customer.getName(), "getName(): Asiakkaan nimi on väärin.");
        assertEquals(email, customer.getEmail(), "getEmail(): Email on väärin.");
    }

    @Test
    @DisplayName("Samaa asiakasta ei saa lisätä toistamiseen")
    @Order(2)
    public void testAddCustomer2() {
        assertTrue(customerDao.addCustomer(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        assertFalse(customerDao.addCustomer(customer), "addCustomer(): Saman asiakkaan voi lisätä kahteen kertaan.");
    }

    @Test
    @DisplayName("Asiakkaan haun tulee onnistua")
    @Order(3)
    public void testSearchById() {
        assertTrue(customerDao.addCustomer(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        // Nyt haun tulee onnistua ja jokaisen kentän tulee tietenkin olla asetettu oikein
        long id = customer.getId();
        customer = customerDao.findByIdCustomer(id);
        assertNotNull(customer, "findCustomerById(): Juuri lisätyn asiakkaan hakeminen ei onnistunut");
        assertEquals(id, customer.getId(), "getId(): customer id tunnus väärin.");
        assertEquals("john.johna@email.com", customer.getEmail(), "getEmail(): emailarvo väärin.");
        assertEquals("John", customer.getName(), "getName(): customerin nimi väärin.");

    }

    @Test
    @DisplayName("Olemattoman asiakkaan haun tulee palauttaa null")
    @Order(4)
    public void testSearchById2() {
        assertNull(customerDao.findByNameCustomer("John"), "findByNameCustomer(): Olemattoman asiakkaan haun piti palauttaa false");
        assertNull(customerDao.findByEmailCustomer("john.johna@email.com"), "findByEmailCustomer(): Olemattoman asiakkaan haun piti palauttaa false");
        assertNull(customerDao.findByPhoneCustomer("0401234567"), "findByPhoneCustomer(): Olemattoman asiakkaan haun piti palauttaa false");

    }

    @Test
    @DisplayName("Asiakkaan email muutoksen tulee onnistua")
    @Order(5)
    public void testEmail() {
        assertTrue(customerDao.addCustomer(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");

        customer.setEmail("ulisija@ulisee.com");
        assertTrue(customerDao.updateCustomer(customer), "updateCustomer(): Juuri lisätyn emailin päivitys ei onnistunut.");
        customer = customerDao.findByIdCustomer(customer.getId());
        assertEquals(customer.getEmail(), "ulisija@ulisee.com", "updateCustomer(): email arvo on väärin.");
    }

    @Test
    @DisplayName("Asiakkaan nimen muutoksen tulee onnistua")
    @Order(6)
    public void testName() {
        assertTrue(customerDao.addCustomer(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");

        customer.setName("Ulisor");
        assertTrue(customerDao.updateCustomer(customer), "updateCustomer(): Juuri lisätyn asiakkaan nimen päivitys ei onnistunut.");
        customer = customerDao.findByIdCustomer(customer.getId());
        assertEquals("Ulisor", customer.getName(), "getName(): customerin nimi väärin.");
    }

    @Test
    @DisplayName("Asiakkaan osoitteen muutos tulee onnistua")
    @Order(7)
    public void testAddress() {
        assertTrue(customerDao.addCustomer(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");

        customer.setAddress("Kuusikatu 2");
        assertTrue(customerDao.updateCustomer(customer), "updateCustomer(): Juuri lisätyn asiakkaan osoite päivitys ei onnistunut.");
        customer = customerDao.findByIdCustomer(customer.getId());
        assertEquals("Kuusikatu 2", customer.getAddress(), "getAddress(): customerin osoite arvo on väärin.");
    }

    @Test
    @DisplayName("Asiakkaan osoite on oikea")
    @Order(8)
    public void testAddress2() {
        assertTrue(customerDao.addCustomer(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        assertEquals(address, customer.getAddress(), "getAddress(): customerin osoite arvo on väärin.");
    }

    @Test
    @DisplayName("Asiakkaan postinumeron muutos tulee onnistua")
    @Order(9)
    public void testPostcode() {
        assertTrue(customerDao.addCustomer(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");

        customer.setPostalcode("00100");
        assertTrue(customerDao.updateCustomer(customer), "updateCustomer(): Juuri lisätyn asiakkaan postinumeron päivitys ei onnistunut.");
        customer = customerDao.findByIdCustomer(customer.getId());
        assertEquals("00100", customer.getPostalcode(), "getPostalcode(): customerin postinumero arvo on väärin.");
    }

    @Test
    @DisplayName("Asiakkaan postinumero on oikea")
    @Order(10)
    public void testPostcode2() {
        assertTrue(customerDao.addCustomer(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        assertEquals(postalcode, customer.getPostalcode(), "getPostalcode(): customerin postinumero arvo on väärin.");
    }

    @Test
    @DisplayName("Asiakkaan puhelinnumeron muutos tulee onnistua")
    @Order(11)
    public void testPhone() {
        assertTrue(customerDao.addCustomer(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        customer.setPhone("0401234567");
        assertTrue(customerDao.updateCustomer(customer), "updateCustomer(): Juuri lisätyn asiakkaan puhelinnumeron päivitys ei onnistunut.");
        customer = customerDao.findByIdCustomer(customer.getId());
        assertEquals("0401234567", customer.getPhone(), "getPhone(): customerin puhelinnumero arvo on väärin.");
    }

    @Test
    @DisplayName("Asiakkaan poiston tulee onnistuttua")
    @Order(12)
    public void testDeleteCustomer() {
        assertTrue(customerDao.addCustomer(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        Customer c = customerDao.findByIdCustomer(customer.getId());
        assertTrue((customerDao.deleteByIdCustomer(c.getId())), "deleteCustomer(): Customerin poisto ei onnistunut.");
        c = customerDao.findByIdCustomer(c.getId());
        assertNull(c, "deleteCustomer(): Customerin poisto ei onnistunut - asiakas voitiin hakea tietokannasta.");
    }

    @Test
    @DisplayName("Olemattoman asiakkaan poistoyrityksen tulee palauttaa false")
    @Order(13)
    public void testDeleteCustomer2() {
        assertFalse(customerDao.deleteByIdCustomer(customer.getId()), "deleteCustomer(): Väittää poistaneensa olemattoman customerin.");
    }

    @Test
    @Order(14)
    @DisplayName("Find all appointments with customer name")
    public void findAppointmentsByNameTest() {
        Appointment a = new Appointment(ZonedDateTime.parse("2020-01-01T10:00:00+02:00[Europe/Helsinki]"), "Testi pesu");
        customer.addAppointment(a);
        customerDao.addCustomer(customer);
        List<Appointment> aList = new ArrayList<>(customerDao.findAppointmentsByCustomerName("John"));
        assertEquals(aList.get(0).getDescription(), "Testi pesu", "findAppointmentsByCustomerName(): Didn't find appointments with customers name.");
    }

    @Test
    @Order(15)
    @DisplayName("Find all animals with customer name")
    public void findAnimalsByNameTest() {
        Animal a = new Animal("Pertti", "Dog", "Saksanpaimenkoira", 8, "Pertti on hyvä koira.");
        customer.addAnimal(a);
        customerDao.addCustomer(customer);
        List<Animal> aList = new ArrayList<>(customerDao.findAnimalsByCustomerName("John"));
        assertEquals(aList.get(0).getName(), "Pertti", "findAnimalsByCustomerName(): Didn't find animals with customers name.");
    }
}
