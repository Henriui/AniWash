package aniwash.entity;

import aniwash.dao.IEmployeeDao;
import aniwash.dao.EmployeeDao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    private Employee employee = new Employee(username, password, name, email, title);

    @BeforeEach
    public void init() {
        employee = new Employee(username, password, name, email, title);
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
    @DisplayName("Find all employees test")
    @Order(6)
    public void findAllEmployeesTest() {
        eDao.addEmployee(employee);
        assertEquals(1, eDao.findAllEmployee().size(), "Employee list size is not 1");
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
    @DisplayName("Delete employee test")
    @Order(8)
    public void deleteEmployeeTest() {
        eDao.addEmployee(employee);
        assertTrue(eDao.deleteByIdEmployee(employee.getId()), "deleteByIdEmployee(): Employee was not deleted");
        assertEquals(0, eDao.findAllEmployee().size(), "Employee list size is not 0");
    }

}
