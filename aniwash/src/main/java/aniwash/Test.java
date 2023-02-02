package aniwash;

import aniwash.entity.*;
import aniwash.framework.*;

public class Test {

    public static void main(String[] args) {
        UserFactory userFactory = new UserFactory();
        User customer = userFactory.createCustomer("x", 1, "s", "s");
        customer.setName("John");
        System.out.println(customer.getName());
    }
}
