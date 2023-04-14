package aniwash.entity;

public class DiscountProduct {
    private double price;
    private String name;
    private String description;
    private String style;
    public DiscountProduct(String name, String description, double price, String style){
        this.price = price;
        this.name = name;
        this.description = description;
        this.style = style;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public String getStyle(){
        return this.style;
    }

    public double getPrice(){
        return this.price;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public void setPrice(double newPrice){
        this.price = newPrice;
    }
}
