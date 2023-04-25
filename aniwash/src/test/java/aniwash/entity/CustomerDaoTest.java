package aniwash.entity;

import aniwash.dao.CustomerDao;
import aniwash.dao.ICustomerDao;
import aniwash.datastorage.DatabaseConnector;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CustomerDAO: tietokantatoimintojen (CRUD) testaus")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerDaoTest {

    private static ICustomerDao customerDao;
    private final String name = "John";
    private final String phone = "0401234567";
    private final String email = "john.johna@email.com";
    private final String address = "John street 1";
    private final String postalCode = "12345";

    private Customer customer = new Customer(name, phone, email, address, postalCode);

    @BeforeAll
    public static void beforeAll() {
        DatabaseConnector.openDbConnection("com.aniwash.test");
        customerDao = new CustomerDao();
    }

    @BeforeEach
    public void beforeEach() {
        customer = new Customer(name, phone, email, address, postalCode);
    }

    @AfterEach
    public void endOfEach() {
        for (Customer c : customerDao.findAll()) {
            customerDao.deleteById(c.getId());
        }
    }

/*
    @AfterAll
    public static void afterAll() {
        DatabaseConnector.closeDbConnection();
    }
*/

    /*
    ================================================================================================================
    ===============================================================================================================
    */
    @Test
    @DisplayName("Tässä kaikki testit yhdessä metodissa.")
    public void testDAOCustomer() {
        // Jos joku testi ei mene läpi, tämän metodin suoritus päättyy heti.
        // Lisää asiakas
        assertTrue(customerDao.add(customer), "addCustomer(): Uuden customerin lisääminen ei onnistu.");
        assertFalse(customerDao.add(customer), "addCustomer(): Saman customerin pystyy lisäämään kahteen kertaan.");
        long id = customer.getId();
        // Nyt haun tulee onnistua ja kenttien tulee tietenkin olla asetettu oikein
        assertNotNull((customer = customerDao.findById(id)), "findCustomerById(): Juuri lisätyn asiakkaan hakeminen ei onnistunut");
        assertEquals(id, customer.getId(), "getId(): customer id tunnus väärin.");
        assertEquals(email, customer.getEmail(), "getEmail(): emailarvo väärin.");
        assertEquals(name, customer.getName(), "getName(): customerin nimi väärin.");
        // CustomerUpdate() muutoksen tulee onnistua
        customer.setName("Jonna");
        assertTrue(customerDao.update(customer), "updateCustomer(): Juuri lisätyn asiakkaan sukupuolen päivitys ei onnistunut.");
        System.out.println("customer.getId() = " + customer.getId());
        System.out.println("id: " + id);
        customer = customerDao.findById(id);
        assertEquals(customer.getName(), "Jonna", "updateCustomer(): Asiakkaan nimi arvo väärin.");
        customer.setAddress("Kuusikatu 2");
        assertTrue(customerDao.update(customer), "updateCustomer(): Juuri lisätyn asiakkaan osoite päivitys ei onnistunut.");
        customer = customerDao.findById(id);
        assertEquals("Kuusikatu 2", customer.getAddress(), "getAddress(): customerin osoite arvo on väärin.");
        customer.setPostalCode("00100");
        assertTrue(customerDao.update(customer), "updateCustomer(): Juuri lisätyn asiakkaan postinumeron päivitys ei onnistunut.");
        customer = customerDao.findById(id);
        assertEquals("00100", customer.getPostalCode(), "getPostalcode(): customerin postinumero arvo on väärin.");
        customer.setPhone("0401234567");
        assertTrue(customerDao.update(customer), "updateCustomer(): Juuri lisätyn asiakkaan puhelinnumeron päivitys ei onnistunut.");
        customer = customerDao.findById(id);
        assertEquals("0401234567", customer.getPhone(), "getPhone(): customerin puhelinnumero arvo on väärin.");
        // Testissä lisätyn customerin poiston tulee onnistua
        assertTrue(customerDao.deleteById(id), "deleteCustomerById(): Asiakkaan poisto ei onnistunut.");
        assertNull(customerDao.findById(id), "deleteCustomerById(): Asiakkaan poisto ei onnistunut - asiakan voitiin hakea tietokannasta.");
        // Olemattoman customerin poiston tulee "epäonnistua"
        assertFalse(customerDao.deleteById(id), "deleteCustomerById(): Väittää poistaneensa olemattoman asiakkaan.");
        // Poistetaan kaikki customerit
        System.out.println("Delete all employees test");
        List<Customer> customerList = customerDao.findAll();
        for (Customer c : customerList) {
            customerDao.deleteById(c.getId());
        }
        assertEquals(0, customerDao.findAll().size(), "Employee list size should be 0");

    }

    @Test
    @DisplayName("Asiakkaan lisäämisen tulee onnistua")
    @Order(1)
    public void testAddCustomer() {
        assertTrue(customerDao.add(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        // Nyt haun tulee onnistua ja jokaisen kentän tulee tietenkin olla asetettu oikein
        assertNotNull((customer = customerDao.findNewest()), "addCustomer(): Juuri lisätyn asiakkaan hakeminen ei onnistunut");
        assertEquals(name, customer.getName(), "getName(): Asiakkaan nimi on väärin.");
        assertEquals(email, customer.getEmail(), "getEmail(): Email on väärin.");
    }

    @Test
    @DisplayName("Samaa asiakasta ei saa lisätä toistamiseen")
    @Order(2)
    public void testAddCustomer2() {
        assertTrue(customerDao.add(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        assertFalse(customerDao.add(customer), "addCustomer(): Saman asiakkaan voi lisätä kahteen kertaan.");
    }

    @Test
    @DisplayName("Asiakkaan haun tulee onnistua")
    @Order(3)
    public void testSearchById() {
        assertTrue(customerDao.add(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        // Nyt haun tulee onnistua ja jokaisen kentän tulee tietenkin olla asetettu oikein
        long id = customer.getId();
        customer = customerDao.findById(id);
        assertNotNull(customer, "findCustomerById(): Juuri lisätyn asiakkaan hakeminen ei onnistunut");
        assertNotNull(customerDao.findByPhone("0401234567"), "findByPhoneCustomer(): Juuri lisätyn asiakkaan hakeminen ei onnistunut");
        assertEquals(id, customer.getId(), "getId(): customer id tunnus väärin.");
        assertEquals("john.johna@email.com", customer.getEmail(), "getEmail(): emailarvo väärin.");
        assertEquals("John", customer.getName(), "getName(): customerin nimi väärin.");

    }

    @Test
    @DisplayName("Olemattoman asiakkaan haun tulee palauttaa null")
    @Order(4)
    public void testSearchById2() {
        assertNull(customerDao.findByName("John"), "findByNameCustomer(): Olemattoman asiakkaan haun piti palauttaa false");
        assertNull(customerDao.findByEmail("john.johna@email.com"), "findByEmailCustomer(): Olemattoman asiakkaan haun piti palauttaa false");
        assertNull(customerDao.findByPhone("0401234567"), "findByPhoneCustomer(): Olemattoman asiakkaan haun piti palauttaa false");
        assertNull(customerDao.findByName("Kimmo Kala"), "findByNameCustomerList(): Olemattoman asiakkaan haun piti palauttaa false");

    }

    @Test
    @DisplayName("Asiakkaan email muutoksen tulee onnistua")
    @Order(5)
    public void testEmail() {
        assertTrue(customerDao.add(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        customer.setEmail("ulisija@ulisee.com");
        assertTrue(customerDao.update(customer), "updateCustomer(): Juuri lisätyn emailin päivitys ei onnistunut.");
        customer = customerDao.findById(customer.getId());
        assertEquals(customer.getEmail(), "ulisija@ulisee.com", "updateCustomer(): email arvo on väärin.");
    }

    @Test
    @DisplayName("Asiakkaan nimen muutoksen tulee onnistua")
    @Order(6)
    public void testName() {
        assertTrue(customerDao.add(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        customer.setName("Ulisor");
        assertTrue(customerDao.update(customer), "updateCustomer(): Juuri lisätyn asiakkaan nimen päivitys ei onnistunut.");
        customer = customerDao.findById(customer.getId());
        assertEquals("Ulisor", customer.getName(), "getName(): customerin nimi väärin.");
    }

    @Test
    @DisplayName("Asiakkaan osoitteen muutos tulee onnistua")
    @Order(7)
    public void testAddress() {
        assertTrue(customerDao.add(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        customer.setAddress("Kuusikatu 2");
        assertTrue(customerDao.update(customer), "updateCustomer(): Juuri lisätyn asiakkaan osoite päivitys ei onnistunut.");
        customer = customerDao.findById(customer.getId());
        assertEquals("Kuusikatu 2", customer.getAddress(), "getAddress(): customerin osoite arvo on väärin.");
    }

    @Test
    @DisplayName("Asiakkaan osoite on oikea")
    @Order(8)
    public void testAddress2() {
        assertTrue(customerDao.add(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        assertEquals(address, customer.getAddress(), "getAddress(): customerin osoite arvo on väärin.");
    }

    @Test
    @DisplayName("Asiakkaan postinumeron muutos tulee onnistua")
    @Order(9)
    public void testPostcode() {
        assertTrue(customerDao.add(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        customer.setPostalCode("00100");
        assertTrue(customerDao.update(customer), "updateCustomer(): Juuri lisätyn asiakkaan postinumeron päivitys ei onnistunut.");
        customer = customerDao.findById(customer.getId());
        assertEquals("00100", customer.getPostalCode(), "getPostalcode(): customerin postinumero arvo on väärin.");
    }

    @Test
    @DisplayName("Asiakkaan postinumero on oikea")
    @Order(10)
    public void testPostcode2() {
        assertTrue(customerDao.add(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        assertEquals(postalCode, customer.getPostalCode(), "getPostalcode(): customerin postinumero arvo on väärin.");
    }

    @Test
    @DisplayName("Asiakkaan puhelinnumeron muutos tulee onnistua")
    @Order(11)
    public void testPhone() {
        assertTrue(customerDao.add(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        customer.setPhone("0401234567");
        assertTrue(customerDao.update(customer), "updateCustomer(): Juuri lisätyn asiakkaan puhelinnumeron päivitys ei onnistunut.");
        customer = customerDao.findById(customer.getId());
        assertEquals("0401234567", customer.getPhone(), "getPhone(): customerin puhelinnumero arvo on väärin.");
    }

    @Test
    @DisplayName("Asiakkaan poiston tulee onnistuttua")
    @Order(12)
    public void testDeleteCustomer() {
        assertTrue(customerDao.add(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        Customer c = customerDao.findById(customer.getId());
        assertTrue((customerDao.deleteById(c.getId())), "deleteCustomer(): Customerin poisto ei onnistunut.");
        c = customerDao.findById(c.getId());
        assertNull(c, "deleteCustomer(): Customerin poisto ei onnistunut - asiakas voitiin hakea tietokannasta.");
    }

    @Test
    @DisplayName("Olemattoman asiakkaan poistoyrityksen tulee palauttaa false")
    @Order(13)
    public void testDeleteCustomer2() {
        assertFalse(customerDao.deleteById(customer.getId()), "deleteCustomer(): Väittää poistaneensa olemattoman customerin.");
    }

    @Test
    @DisplayName("Asiakas listan haku nimen perusteella")
    @Order(14)
    public void testNameList() {
        Customer customer2 = new Customer(name, phone, email, "John street 2", postalCode);
        Customer customer3 = new Customer(name, phone, email, "John street 3", postalCode);
        assertTrue(customerDao.add(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        assertTrue(customerDao.add(customer2), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        assertTrue(customerDao.add(customer3), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        List<Customer> customers2 = new ArrayList<>();
        customers2.add(customer);
        customers2.add(customer2);
        customers2.add(customer3);
        List<Customer> customers = customerDao.findByNameList(name);
        assertTrue(customers.size() > 0, "findByNameCustomerList(): Asiakas listan haku nimen perusteella ei onnistunut. Lista liian lyhyt.");
        assertEquals(3, customers.size(), "findByNameCustomerList(): Asiakas listan haku nimen perusteella ei onnistunut. Listan pituus ei täsmää.");
        assertEquals(customers2, customers, "findByNameCustomerList(): Asiakas listan haku nimen perusteella ei onnistunut. Listat eivät täsmää.");
    }

    @Test
    @DisplayName("Olemattoman asikaan päivitys yrityksen pitää palauttaa false")
    @Order(15)
    public void testUpdateCustomer2() {
        customer = new Customer("Kimmo", phone, email, address, postalCode);
        assertFalse(customerDao.update(customer), "updateCustomer(): Väittää päivittäneensä olematonta asiakasta.");
    }

    @Test
    @DisplayName("Customer soft delete")
    @Order(16)
    public void testSoftDelete() {
        assertTrue(customerDao.add(customer), "addCustomer(): Uuden asiakkaan lisääminen ei onnistu.");
        long id = customer.getId();
        customer.setDeleted();
        assertTrue(customerDao.update(customer), "updateCustomer");
        assertNotNull(customerDao.findById(id), "softDeleteCustomer(): Customerin poisto ei onnistunut.");
        assertEquals(1, customerDao.findById(id).isDeleted(), "softDeleteCustomer(): Customerin poisto ei onnistunut.");
    }

}
