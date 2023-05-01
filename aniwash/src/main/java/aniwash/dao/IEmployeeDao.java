package aniwash.dao;

import aniwash.entity.Employee;

public interface IEmployeeDao extends IDao<Employee> {

    Employee findByName(String name);

    Employee findByTitle(String title);

    Employee findByUsername(String username);

    Employee findByEmail(String email);

}
