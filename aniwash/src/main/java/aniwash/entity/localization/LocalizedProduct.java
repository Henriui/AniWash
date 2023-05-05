package aniwash.entity.localization;

import aniwash.entity.Product;
import jakarta.persistence.*;

/**
 * The LocalizedProduct class represents a product with localized name and description.
 */
@Entity
public class LocalizedProduct {

    @EmbeddedId
    private LocalizedId localizedId;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    public LocalizedProduct() {
    }

    public LocalizedProduct(Product product, String name, String description) {
        this.product = product;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return localizedId.getLocale();
    }

    public void setId(LocalizedId localizedId) {
        this.localizedId = localizedId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
