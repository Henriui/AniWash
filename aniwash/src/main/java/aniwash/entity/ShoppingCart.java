package aniwash.entity;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {

    private Map<Product, String> productsMap;

    public ShoppingCart() {
        this.productsMap = new HashMap();
    }

    public void addProduct(Product product, String discount) {
        productsMap.put(product, discount);
    }

    public void editDiscount(Product product, double percentage) {
        String discount = productsMap.get(product);
        if (discount != null) {
            discount = Integer.toString((int) (percentage * 100)) + "%";
        }
    }

    public Product removeProduct(Product product) {
        productsMap.remove(product);
        return product;
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (Product product : productsMap.keySet()) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }

    public double getTotalDiscountedPrice() {
        double totalDiscountedPrice = 0;
        for (Map.Entry<Product, String> entry : productsMap.entrySet()) {
            Product product = entry.getKey();
            String discount = entry.getValue();
            totalDiscountedPrice += product.getPrice() * Double.parseDouble(discount);
        }
        return totalDiscountedPrice;
    }

    public String getDiscount(Product product) {
        return productsMap.get(product);
    }

    public void getSelectedProducts() {
        // TODO: return a list of selected products
        // FIXME: now it only prints the products for Jonnes testing purposes.
        for (Map.Entry<Product, String> entry : productsMap.entrySet()) {
            Product product = entry.getKey();
            String discount = entry.getValue();
            System.out.println(product.getName() + " " + discount);
        }
    }

}