package aniwash.entity;

import aniwash.dao.EmployeeDao;
import aniwash.dao.IEmployeeDao;
import aniwash.datastorage.DatabaseConnector;
import aniwash.enums.UserType;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EmployeeDAO: CRUD testings")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeDaoTest {

    private static IEmployeeDao eDao;

    private final String username = "ani";
    private final String password = "password";
    private final String name = "Tim";
    private final String email = "tim@aniwash.com";
    private final String title = "Manager";

    private Employee employee = new Employee(username, password, name, email, title, UserType.EMPLOYEE);

    @BeforeAll
    public static void initAll() {
        DatabaseConnector.openDbConnection("com.aniwash.test");
        eDao = new EmployeeDao();
    }

    @BeforeEach
    public void init() {
        employee = new Employee(username, password, name, email, title, UserType.EMPLOYEE);
    }

    @AfterEach
    public void tearDown() {
        for (Employee e : eDao.findAll()) {
            eDao.deleteById(e.getId());
        }
    }

/*
    @AfterAll
    public static void tearDownAll() {
        DatabaseConnector.closeDbConnection();
    }
*/

    @Test
    @DisplayName("Create employee test")
    @Order(1)
    public void createEmployeeTest() {
        assertTrue(eDao.add(employee), "addEmployee(): Employee was not added");
        assertFalse(eDao.add(employee), "addEmployee(): Duplicates should not be allowed.");
        employee.setUsername("tim");
        assertFalse(eDao.add(employee), "addEmployee(): Duplicates should not be allowed.");
        assertNotNull((employee = eDao.findByName("Tim")), "addEmployee(): Added employee is null");
        assertEquals(name, employee.getName(), "getName(): Name is invalid");
        assertEquals(email, employee.getEmail(), "getEmail(): Email is invalid.");
    }

    @Test
    @DisplayName("Find employee by id test")
    @Order(2)
    public void findEmployeeByIdTest() {
        eDao.add(employee);
        long id = employee.getId();
        employee = eDao.findById(id);
        assertEquals(id, employee.getId(), "Employee id is not the same");
    }

    @Test
    @DisplayName("Find employee by username test")
    @Order(3)
    public void findEmployeeByUsernameTest() {
        eDao.add(employee);
        Employee e = eDao.findByUsername(employee.getUsername());
        assertEquals(employee.getUsername(), e.getUsername(), "Employee username is not the same");
    }

    @Test
    @DisplayName("Find employee by email test")
    @Order(4)
    public void findEmployeeByEmailTest() {
        eDao.add(employee);
        Employee e = eDao.findByEmail(employee.getEmail());
        assertEquals(email, e.getEmail(), "Employee email is not the same");
    }

    @Test
    @DisplayName("Find employee by title test")
    @Order(5)
    public void findEmployeeByTitleTest() {
        eDao.add(employee);
        Employee e = eDao.findByTitle(employee.getTitle());
        assertEquals(title, e.getTitle(), "Employee role is not the same");
    }

    @Test
    @DisplayName("Find employee by name test")
    @Order(6)
    public void findEmployeeByNameTest() {
        eDao.add(employee);
        Employee e = eDao.findByName(employee.getName());
        assertEquals(name, e.getName(), "Employee name is not the same");
    }

    @Test
    @DisplayName("Update employee test")
    @Order(7)
    public void updateEmployeeTest() {
        eDao.add(employee);
        employee = eDao.findById(employee.getId());
        employee.setTitle("Employee");
        employee.setEmail("timothy@aniwash.com");
        employee.setName("Timothy");
        assertTrue(eDao.update(employee), "updateEmployee(): Employee was not updated");
        employee = eDao.findById(employee.getId());
        assertEquals("Employee", employee.getTitle(), "Employee role is not the same");
        assertEquals("timothy@aniwash.com", employee.getEmail(), "Employee email is not the same");
        assertEquals("Timothy", employee.getName(), "Employee name is not the same");
    }

    @Test
    @DisplayName("Update employee password test")
    @Order(8)
    public void updateEmployeePasswordTest() {
        eDao.add(employee);
        employee = eDao.findById(employee.getId());
        employee.setPassword("newPassword");
        assertTrue(eDao.update(employee), "updateEmployee(): Employee was not updated");
        employee = eDao.findById(employee.getId());
        assertEquals("newPassword", employee.getPassword(), "updateEmployee(): Employee password is not the same");
        System.out.println(employee.getPassword());
    }

    @Test
    @DisplayName("Delete employee test")
    @Order(9)
    public void deleteEmployeeTest() {
        eDao.add(employee);
        assertTrue(eDao.deleteById(employee.getId()), "deleteByIdEmployee(): Employee was not deleted");
    }

    @Test
    @DisplayName("Delete null employee test")
    @Order(10)
    public void deleteEmployeeTest2() {
        assertFalse(eDao.deleteById(employee.getId()), "deleteByIdEmployee(): There was Employee to delete");
    }

    @Test
    @DisplayName("Employee soft delete test")
    @Order(11)
    public void softDeleteEmployeeTest() {
        assertTrue(eDao.add(employee), "addEmployee(): Employee was not added");
        long id = employee.getId();
        employee.setDeleted();
        assertTrue(eDao.update(employee), "updateEmployee(): Employee was not updated");
        assertNotNull((employee = eDao.findById(id)), "addEmployee(): Added employee is null");
        assertEquals(1, employee.isDeleted(), "isDeleted(): Employee is not deleted");
    }

    @Test
    @DisplayName("Search for nonexisting employee should return null")
    @Order(12)
    public void searchForNonExistingEmployeeTest() {
        assertNull(eDao.findById(9999L), "findByIdEmployee(): Employee was found");
        assertNull(eDao.findByUsername("nonexisting"), "findByUsernameEmployee(): Employee was found");
        assertNull(eDao.findByEmail("nonexisting@mail.com"), "findByEmailEmployee(): Employee was found");
        assertNull(eDao.findByTitle("nonexisting"), "findByTitleEmployee(): Employee was found");
        assertNull(eDao.findByName("nonexisting"), "findByNameEmployee(): Employee was found");
    }

    @Test
    @DisplayName("Attempting to edit nonexisting employee should return false")
    @Order(13)
    public void editNonExistingEmployeeTest() {
        employee = new Employee("kimmok", "password", "Kimmo Kala", "kimmo.kala@gmail.com", "Manager", UserType.EMPLOYEE);
        assertFalse(eDao.update(employee), "updateEmployee(): Employee was updated");
    }

}
