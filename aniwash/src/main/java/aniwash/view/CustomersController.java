package aniwash.view;

import java.io.IOException;
import java.util.function.Predicate;

import aniwash.MainApp;
import aniwash.entity.Customer;
import aniwash.resources.model.CustomerListViewCell;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class CustomersController {
    @FXML
    private ListView<Customer> listView;
    @FXML
    private Text customerCount;
    private final ObservableList<Customer> customers = FXCollections.observableArrayList(
            new Customer("asd1", "112", "jonne.borgman@metropolia.if", "IsonVillasaarnetie", "00960"),
            new Customer("asd2", "112", "jonne.borgman@metropolia.if", "IsonVillasaarnetie",
                    "00960"),
            new Customer("asd3", "112", "jonne.borgman@metropolia.if", "IsonVillasaarnetie",
                    "00960"),
            new Customer("asd4", "112", "jonne.borgman@metropolia.if", "IsonVillasaarnetie",
                    "00960"),
            new Customer("asd5", "112", "jonne.borgman@metropolia.if", "IsonVillasaarnetie",
                    "00960"),
            new Customer("asd6", "112", "jonne.borgman@metropolia.if", "IsonVillasaarnetie",
                    "00960"),
            new Customer("asd7", "112", "jonne.borgman@metropolia.if", "IsonVillasaarnetie",
                    "00960"),
            new Customer("asd8", "112", "jonne.borgman@metropolia.if", "IsonVillasaarnetie",
                    "00960"),
            new Customer("asd9", "112", "jonne.borgman@metropolia.if", "IsonVillasaarnetie",
                    "00960"),
            new Customer("asd10", "112", "jonne.borgman@metropolia.if", "IsonVillasaarnetie", "00960"));
    @FXML
    private TextField searchField;

    public void initialize() {
        listView.setItems(customers);

        customerCount.setText(String.valueOf(customers.size()));

        // Set the cell factory to create custom ListCells

        listView.setCellFactory(listView -> new CustomerListViewCell());
        listView.setStyle("-fx-background-color:  #f2f5f9; -fx-background:  #f2f5f9;");

        Background background = new Background(
                new BackgroundFill(Color.web("#f2f5f9"), CornerRadii.EMPTY, Insets.EMPTY));
        listView.setPlaceholder(new Label("No items") {
            @Override
            protected void updateBounds() {
                super.updateBounds();
                setBackground(background);
            }
        });

        // Bind the searchField text property to the filter predicate property.

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            Predicate<Customer> filter = customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                } else {
                    return customer.getName().toLowerCase().contains(newValue.toLowerCase());
                }
            };
            ObservableList<Customer> filteredCustomers = customers.filtered(filter);
            listView.setItems(filteredCustomers);

        });
        
        // Bind the customerCount text property to the size of the list

        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Customer selectedCustomer = listView.getSelectionModel().getSelectedItem();
                // Create and show the popup window
                // Pass the selected customer object to the popup window to display its info
                System.out.println(selectedCustomer.getName());
            }
        });

    }
    
    @FXML
    private void mySchedule() throws IOException {
        MainApp.setRoot("schedule");
    }

    @FXML
    private void dashBoard() throws IOException {
        MainApp.setRoot("mainView");
    }
}