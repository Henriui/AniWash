package aniwash.dao;

import aniwash.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.function.Consumer;

/*
 * This class is used to access the database and perform CRUD operations on the Employee table.
 * @author rasmushy, lassib
 */
public class EmployeeDao implements IEmployeeDao {

    @Override
    public boolean addEmployee(Employee employee) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        // Find employee by username to check if it already exists.
        // If it does, return false.
        try {
            Employee e = em.createQuery("SELECT a FROM Employee a WHERE a.username = :username", Employee.class).setParameter("username", employee.getUsername()).getSingleResult();
            if (em.contains(e)) {
                System.out.println("Employee with given username already exists.");
                return false;
            }
        } catch (NoResultException e) {
            System.out.println("No Employee found with username: " + employee.getUsername());
        }

        Employee c = em.find(Employee.class, employee.getId());
        if (em.contains(c)) {
            System.out.println("Employee already exists with id: " + employee.getId());
            return false;
        }

        executeInTransaction(entityManager -> em.persist(employee), em);
        return true;
    }

    @Override
    public List<Employee> findAllEmployee() {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        return em.createQuery("SELECT a FROM Employee a", Employee.class).getResultList();
    }

    @Override
    public Employee findByIdEmployee(long id) {

        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        return em.find(Employee.class, id);
    }

    @Override
    public Employee findByNameEmployee(String name) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Employee emp = null;
        try {
            emp = em.createQuery("SELECT a FROM Employee a WHERE a.name = :name", Employee.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No Employee found with name: " + name);
        }
        return emp;
    }

    @Override
    public Employee findByEmailEmployee(String email) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Employee emp = null;
        try {
            emp = em.createQuery("SELECT a FROM Employee a WHERE a.email = :email", Employee.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No Employee found with email: " + email);
        }
        return emp;
    }

    @Override
    public Employee findByTitleEmployee(String title) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Employee emp = null;
        try {
            emp = em.createQuery("SELECT a FROM Employee a WHERE a.title = :title", Employee.class).setParameter("title", title).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No Employee found with title: " + title);
        }
        return emp;
    }

    @Override
    public Employee findByUsernameEmployee(String username) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Employee emp = null;
        try {
            emp = em.createQuery("SELECT a FROM Employee a WHERE a.username = :username", Employee.class).setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No Employee found with username: " + username);
        }
        return emp;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Employee emp = em.find(Employee.class, employee.getId());
        if (emp == null) {
            System.out.println("Employee does not exist: " + employee.getName());
            return false;
        }
        em.getTransaction().begin();
        emp.setUsername(emp.getUsername());
        emp.setPassword(emp.getPassword());
        emp.setName(emp.getName());
        emp.setEmail(emp.getEmail());
        emp.setTitle(emp.getTitle());
        em.getTransaction().commit();
        return true;
    }

    @Override
    public boolean deleteByIdEmployee(long id) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Employee employee = em.find(Employee.class, id);
        if (employee != null) {
            executeInTransaction(entityManager -> em.remove(employee), em);
            return true;
        }
        System.out.println("Employee does not exist with id: " + id);
        return false;
    }

    @Override
    public Employee findNewestEmployee() {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Employee employee = null;
        try {
            employee = em.createQuery("SELECT a FROM Employee a ORDER BY a.id DESC", Employee.class).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No Employee found");
        }
        return employee;
    }

    private void executeInTransaction(Consumer<EntityManager> action, EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            action.accept(em);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        }
    }

}
