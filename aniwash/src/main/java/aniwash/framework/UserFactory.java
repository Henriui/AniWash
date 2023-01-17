package aniwash.framework;
import aniwash.model.*;

public class UserFactory {
    
    public AbstractUser createCustomer(){
        // TODO Auto-generated method stub
        return new Customer();
    }

    public AbstractUser createEmployee(){
        // TODO Auto-generated method stub
        return new Employee();
    }

    public AbstractUser createEmployer(){
        // TODO Auto-generated method stub
        return new Employer();
    }
}
