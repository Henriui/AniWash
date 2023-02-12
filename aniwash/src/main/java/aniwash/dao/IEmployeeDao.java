package aniwash.dao;

import aniwash.entity.Employee;

import java.util.List;

public interface IEmployeeDao {

    boolean addEmployee(Employee employee);

    List<Employee> findAllEmployee();

    Employee findByIdEmployee(long id);

    Employee findByNameEmployee(String name);

    Employee findByEmailEmployee(String email);

    Employee findByTitleEmployee(String title);

    Employee findByUsernameEmployee(String username);

    boolean deleteByIdEmployee(long id);

    boolean updateEmployee(Employee employee);
}
