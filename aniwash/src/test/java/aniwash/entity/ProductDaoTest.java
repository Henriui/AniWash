package aniwash.entity;

import aniwash.dao.IProductDao;
import aniwash.dao.ProductDao;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductDAO: CRUD testings")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductDaoTest {
    private final IProductDao productDao = new ProductDao();

    private final String Nimi = "Pesu pieni";
    private final String Description = "Pienen eläimen pesu";
    private final double Price = 30.00;

    private Product product = new Product(Nimi, Description, Price);

    @BeforeEach
    public void setUp() {
        product = new Product(Nimi, Description, Price);
    }

    @AfterEach
    public void tearDown() {
        productDao.deleteByIdProduct(product.getId());
    }

    @Test
    @Order(1)
    @DisplayName("Add new product")
    public void testAddProduct() {
        assertTrue(productDao.addProduct((product)), "addProduct(): Can't add new product.");
        assertNotNull(product = productDao.findByIdProduct(product.getId()), "addProduct(): Can't find added product.");
        assertEquals(product.getName(), Nimi, "addProduct(): Name of added product does not match.");
        assertEquals(product.getDescription(), Description, "addProduct(): Description of added product does not match.");
        assertEquals(product.getPrice(), Price, "addProduct(): Price of added product does not match.");
    }

    @Test
    @Order(2)
    @DisplayName("Try to add same product twice")
    public void testAddSameProductTwice() {
        assertTrue(productDao.addProduct((product)), "addProduct(): Can't add new product.");
        assertFalse(productDao.addProduct((product)), "addProduct(): Can add same product twice.");
    }

    @Test
    @Order(3)
    @DisplayName("Fetch added product by id")
    public void testFindByIdProduct() {
        assertTrue(productDao.addProduct((product)), "addProduct(): Can't add new product.");
        long id = product.getId();
        assertNotNull((product = productDao.findByIdProduct(id)), "findProductById(): Search for added product failed.");
    }

    @Test
    @Order(4)
    @DisplayName("Fetch added product by name")
    public void testFindByNameProduct() {
        assertTrue(productDao.addProduct((product)), "addProduct(): Can't add new product.");
        String name = product.getName();
        assertNotNull((product = productDao.findByNameProduct(name)), "findProductByName(): Search for added product failed.");
    }

    @Test
    @Order(5)
    @DisplayName("Fetch all products - one products in database")
    public void testFindAllProduct() {
        assertTrue(productDao.addProduct((product)), "addProduct(): Can't add new product.");
        assertTrue(productDao.findAllProduct().size() > 0, "findAllProduct(): No products found.");
        assertTrue(productDao.findAllProduct().size() == 1, "findAllProduct(): Number of products does not match.");
        Product product2 = new Product("Pesu iso", "Ison eläimen pesu", 50.00);
        assertTrue(productDao.addProduct((product2)), "addProduct(): Can't add new product.");
        assertTrue(productDao.findAllProduct().size() == 2, "findAllProduct(): Number of products does not match.");

    }

    @Test
    @Order(6)
    @DisplayName("Fetch all products - no products in database")
    public void testFindAllProductEmpty() {
        productDao.addProduct((product));
        List<Product> products = productDao.findAllProduct();
        for (Product p : products) {
            productDao.deleteByIdProduct(p.getId());
        }
        assertTrue(productDao.findAllProduct().size() == 0, "findAllProduct(): Products found.");
    }

    @Test
    @Order(7)
    @DisplayName("Update name of product")
    public void testUpdateNameProduct() {
        assertTrue(productDao.addProduct((product)), "addProduct(): Can't add new product.");
        String newName = "Pesu iso";
        product.setName(newName);
        assertTrue(productDao.updateProduct(product), "updateProduct(): Can't update product.");
        assertEquals(newName, productDao.findByIdProduct(product.getId()).getName(), "updateProduct(): Name of product not updated.");
    }

    @Test
    @Order(8)
    @DisplayName("Update description of product")
    public void testUpdateDescriptionProduct() {
        assertTrue(productDao.addProduct((product)), "addProduct(): Can't add new product.");
        String newDescription = "Ison eläimen pesu";
        product.setDescription(newDescription);
        assertTrue(productDao.updateProduct(product), "updateProduct(): Can't update product.");
        assertEquals(newDescription, productDao.findByIdProduct(product.getId()).getDescription(), "updateProduct(): Description of product not updated.");
    }

    @Test
    @Order(9)
    @DisplayName("Update price of product")
    public void testUpdatePriceProduct() {
        assertTrue(productDao.addProduct((product)), "addProduct(): Can't add new product.");
        double newPrice = 50.00;
        product.setPrice(newPrice);
        assertTrue(productDao.updateProduct(product), "updateProduct(): Can't update product.");
        assertEquals(newPrice, productDao.findByIdProduct(product.getId()).getPrice(), "updateProduct(): Price of product not updated.");
    }

    @Test
    @Order(10)
    @DisplayName("Delete product by id")
    public void testDeleteByIdProduct() {
        assertTrue(productDao.addProduct((product)), "addProduct(): Can't add new product.");
        long id = product.getId();
        assertTrue(productDao.deleteByIdProduct(id), "deleteProductById(): Can't delete product.");
        assertNull(productDao.findByIdProduct(id), "deleteProductById(): Product not deleted.");
    }

    @Test
    @Order(11)
    @DisplayName("Attempting to delete non-existing product should return false")
    public void testDeleteNonExistingProduct() {
        assertFalse(productDao.deleteByIdProduct(999L), "deleteProductById(): Can delete non-existing product.");
    }
}
