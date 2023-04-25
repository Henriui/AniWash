package aniwash.entity;

import aniwash.dao.IProductDao;
import aniwash.dao.ProductDao;
import aniwash.datastorage.DatabaseConnector;
import aniwash.entity.localization.LocalizedId;
import aniwash.entity.localization.LocalizedProduct;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductDAO: CRUD testings")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductDaoTest {

    private static IProductDao productDao;

    private final String Nimi = "Pesu pieni";
    private final String Description = "Pienen eläimen pesu";
    private final double Price = 30.00;

    private Product product;

    @BeforeAll
    public static void initAll() {
        DatabaseConnector.openDbConnection("com.aniwash.test");
        productDao = new ProductDao();
    }

    @BeforeEach
    public void setUp() {
        product = new Product(Nimi, Description, Price, "style1");
        LocalizedProduct localizedProduct = new LocalizedProduct(product, Nimi, Description);
        localizedProduct.setId(new LocalizedId("en"));
        product.getLocalizations().put("en", localizedProduct);
    }

    @AfterEach
    public void tearDown() {
        for (Product p : productDao.findAll()) {
            productDao.deleteById(p.getId());
        }
    }

/*
    @AfterAll
    public static void tearDownAll() {
        DatabaseConnector.closeDbConnection();
    }
*/

    @Test
    @Order(1)
    @DisplayName("Add new product")
    public void testAddProduct() {
        assertTrue(productDao.add((product)), "addProduct(): Can't add new product.");
        assertNotNull(product = productDao.findById(product.getId()), "addProduct(): Can't find added product.");
        assertEquals(product.getName("en"), Nimi, "addProduct(): Name of added product does not match.");
        assertEquals(product.getDescription("en"), Description, "addProduct(): Description of added product does not match.");
        assertEquals(product.getPrice(), Price, "addProduct(): Price of added product does not match.");
    }

    @Test
    @Order(2)
    @DisplayName("Try to add same product twice")
    public void testAddSameProductTwice() {
        assertTrue(productDao.add((product)), "addProduct(): Can't add new product.");
        assertFalse(productDao.add((product)), "addProduct(): Can add same product twice.");
    }

    @Test
    @Order(3)
    @DisplayName("Fetch added product by id")
    public void testFindByIdProduct() {
        assertTrue(productDao.add((product)), "addProduct(): Can't add new product.");
        long id = product.getId();
        assertNotNull((product = productDao.findById(id)), "findProductById(): Search for added product failed.");
    }

    @Test
    @Order(4)
    @DisplayName("Fetch added product by name")
    public void testFindByNameProduct() {
        assertTrue(productDao.add((product)), "addProduct(): Can't add new product.");
        String name = product.getName("en");
        assertNotNull((product = productDao.findByName(name)), "findProductByName(): Search for added product failed.");
    }

    @Test
    @Order(5)
    @DisplayName("Fetch all products - one products in database")
    public void testFindAllProduct() {
        assertTrue(productDao.add((product)), "addProduct(): Can't add new product.");
        assertTrue(productDao.findAll().size() > 0, "findAllProduct(): No products found.");
        assertEquals(1, productDao.findAll().size(), "findAllProduct(): Number of products does not match.");
        Product product2 = new Product("Pesu iso", "Ison eläimen pesu", 50.00, "basic");
        assertTrue(productDao.add((product2)), "addProduct(): Can't add new product.");
        assertEquals(2, productDao.findAll().size(), "findAllProduct(): Number of products does not match.");

    }

    @Test
    @Order(6)
    @DisplayName("Fetch all products - no products in database")
    public void testFindAllProductEmpty() {
        productDao.add((product));
        List<Product> products = productDao.findAll();
        for (Product p : products) {
            productDao.deleteById(p.getId());
        }
        assertEquals(0, productDao.findAll().size(), "findAllProduct(): Products found.");
    }

    @Test
    @Order(7)
    @DisplayName("Update name of product")
    public void testUpdateNameProduct() {
        assertTrue(productDao.add((product)), "addProduct(): Can't add new product.");
        String newName = "Pesu iso";
        product.getLocalizations().get("en").setName(newName);
        assertTrue(productDao.update(product), "updateProduct(): Can't update product.");
        assertEquals(newName, productDao.findById(product.getId()).getName("en"), "updateProduct(): Name of product not updated.");
    }

    @Test
    @Order(8)
    @DisplayName("Update description of product")
    public void testUpdateDescriptionProduct() {
        assertTrue(productDao.add((product)), "addProduct(): Can't add new product.");
        String newDescription = "Ison eläimen pesu";
        product.getLocalizations().get("en").setDescription(newDescription);
        assertTrue(productDao.update(product), "updateProduct(): Can't update product.");
        assertEquals(newDescription, productDao.findById(product.getId()).getDescription("en"), "updateProduct(): Description of product not updated.");
    }

    @Test
    @Order(9)
    @DisplayName("Update price of product")
    public void testUpdatePriceProduct() {
        assertTrue(productDao.add((product)), "addProduct(): Can't add new product.");
        double newPrice = 50.00;
        product.setPrice(newPrice);
        assertTrue(productDao.update(product), "updateProduct(): Can't update product.");
        assertEquals(newPrice, productDao.findById(product.getId()).getPrice(), "updateProduct(): Price of product not updated.");
    }

    @Test
    @Order(10)
    @DisplayName("Delete product by id")
    public void testDeleteByIdProduct() {
        assertTrue(productDao.add((product)), "addProduct(): Can't add new product.");
        long id = product.getId();
        assertTrue(productDao.deleteById(id), "deleteProductById(): Can't delete product.");
        assertNull(productDao.findById(id), "deleteProductById(): Product not deleted.");
    }

    @Test
    @Order(11)
    @DisplayName("Attempting to delete non-existing product should return false")
    public void testDeleteNonExistingProduct() {
        assertFalse(productDao.deleteById(999L), "deleteProductById(): Can delete non-existing product.");
    }

    @Test
    @Order(12)
    @DisplayName("Attempting to update non-existing product should return false")
    public void testUpdateNonExistingProduct() {
        assertFalse(productDao.update(product), "updateProduct(): Can update non-existing product.");
    }

    @Test
    @Order(13)
    @DisplayName("Product soft delete")
    public void testSoftDeleteProduct() {
        assertTrue(productDao.add((product)), "addProduct(): Can't add new product.");
        long id = product.getId();
        product.setDeleted();
        assertTrue(productDao.update(product), "softDeleteProduct(): Can't update product");
        assertNotNull(productDao.findById(id), "softDeleteProduct(): Product not found.");
        assertEquals(1, productDao.findById(id).isDeleted(), "softDeleteProduct(): Product not soft deleted.");
    }

    @Test
    @Order(14)
    @DisplayName("Search for nonexistent product should return null")
    public void testSearchNonExistingProduct() {
        assertNull(productDao.findById(999L), "findByIdProduct(): Found non-existing product.");
        assertNull(productDao.findByName("Non-existing product"), "findByNameProduct(): Found non-existing product.");
    }

}
