package aniwash.framework;

import aniwash.entity.*;

public class UserFactory {

    public User createCustomer(String name, int id, String phone, String email) {
        return new Customer(name, id, phone, email);
    }

    public User createEmployee(String name, int id, String phone, String email) {
        return new Employee(name, id, phone, email);
    }

    public User createEmployer(String name, int id, String phone, String email) {
        return new Employer(name, id, phone, email);
    }
}
