package aniwash.dao;

import aniwash.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.function.Consumer;

/*
 * This class is used to access the database and perform CRUD operations on the Customer table.
 * @author rasmushy
 */
public class CustomerDao implements ICustomerDao {

    @Override
    public boolean addCustomer(Customer customer) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Customer c = em.find(Customer.class, customer.getId());
        if (em.contains(c)) {
            System.out.println("Customer already exists with id: " + customer.getId());
            return false;
        }

        executeInTransaction(entityManager -> em.persist(customer), em);
        return true;
    }

    @Override
    public List<Customer> findAllCustomer() {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        return em.createQuery("SELECT c FROM Customer c WHERE c.deleted = 0", Customer.class).getResultList();
    }

    @Override
    public Customer findByIdCustomer(long id) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        return em.find(Customer.class, id);
    }

    @Override
    public Customer findByEmailCustomer(String email) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Customer c = null;
        try {
            c = em.createQuery("SELECT a FROM Customer a WHERE a.email = :email", Customer.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No customer found with email: " + email);
        }
        return c;
    }

    @Override
    public Customer findByPhoneCustomer(String phone) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Customer c = null;
        try {
            c = em.createQuery("SELECT a FROM Customer a WHERE a.phone = :phone", Customer.class).setParameter("phone", phone).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No customer found with phone: " + phone);
        }
        return c;
    }

    @Override
    public Customer findByNameCustomer(String name) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Customer c = null;
        try {
            c = em.createQuery("SELECT a FROM Customer a WHERE a.name = :name", Customer.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No customer found with name: " + name);
        }
        return c;
    }

    @Override
    public List<Customer> findByNameCustomerList(String name) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        List<Customer> c = null;
        try {
            c = em.createQuery("SELECT a FROM Customer a WHERE a.name = :name order by name desc", Customer.class).setParameter("name", name).getResultList();
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

    @Override
    public boolean updateCustomer(Customer customer) {
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

    @Override
    public boolean deleteByIdCustomer(long id) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Customer c = em.find(Customer.class, id);
        if (em.contains(c)) {
            executeInTransaction(entityManager -> em.remove(c), em);
            return true;
        }
        System.out.println("Customer does not exist with id: " + id);
        return false;
    }

    @Override
    public Customer findNewestCustomer() {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Customer c = null;
        try {
            c = em.createQuery("SELECT a FROM Customer a order by id desc", Customer.class).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No customer found");
        }
        return c;
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
