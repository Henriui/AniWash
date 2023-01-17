package aniwash.model;

public abstract class AbstractUser {

    public abstract String getName();
    public abstract int getId();
    public abstract String getPhone();
    public abstract String getEmail();
    public abstract String getAddress();
    public abstract String getPostalcode();
    public abstract void setName(String name);
    public abstract void setId(int id);
    public abstract void setPhone(String phone);
    public abstract void setEmail(String email);
    public abstract void setAddress(String address);
    public abstract void setPostalcode(String postalcode);

}
