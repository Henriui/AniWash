package aniwash;

import aniwash.Entity.*;
import aniwash.framework.*;

public class Test {

    public static void main( String[] args )
    {
        UserFactory userFactory = new UserFactory();
        User customer = userFactory.createCustomer("x",1,"s","s","s","x");
        customer.setName("John");
        System.out.println(customer.getName());
        
    }
}
