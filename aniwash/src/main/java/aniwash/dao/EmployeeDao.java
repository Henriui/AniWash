package aniwash.dao;

import aniwash.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

/*
 * This class is used to access the database and perform CRUD operations on the Employee table.
 * @author rasmushy
 */
public class EmployeeDao implements IEmployeeDao {

    EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();

    @Override
    public boolean addEmployee(Employee employee) {
        boolean success = true;
        em.getTransaction().begin();
        Employee c = em.find(Employee.class, employee.getId());
        if (c != null) {
            System.out.println("Employee already exists: " + employee.getId());
            success = false;
        } else {
            em.merge(employee);
        }
        em.getTransaction().commit();
        return success;
    }

    @Override
    public List<Employee> findAllEmployee() {
        em.getTransaction().begin();
        List<Employee> customers = em.createQuery("SELECT a FROM Employee a", Employee.class).getResultList();
        em.getTransaction().commit();
        return customers;
    }

    @Override
    public Employee findByIdEmployee(int id) {
        em.getTransaction().begin();
        Employee employee = em.find(Employee.class, id);
        em.getTransaction().commit();
        return employee;
    }

    @Override
    public Employee findByNameEmployee(String name) {
        Employee employee = null;
        em.getTransaction().begin();
        try {
            employee = em.createQuery("SELECT a FROM Employee a WHERE a.name = :name", Employee.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No Employee found with name: " + name);
        }
        em.getTransaction().commit();
        return employee;
    }

    @Override
    public boolean deleteByIdEmployee(int id) {
        boolean deleted = false;
        em.getTransaction().begin();
        Employee employee = em.find(Employee.class, id);
        if (employee != null) {
            em.remove(employee);
            deleted = true;
        }
        em.getTransaction().commit();
        return deleted;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        em.getTransaction().begin();
        Employee t = em.find(Employee.class, employee.getId());
        if (t == null) {
            em.getTransaction().commit();
            return false;
        }
        //t.setUsername(employee.getUsername());
        t.setName(employee.getName());
        t.setEmail(employee.getEmail());
        t.setTitle(employee.getTitle());
        em.getTransaction().commit();
        return true;
    }
}
