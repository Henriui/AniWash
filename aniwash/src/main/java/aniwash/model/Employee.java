package aniwash.model;

public class Employee extends AbstractUser{

    private String name;
    private int id;
    private String phone;
    private String email;
    private String address;
    private String postalcode;

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
