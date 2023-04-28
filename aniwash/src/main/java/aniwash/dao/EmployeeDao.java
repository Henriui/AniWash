package aniwash.dao;

import aniwash.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.function.Consumer;

/**
 * This class is used to access the database and perform CRUD operations on the Employee table.
 *
 * @author rasmushy, lassib
 */
public class EmployeeDao implements IEmployeeDao {

    public boolean add(Employee employee) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
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

    public List<Employee> findAll() {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        return em.createQuery("SELECT a FROM Employee a WHERE a.deleted = 0", Employee.class).getResultList();
    }

    public Employee findById(long id) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Employee e = em.find(Employee.class, id);
        if (e != null && e.isDeleted() > 0) {
            return null;
        }
        return e;
    }

    public Employee findByName(String name) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Employee emp = null;
        try {
            emp = em.createQuery("SELECT a FROM Employee a WHERE a.name = :name AND a.deleted = 0", Employee.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No Employee found with name: " + name);
        }
        return emp;
    }

    public Employee findByEmail(String email) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Employee emp = null;
        try {
            emp = em.createQuery("SELECT a FROM Employee a WHERE a.email = :email AND a.deleted = 0", Employee.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No Employee found with email: " + email);
        }
        return emp;
    }

    public Employee findByTitle(String title) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Employee emp = null;
        try {
            emp = em.createQuery("SELECT a FROM Employee a WHERE a.title = :title AND a.deleted = 0", Employee.class).setParameter("title", title).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No Employee found with title: " + title);
        }
        return emp;
    }

    public Employee findByUsername(String username) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Employee emp = null;
        try {
            emp = em.createQuery("SELECT a FROM Employee a WHERE a.username = :username AND a.deleted = 0", Employee.class).setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No Employee found with username: " + username);
        }
        return emp;
    }

    public boolean update(Employee employee) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Employee emp = em.find(Employee.class, employee.getId());
        if (!em.contains(emp)) {
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

    public boolean deleteById(long id) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Employee employee = em.find(Employee.class, id);
        if (em.contains(employee) && employee.isDeleted() == 0) {
            em.getTransaction().begin();
            employee.setDeleted();
            em.getTransaction().commit();
            //executeInTransaction(entityManager -> em.remove(employee), em);
            return true;
        }
        System.out.println("Employee does not exist with id: " + id);
        return false;
    }

    public Employee findNewest() {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Employee employee = null;
        try {
            employee = em.createQuery("SELECT a FROM Employee a WHERE a.deleted = 0 ORDER BY a.id DESC", Employee.class).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No Employee found");
        }
        return employee;
    }

    public void executeInTransaction(Consumer<EntityManager> action, EntityManager em) {
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
