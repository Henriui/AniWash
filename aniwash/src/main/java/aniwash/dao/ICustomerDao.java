package aniwash.dao;

import aniwash.entity.Customer;

import java.util.List;

public interface ICustomerDao {
    boolean addCustomer(Customer customer);

    List<Customer> findAllCustomer();

    Customer findByIdCustomer(Customer customer);

    boolean deleteByIdCustomer(int id);

    boolean updateCustomer(Customer customer);
}
