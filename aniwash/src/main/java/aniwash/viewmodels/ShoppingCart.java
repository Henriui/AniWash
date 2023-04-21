package aniwash.viewmodels;

import aniwash.entity.Product;

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

    public void editDiscountString(String product, String discount) {
        for (Map.Entry<Product, String> entry : productsMap.entrySet()) {
            if (entry.getKey().getName("en") == product) {
                System.out.println("herehere");
                productsMap.put(entry.getKey(), discount);
            }
        }
    }

    public Product getProduct(String product) {
        for (Map.Entry<Product, String> entry : productsMap.entrySet()) {
            if (entry.getKey().getName("en") == product) {
                System.out.println("herehere");
                return entry.getKey();
            }

        }
        return null;
    }

    public void editDiscount(Product product, String discount) {
        productsMap.put(product, discount);
    }

    public void removeMainProduct(Product mainProduct) {
        productsMap.remove(mainProduct);
        // FOR TESTING PUPROSES getSelectedProducts();
    }

    public Product removeProduct(Product product) {
        productsMap.remove(product);
        return product;
    }

    public void removeProductString(String product) {
        for (Map.Entry<Product, String> entry : productsMap.entrySet()) {
            if (entry.getKey().getName("en") == product) {
                productsMap.remove(entry.getKey());
                break;
            }
        }
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
            totalDiscountedPrice += product.getPrice() - (product.getPrice() * (0.01 * Double.parseDouble(discount)));
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
            System.out.println(product.getName("en") + " " + discount);
        }
    }

}
