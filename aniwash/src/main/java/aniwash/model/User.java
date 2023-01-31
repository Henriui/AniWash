package aniwash.model;

public abstract class User {

    // Attributes
    private String name;
    private int id;
    private String phone;
    private String email;
    private String address;
    private String postalcode;

    public User(String name, int id, String phone, String email, String address, String postalcode) {
        this.name = name;
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.postalcode = postalcode;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }
}
