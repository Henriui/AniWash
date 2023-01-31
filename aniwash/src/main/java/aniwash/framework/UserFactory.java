package aniwash.framework;
import aniwash.Entity.*;

public class UserFactory {

    public User createCustomer(String name, int id, String phone, String email, String address, String postalcode){
        return new Customer(name, id, phone, email, address, postalcode);
    }

    public User createEmployee(String name, int id, String phone, String email, String address, String postalcode){
        return new Employee(name, id, phone, email, address, postalcode);
    }

    public User createEmployer(String name, int id, String phone, String email, String address, String postalcode){
        return new Employer(name, id, phone, email, address, postalcode);
    }
}
