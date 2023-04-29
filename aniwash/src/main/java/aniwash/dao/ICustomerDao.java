package aniwash.dao;

import aniwash.entity.Customer;

import java.util.List;

public interface ICustomerDao extends IDao<Customer> {

    List<Customer> findByNameList(String name);
    Customer findByName(String name);

    Customer findByEmail(String email);

    Customer findByPhone(String phone);

}
