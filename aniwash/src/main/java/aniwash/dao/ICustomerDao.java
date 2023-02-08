package aniwash.dao;

import aniwash.entity.Customer;

import java.util.List;

public interface ICustomerDao {
    boolean addCustomer(Customer customer);

    List<Customer> findAllCustomer();

    Customer findByIdCustomer(long id);

    boolean deleteByIdCustomer(long id);

    boolean updateCustomer(Customer customer);
}
