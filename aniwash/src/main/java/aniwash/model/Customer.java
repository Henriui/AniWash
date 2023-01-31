package aniwash.model;

public class Customer extends User {
    
    // Use constructor for database connection

    public Customer(String name, int id, String phone, String email, String address, String postalcode) {
        super(name, id, phone, email, address, postalcode);
    }
}
