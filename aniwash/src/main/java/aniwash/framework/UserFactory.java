package aniwash.framework;
import aniwash.model.*;

public class UserFactory {
    
    public IUser createCustomer(){
        return new Customer();
    }

    public IUser createCustomer(String name, int id){
        return new Customer(name, id);
    }

    public IUser createCustomer(String name, int id, String phone, String email){
        return new Customer(name, id, phone, email);
    }

    public IUser createCustomer(String name, int id, String phone, String email, String address, String postalcode){
        return new Customer(name, id, phone, email, address, postalcode);
    }

    public IUser createEmployee(){
        return new Employee();
    }

    public IUser createEmployee(String name, int id){
        return new Employee(name, id);
    }

    public IUser createEmployee(String name, int id, String phone, String email){
        return new Employee(name, id, phone, email);
    }

    public IUser createEmployee(String name, int id, String phone, String email, String address, String postalcode){
        return new Employee(name, id, phone, email, address, postalcode);
    }

    public IUser createEmployer(){
        return new Employer();
    }

    public IUser createEmployer(String name, int id){
        return new Employer(name, id);
    }

    public IUser createEmployer(String name, int id, String phone, String email){
        return new Employer(name, id, phone, email);
    }

    public IUser createEmployer(String name, int id, String phone, String email, String address, String postalcode){
        return new Employer(name, id, phone, email, address, postalcode);
    }
}
