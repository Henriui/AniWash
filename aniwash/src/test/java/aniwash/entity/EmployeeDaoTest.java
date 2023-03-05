package aniwash.entity;

import aniwash.dao.EmployeeDao;
import aniwash.dao.IEmployeeDao;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EmployeeDAO: CRUD testings")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeDaoTest {

    private final IEmployeeDao eDao = new EmployeeDao();

    private final String username = "ani";
    private final String password = "password";
    private final String name = "Tim";
    private final String email = "tim@aniwash.com";
    private final String title = "Manager";

    private Employee employee = new Employee(username, password, name, email, title, UserType.EMPLOYEE);

    @BeforeEach
    public void init() {
        employee = new Employee(username, password, name, email, title, UserType.EMPLOYEE);
    }

    @AfterEach
    public void tearDown() {
        eDao.deleteByIdEmployee(employee.getId());
    }

    @Test
    @DisplayName("Create employee test")
    @Order(1)
    public void createEmployeeTest() {
        assertTrue(eDao.addEmployee(employee), "addEmployee(): Employee was not added");
        assertFalse(eDao.addEmployee(employee), "addEmployee(): Duplicates should not be allowed.");

        employee.setUsername("tim");
        assertFalse(eDao.addEmployee(employee), "addEmployee(): Duplicates should not be allowed.");

        assertNotNull((employee = eDao.findByNameEmployee("Tim")), "addEmployee(): Added employee is null");
        assertEquals(name, employee.getName(), "getName(): Name is invalid");
        assertEquals(email, employee.getEmail(), "getEmail(): Email is invalid.");
    }

    @Test
    @DisplayName("Find employee by id test")
    @Order(2)
    public void findEmployeeByIdTest() {
        eDao.addEmployee(employee);
        long id = employee.getId();
        employee = eDao.findByIdEmployee(id);
        assertEquals(id, employee.getId(), "Employee id is not the same");
    }

    @Test
    @DisplayName("Find employee by username test")
    @Order(3)
    public void findEmployeeByUsernameTest() {
        eDao.addEmployee(employee);
        Employee e = eDao.findByUsernameEmployee(employee.getUsername());
        assertEquals(employee.getUsername(), e.getUsername(), "Employee username is not the same");
    }

    @Test
    @DisplayName("Find employee by email test")
    @Order(4)
    public void findEmployeeByEmailTest() {
        eDao.addEmployee(employee);
        Employee e = eDao.findByEmailEmployee(employee.getEmail());
        assertEquals(email, e.getEmail(), "Employee email is not the same");
    }

    @Test
    @DisplayName("Find employee by title test")
    @Order(5)
    public void findEmployeeByTitleTest() {
        eDao.addEmployee(employee);
        Employee e = eDao.findByTitleEmployee(employee.getTitle());
        assertEquals(title, e.getTitle(), "Employee role is not the same");
    }

    @Test
    @DisplayName("Find employee by name test")
    @Order(6)
    public void findEmployeeByNameTest() {
        eDao.addEmployee(employee);
        Employee e = eDao.findByNameEmployee(employee.getName());
        assertEquals(name, e.getName(), "Employee name is not the same");
    }

    @Test
    @DisplayName("Update employee test")
    @Order(7)
    public void updateEmployeeTest() {
        eDao.addEmployee(employee);
        employee = eDao.findByIdEmployee(employee.getId());
        employee.setTitle("Employee");
        employee.setEmail("timothy@aniwash.com");
        employee.setName("Timothy");
        assertTrue(eDao.updateEmployee(employee), "updateEmployee(): Employee was not updated");
        employee = eDao.findByIdEmployee(employee.getId());
        assertEquals("Employee", employee.getTitle(), "Employee role is not the same");
        assertEquals("timothy@aniwash.com", employee.getEmail(), "Employee email is not the same");
        assertEquals("Timothy", employee.getName(), "Employee name is not the same");
    }

    @Test
    @DisplayName("Update employee password test")
    @Order(8)
    public void updateEmployeePasswordTest() {
        eDao.addEmployee(employee);
        employee = eDao.findByIdEmployee(employee.getId());
        employee.setPassword("newPassword");
        assertTrue(eDao.updateEmployee(employee), "updateEmployee(): Employee was not updated");
        employee = eDao.findByIdEmployee(employee.getId());
        assertEquals("newPassword", employee.getPassword(), "updateEmployee(): Employee password is not the same");
        System.out.println(employee.getPassword());
    }

    @Test
    @DisplayName("Delete employee test")
    @Order(9)
    public void deleteEmployeeTest() {
        eDao.addEmployee(employee);
        assertTrue(eDao.deleteByIdEmployee(employee.getId()), "deleteByIdEmployee(): Employee was not deleted");
    }

    @Test
    @DisplayName("Delete null employee test")
    @Order(10)
    public void deleteEmployeeTest2() {
        assertFalse(eDao.deleteByIdEmployee(employee.getId()), "deleteByIdEmployee(): There was Employee to delete");
    }

    @Test
    @DisplayName("Employee soft delete test")
    @Order(11)
    public void softDeleteEmployeeTest() {
        assertTrue(eDao.addEmployee(employee), "addEmployee(): Employee was not added");
        long id = employee.getId();
        employee.setDeleted();
        assertTrue(eDao.updateEmployee(employee), "updateEmployee(): Employee was not updated");
        assertNotNull((employee = eDao.findByIdEmployee(id)), "addEmployee(): Added employee is null");
        assertEquals(1, employee.isDeleted(), "isDeleted(): Employee is not deleted");
    }

    @Test
    @DisplayName("Search for nonexisting employee should return null")
    @Order(12)
    public void searchForNonExistingEmployeeTest() {
        assertNull(eDao.findByIdEmployee(9999L), "findByIdEmployee(): Employee was found");
        assertNull(eDao.findByUsernameEmployee("nonexisting"), "findByUsernameEmployee(): Employee was found");
        assertNull(eDao.findByEmailEmployee("nonexisting@mail.com"), "findByEmailEmployee(): Employee was found");
        assertNull(eDao.findByTitleEmployee("nonexisting"), "findByTitleEmployee(): Employee was found");
        assertNull(eDao.findByNameEmployee("nonexisting"), "findByNameEmployee(): Employee was found");
    }

    @Test
    @DisplayName("Attempting to edit nonexisting employee should return false")
    @Order(13)
    public void editNonExistingEmployeeTest() {
        employee = new Employee("kimmok", "password", "Kimmo Kala", "kimmo.kala@gmail.com", "Manager");
        assertFalse(eDao.updateEmployee(employee), "updateEmployee(): Employee was updated");
    }

}
