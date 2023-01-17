package aniwash.model;

public interface IUser {

    public String getName();
    public int getId();
    public String getPhone();
    public String getEmail();
    public String getAddress();
    public String getPostalcode();
    public void setName(String name);
    public void setId(int id);
    public void setPhone(String phone);
    public void setEmail(String email);
    public void setAddress(String address);
    public void setPostalcode(String postalcode);

}
