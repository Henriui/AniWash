package aniwash;

import aniwash.framework.*;
import aniwash.model.*;

public class Test {

    public static void main( String[] args )
    {
        UserFactory userFactory = new UserFactory();
        AbstractUser customer = userFactory.createCustomer();
        customer.setName("John");
        System.out.println(customer.getName());
        
    }
}
