package aniwash;

import aniwash.entity.*;

public class Test {

    public static void main( String[] args )
    {
        Customer customer = new Customer("x",1,"s","s","s","x");
        customer.setName("John");
        Animal animal = new Animal("Räyhä-roope","Koera","Lapinkoera","Haukkuu ihan vitusti");
        customer.addAnimal(animal);
        System.out.println(customer.getName());
        animal.getOwner().forEach((Customer c) -> {
            System.out.println(c.getName());
        });
    }
}
