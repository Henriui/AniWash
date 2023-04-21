package aniwash.view.controllers;

import aniwash.MainApp;
import aniwash.dao.IProductDao;
import aniwash.dao.ProductDao;
import aniwash.entity.Product;
import aniwash.view.model.CustomListViewCellProduct;
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

public class ProductController {

    @FXML
    private ListView<Product> listView;
    @FXML
    private TextField searchField;
    @FXML
    private Button newProduct;
    private static Product selectedProduct;

    private IProductDao productDao;

    public void initialize() {

        productDao = new ProductDao();
        AtomicReference<ObservableList<Product>> products = new AtomicReference<>(FXCollections.observableList(productDao.findAllProduct()));
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
                    return product.getLocalizations().get("en").getName().toLowerCase().contains(newValue.toLowerCase());
                }
            };
            ObservableList<Product> filteredCustomers = products.get().filtered(productFilter);
            listView.setItems(filteredCustomers);

        });

        // Double click on a customer to open the customer info popup window

       /*  listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                selectedProduct = listView.getSelectionModel().getSelectedItem();
                // Create and show the popup window
                // Pass the selected customer object to the popup window to display its info
                final FXMLLoader loader;
                final Scene scene;
                try {
                    loader = ControllerUtilities.loadFXML("");
                    scene = new Scene((Parent) loader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Edit Product");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.show();

                    // TODO: Should this only be done if change is made?
                    stage.setOnHidden(view -> listView.setItems(FXCollections.observableList(productDao.findAllProduct())));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }); */

    }

    @FXML
    public void newProduct() throws IOException {
        Stage stage = new Stage();
        stage.setOnHidden(event -> listView.setItems(FXCollections.observableList(productDao.findAllProduct())));
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
