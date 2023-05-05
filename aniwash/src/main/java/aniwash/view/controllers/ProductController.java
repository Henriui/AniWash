package aniwash.view.controllers;

import aniwash.MainApp;
import aniwash.dao.IProductDao;
import aniwash.dao.ProductDao;
import aniwash.entity.Product;
import aniwash.view.elements.CustomListViewCellProduct;
import aniwash.view.utilities.ControllerUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

/**
 * The ProductController class controls the behavior of a ListView of products and allows for searching
 * and adding new products.
 */
public class ProductController {

    @FXML
    private ListView<Product> listView;
    @FXML
    private TextField searchField;
    @FXML
    private Button newProduct;
    private static Product selectedProduct;

    private IProductDao productDao;

    /**
     * This function initializes a ListView of products with a custom cell factory and a search filter.
     */
    public void initialize() {

        productDao = new ProductDao();
        AtomicReference<ObservableList<Product>> products = new AtomicReference<>(FXCollections.observableList(productDao.findAll()));
        listView.setItems(products.get());

        // Set the cell factory to create custom ListCells

        listView.setCellFactory(listView -> new CustomListViewCellProduct());
        listView.setStyle("-fx-background-color:  #f2f5f9; -fx-background:  #f2f5f9;");

        // Set the placeholder text for the ListView

        Background background = new Background(new BackgroundFill(Color.web("#f2f5f9"), CornerRadii.EMPTY, Insets.EMPTY));
        listView.setPlaceholder(new Label("No products") {
            @Override
            protected void updateBounds() {
                super.updateBounds();
                setBackground(background);
            }
        });

        // Bind the searchField text property to the filter predicate property.

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            Predicate<Product> productFilter = product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                } else {
                    return product.getLocalizations().get(MainApp.getLocale().getLanguage()).getName().toLowerCase().contains(newValue.toLowerCase());
                }
            };
            ObservableList<Product> filteredCustomers = products.get().filtered(productFilter);
            listView.setItems(filteredCustomers);

        });

    }

    @FXML
    public void newProduct() throws IOException {
        Stage stage = new Stage();
        stage.setOnHidden(event -> listView.setItems(FXCollections.observableList(productDao.findAll())));
        ControllerUtilities.newProduct(stage);
    }

    public Product getSelectedCustomer() {
        return selectedProduct;
    }

    @FXML
    private void mySchedule() throws IOException {
        MainApp.setRoot("schedule");
    }

    @FXML
    private void dashBoard() throws IOException {
        MainApp.setRoot("mainView");
    }

    @FXML
    private void customers() throws IOException {
        MainApp.setRoot("customerView");
    }

    @FXML
    private void admin() throws IOException {
        MainApp.setRoot("AdminPanel");
    }

}
