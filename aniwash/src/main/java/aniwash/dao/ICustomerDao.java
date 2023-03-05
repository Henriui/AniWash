package aniwash.dao;

import aniwash.entity.Customer;

import java.util.List;

public interface ICustomerDao {
    boolean addCustomer(Customer customer);

    List<Customer> findAllCustomer();

    Customer findByIdCustomer(long id);

    Customer findByEmailCustomer(String email);

    Customer findByPhoneCustomer(String phone);

    Customer findByNameCustomer(String name);

    List<Customer> findByNameCustomerList(String name);

    boolean updateCustomer(Customer customer);

    boolean deleteByIdCustomer(long id);

    Customer findNewestCustomer();
}
