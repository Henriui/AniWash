package aniwash.framework;
import aniwash.model.*;

public class UserFactory {
    
    public IUser createCustomer(){
        // TODO Auto-generated method stub
        return new Customer();
    }

    public IUser createEmployee(){
        // TODO Auto-generated method stub
        return new Employee();
    }

    public IUser createEmployer(){
        // TODO Auto-generated method stub
        return new Employer();
    }
}
