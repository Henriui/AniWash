package aniwash.dao;

import aniwash.entity.Animal;
import aniwash.entity.Customer;
import jakarta.persistence.*;

import java.util.List;

/*
 * This class is used to access the database and perform CRUD operations on the Customer table.
 * @author rasmushy
 */
public class CustomerDao implements ICustomerDao {
    private final EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();

    @Override
    public boolean addCustomer(Customer customer) {
        boolean success = true;
        em.getTransaction().begin();
        Customer c = em.find(Customer.class, customer.getId());
        if (c != null) {
            System.out.println("Customer already exists: " + customer.getId());
            success = false;
        } else {
            em.persist(customer);
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
    public Customer findByIdCustomer(long id) {
        em.getTransaction().begin();
        Customer c = em.find(Customer.class, id);
        em.getTransaction().commit();
        return c;
    }

    @Override
    public Customer findByEmailCustomer(String email) {
        Customer c = null;
        em.getTransaction().begin();
        try {
            c = em.createQuery("SELECT a FROM Customer a WHERE a.email = :email", Customer.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No customer found with email: " + email);
        }
        em.getTransaction().commit();
        return c;
    }

    @Override
    public Customer findByPhoneCustomer(String phone) {
        Customer c = null;
        em.getTransaction().begin();
        try {
            c = em.createQuery("SELECT a FROM Customer a WHERE a.phone = :phone", Customer.class).setParameter("phone", phone).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No customer found with phone: " + phone);
        }
        em.getTransaction().commit();
        return c;
    }

    @Override
    public Customer findByNameCustomer(String name) {
        Customer c = null;
        em.getTransaction().begin();
        try {
            c = em.createQuery("SELECT a FROM Customer a WHERE a.name = :name", Customer.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No customer found with name: " + name);
        }
        em.getTransaction().commit();
        return c;
    }

    @Override
    public List<Customer> findByNameCustomerList(String name) {
        List<Customer> c = null;
        em.getTransaction().begin();
        c = em.createQuery("SELECT a FROM Customer a WHERE a.name = :name order by name desc", Customer.class).setParameter("name", name).getResultList();
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
    public boolean deleteByIdCustomer(long id) {
        boolean deleted = false;
        em.getTransaction().begin();
        Customer t = em.find(Customer.class, id);
        if (t != null) {
            for (Animal a : t.getAnimals()) {
                t.removeAnimal(a);
                if (a.getOwner().size() == 0) {
                    em.remove(a);
                    System.out.println("Animal " + a.getName() + " deleted");
                }
            }
            em.remove(t);
            deleted = true;
        }
        em.getTransaction().commit();
        return deleted;
    }

}

