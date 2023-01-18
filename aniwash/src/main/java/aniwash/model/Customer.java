package aniwash.model;

public class Customer implements IUser {

    private String name;
    private int id;
    private String phone;
    private String email;
    private String address;
    private String postalcode;

    public Customer() {
    }

    public Customer(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Customer(String name, int id, String phone, String email) {
        this.name = name;
        this.id = id;
        this.phone = phone;
        this.email = email;
    }

    public Customer(String name, int id, String phone, String email, String address, String postalcode) {
        this.name = name;
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.postalcode = postalcode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getPostalcode() {
        return postalcode;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }
}