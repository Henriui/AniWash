package aniwash.dao;

import aniwash.entity.Animal;
import aniwash.entity.Customer;

import java.util.List;
import java.util.Set;

public interface ICustomerDao {
    boolean addCustomer(Customer customer);

    List<Customer> findAllCustomer();

    Customer findByIdCustomer(long id);

    boolean deleteByIdCustomer(long id);

    Customer findByEmailCustomer(String email);

    Customer findByPhoneCustomer(String phone);

    Customer findByNameCustomer(String name);

    List<Customer> findByNameCustomerList(String name);

    boolean updateCustomer(Customer customer);

}
