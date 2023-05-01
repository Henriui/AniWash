package aniwash.dao;

import aniwash.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.function.Consumer;

/**
 * This class is used to access the database and perform CRUD operations on the Customer table.
 *
 * @author rasmushy
 */
public class CustomerDao implements ICustomerDao {

    public boolean add(Customer customer) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Customer c = em.find(Customer.class, customer.getId());
        if (em.contains(c)) {
            System.out.println("Customer already exists with id: " + customer.getId());
            return false;
        }

        executeInTransaction(entityManager -> em.persist(customer), em);
        return true;
    }

    public List<Customer> findAll() {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        return em.createQuery("SELECT c FROM Customer c WHERE c.deleted = 0", Customer.class).getResultList();
    }

    public Customer findById(long id) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Customer c = em.find(Customer.class, id);
        if (c != null && c.isDeleted() > 0) {
            return null;
        }
        return c;
    }

    public Customer findByEmail(String email) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Customer c = null;
        try {
            c = em.createQuery("SELECT a FROM Customer a WHERE a.email = :email AND a.deleted = 0", Customer.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No customer found with email: " + email);
        }
        return c;
    }

    public Customer findByPhone(String phone) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Customer c = null;
        try {
            c = em.createQuery("SELECT a FROM Customer a WHERE a.phone = :phone AND a.deleted = 0", Customer.class).setParameter("phone", phone).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No customer found with phone: " + phone);
        }
        return c;
    }

    public Customer findByName(String name) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Customer c = null;
        try {
            c = em.createQuery("SELECT a FROM Customer a WHERE a.name = :name AND a.deleted = 0", Customer.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No customer found with name: " + name);
        }
        return c;
    }

    public List<Customer> findByNameList(String name) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        List<Customer> c = null;
        try {
            c = em.createQuery("SELECT a FROM Customer a WHERE a.name = :name AND a.deleted = 0 order by name desc", Customer.class).setParameter("name", name).getResultList();
        } catch (NoResultException e) {
            System.out.println("No customer found with name: " + name);
        }
        assert c != null;
        if (c.isEmpty()) {
            System.out.println("No customer found with name: " + name);
            c = null;
        }
        return c;
    }

    public boolean update(Customer customer) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Customer c = em.find(Customer.class, customer.getId());
        if (!em.contains(c)) {
            System.out.println("Customer does not exist with id: " + customer.getId());
            return false;
        }
        em.getTransaction().begin();
        c.setName(customer.getName());
        c.setEmail(customer.getEmail());
        c.setAddress(customer.getAddress());
        c.setPhone(customer.getPhone());
        c.setPostalCode(customer.getPostalCode());
        em.getTransaction().commit();
        return true;
    }

    public boolean deleteById(long id) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Customer c = em.find(Customer.class, id);
        if (em.contains(c) && c.isDeleted() == 0) {
            em.getTransaction().begin();
            c.setDeleted();
            em.getTransaction().commit();
            //executeInTransaction(entityManager -> em.remove(c), em);
            return true;
        }
        System.out.println("Customer does not exist with id: " + id);
        return false;
    }

    public Customer findNewest() {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Customer c = null;
        try {
            c = em.createQuery("SELECT a FROM Customer a WHERE a.deleted = 0 order by id desc", Customer.class).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No customer found");
        }
        return c;
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
