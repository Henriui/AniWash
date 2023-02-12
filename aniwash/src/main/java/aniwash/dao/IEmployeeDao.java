package aniwash.dao;

import aniwash.entity.Employee;

import java.util.List;

public interface IEmployeeDao {

    boolean addEmployee(Employee employee);

    List<Employee> findAllEmployee();

    Employee findByIdEmployee(Long id);

    Employee findByNameEmployee(String name);

    boolean deleteByIdEmployee(Long id);

    boolean updateEmployee(Employee employee);
}
