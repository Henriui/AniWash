package aniwash.dao;

import aniwash.entity.Customer;
import jakarta.persistence.*;

import java.util.List;

/*
 * This class is used to access the database and perform CRUD operations on the Customer table.
 * @author rasmushy
 */
public class CustomerDao implements ICustomerDao {
    private EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();

    @Override
    public boolean addCustomer(Customer customer) {
        boolean success = true;
        em.getTransaction().begin();
        Customer c = em.find(Customer.class, customer.getId());
        if (c != null) {
            System.out.println("Customer already exists: " + customer.getId());
            success = false;
        } else {
            em.merge(customer);
        }
        em.getTransaction().commit();
        return success;
    }


    @Override
    public List<Customer> findAllCustomer() {
        em.getTransaction().begin();
        List<Customer> customers = em.createQuery("SELECT a FROM Customer a", Customer.class).getResultList();
        em.getTransaction().commit();
        return customers;
    }

    @Override
    public Customer findByIdCustomer(Customer customer) {
        em.getTransaction().begin();
        Customer c = em.find(Customer.class, customer.getId());
        em.getTransaction().commit();
        return c;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        em.getTransaction().begin();
        Customer c = em.find(Customer.class, customer.getId());
        if (c == null) {
            em.getTransaction().commit();
            return false;
        }
        c.setName(customer.getName());
        c.setEmail(customer.getEmail());
        c.setAddress(customer.getAddress());
        c.setPhone(customer.getPhone());
        c.setPostalcode(customer.getPostalcode());
        em.getTransaction().commit();
        return true;
    }

    @Override
    public boolean deleteByIdCustomer(int id) {
        boolean deleted = false;
        em.getTransaction().begin();
        Customer t = em.find(Customer.class, id);
        if (t != null) {
            em.remove(t);
            deleted = true;
        }
        em.getTransaction().commit();
        return deleted;
    }

}

