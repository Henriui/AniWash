package aniwash.entity;

public class DiscountProduct {
    private double price;
    private String name;

    public DiscountProduct(String name, double price) {
        this.price = price;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setPrice(double newPrice) {
        this.price = newPrice;
    }
}
