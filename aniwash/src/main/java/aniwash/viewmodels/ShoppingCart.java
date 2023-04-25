package aniwash.viewmodels;

import aniwash.entity.Discount;
import aniwash.entity.Product;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {

    private Map<Product, Discount> productsMap;

    public ShoppingCart() {
        this.productsMap = new HashMap();
    }

    public void addProduct(Product product, Discount discount) {
        System.out.println("!!!!!!!!!!!!!!!!!!?!?!?!?!?!?" + product.getName("en"));
        productsMap.put(product, discount);
    }

    public void editDiscountString(String product, Discount discount) {
        for (Map.Entry<Product, Discount> entry : productsMap.entrySet()) {
            if (entry.getKey().getName("en") == product) {
                productsMap.put(entry.getKey(), discount);
            }
        }
    }

    public Product getProduct(String product) {
        for (Map.Entry<Product, Discount> entry : productsMap.entrySet()) {
            if (entry.getKey().getName("en") == product) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void editDiscount(Product product, Discount discount) {
        productsMap.put(product, discount);
    }

    public Product removeProduct(Product product) {
        productsMap.remove(product);
        return product;
    }

    public void removeProductString(String product) {
        for (Map.Entry<Product, Discount> entry : productsMap.entrySet()) {
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
        for (Map.Entry<Product, Discount> entry : productsMap.entrySet()) {
            Product product = entry.getKey();
            Discount discount = entry.getValue();
            totalDiscountedPrice += product.getPrice() - (product.getPrice() * (0.01 * discount.getDiscountPercent()));
        }
        return totalDiscountedPrice;
    }

    public Discount getDiscount(Product product) {
        return productsMap.get(product);
    }

    public void getSelectedProducts() {
        // TODO: return a list of selected products
        // FIXME: now it only prints the products for Jonnes testing purposes.
        for (Map.Entry<Product, Discount> entry : productsMap.entrySet()) {
            System.out.println(entry.getKey().getName("en"));
        }
    }

    public Map<Product, Discount> getProductList() {
        return productsMap;
    }

}
