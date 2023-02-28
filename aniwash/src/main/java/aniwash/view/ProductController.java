package aniwash.view;

import java.io.IOException;
import java.util.function.Predicate;

import aniwash.MainApp;
import aniwash.entity.Product;
import aniwash.resources.model.CustomerListViewCellProduct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProductController {
    @FXML
    private ListView<Product> listView;
    private static ObservableList<Product> customers = FXCollections.observableArrayList(
            new Product("asd1", "112", 10), new Product("asd2", "112", 10), new Product("asd3", "112", 10),
            new Product("asd4", "112", 10), new Product("asd5", "112", 10), new Product("asd6", "112", 10),
            new Product("asd7", "112", 10), new Product("asd8", "112", 10), new Product("asd9", "112", 10),
            new Product("asd10", "112", 10), new Product("asd11", "112", 10), new Product("asd12", "112", 10),
            new Product("asd13", "112", 10), new Product("asd14", "112", 10), new Product("asd15", "112", 10),
            new Product("asd16", "112", 10), new Product("asd17", "112", 10), new Product("asd18", "112", 10),
            new Product("asd19", "112", 10), new Product("asd20", "112", 10), new Product("asd21", "112", 10),
            new Product("asd22", "112", 10), new Product("asd23", "112", 10), new Product("asd24", "112", 10),
            new Product("asd25", "112", 10), new Product("asd26", "112", 10), new Product("asd27", "112", 10),
            new Product("asd28", "112", 10), new Product("asd29", "112", 10), new Product("asd30", "112", 10),
            new Product("asd31", "112", 10), new Product("asd32", "112", 10), new Product("asd33", "112", 10),
            new Product("asd34", "112", 10), new Product("asd35", "112", 10), new Product("asd36", "112", 10),
            new Product("asd37", "112", 10), new Product("asd38", "112", 10), new Product("asd39", "112", 10),
            new Product("asd40", "112", 10), new Product("asd41", "112", 10), new Product("asd42", "112", 10),
            new Product("asd43", "112", 10), new Product("asd44", "112", 10));
    @FXML
    private TextField searchField;
    @FXML
    private Button newProduct;
    private static Product selectedCustomer;

    public void initialize() {

        listView.setItems(customers);

        // Set the cell factory to create custom ListCells

        listView.setCellFactory(listView -> new CustomerListViewCellProduct());
        listView.setStyle("-fx-background-color:  #f2f5f9; -fx-background:  #f2f5f9;");

        // Set the placeholder text for the ListView

        Background background = new Background(
                new BackgroundFill(Color.web("#f2f5f9"), CornerRadii.EMPTY, Insets.EMPTY));
        listView.setPlaceholder(new Label("No products") {
            @Override
            protected void updateBounds() {
                super.updateBounds();
                setBackground(background);
            }
        });

        // Bind the searchField text property to the filter predicate property.

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            Predicate<Product> filter = customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                } else {
                    return customer.getName().toLowerCase().contains(newValue.toLowerCase());
                }
            };
            ObservableList<Product> filteredCustomers = customers.filtered(filter);
            listView.setItems(filteredCustomers);

        });

        // Double click on a customer to open the customer info popup window

        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                selectedCustomer = listView.getSelectionModel().getSelectedItem();
                // Create and show the popup window
                // Pass the selected customer object to the popup window to display its info
                final FXMLLoader loader;
                final Scene scene;
                try {
                    loader = loadFXML("editCustomerView");
                    scene = new Scene((Parent) loader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Edit Customer");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.show();
                    stage.setOnHidden(view -> {
                        // TODO: Get customers from database so the listview reloads
                    });
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

    }

    @FXML
    public void newProduct() throws IOException {
        final FXMLLoader loader;
        final Scene scene;

        loader = loadFXML("newProductView");
        scene = new Scene((Parent) loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Create Customer");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        stage.setOnHidden(event -> {
            // TODO: Get customers from database so the listview reloads
        });
    }

    public Product getSelectedCustomer() {
        return selectedCustomer;
    }

    @FXML
    private void mySchedule() throws IOException {
        MainApp.setRoot("schedule");
    }

    @FXML
    private void dashBoard() throws IOException {
        MainApp.setRoot("mainView");
    }

    private static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/" + fxml + ".fxml"));
        return fxmlLoader;
    }
}
