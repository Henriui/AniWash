package aniwash.dao;

import aniwash.entity.Employee;

import java.util.List;

public interface IEmployeeDao {

    boolean addEmployee(Employee employee);

    List<Employee> findAllEmployee();

    Employee findByIdEmployee(int id);

    Employee findByNameEmployee(String name);

    boolean deleteByIdEmployee(int id);

    boolean updateEmployee(Employee employee);
}
