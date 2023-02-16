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

    private final EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();

    @Override
    public boolean addEmployee(Employee employee) {
        Employee c = em.find(Employee.class, employee.getId());
        if (c != null) {
            System.out.println("Employee already exists with id: " + employee.getId());
            return false;
        }
        executeInTransaction(em -> em.persist(employee));
        return true;
    }

    @Override
    public List<Employee> findAllEmployee() {
        return em.createQuery("SELECT a FROM Employee a", Employee.class).getResultList();
    }

    @Override
    public Employee findByIdEmployee(long id) {
        return em.find(Employee.class, id);
    }

    @Override
    public Employee findByNameEmployee(String name) {
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
        Employee t = em.find(Employee.class, employee.getId());
        if (t == null) {
            System.out.println("Employee does not exist: " + employee.getId());
            return false;
        }
        em.getTransaction().begin();
        //t.setUsername(employee.getUsername());
        t.setName(employee.getName());
        t.setEmail(employee.getEmail());
        t.setTitle(employee.getTitle());
        em.getTransaction().commit();
        return true;
    }

    @Override
    public boolean deleteByIdEmployee(long id) {
        Employee employee = em.find(Employee.class, id);
        if (employee != null) {
            executeInTransaction(em -> em.remove(employee));
            return true;
        }
        System.out.println("Employee does not exist with id: " + id);
        return false;
    }

    private void executeInTransaction(Consumer<EntityManager> action) {
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
