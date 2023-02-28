package aniwash.view;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.function.Predicate;

import aniwash.MainApp;
import aniwash.entity.Animal;
import aniwash.entity.Appointment;
import aniwash.entity.Customer;
import aniwash.resources.model.CustomerListViewCell;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.Parent;
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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomersController {
    @FXML
    private ListView<Customer> listView;
    @FXML
    private Text customerCount;
    @FXML
    private Button newCustomer;
    private static Customer selectedCustomer;

    private static ObservableList<Customer> customers = FXCollections.observableArrayList(
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

    public void test() {
        for (Customer customer : customers) {
            customer.addAnimal(new Animal("Testi111", "Eläin", "TestiEläin", 10, "Tämä eläin on testi"));
            customer.addAppointment(new Appointment(ZonedDateTime.now(), "Cancer Treatment"));
    
        }
    }

    public void initialize() {
        test();
        // Bind the ListView to the ObservableList

        listView.setItems(customers);

        // Bind the customerCount text property to the size of the list

        customerCount.setText(String.valueOf(customers.size()));

        // Set the cell factory to create custom ListCells

        listView.setCellFactory(listView -> new CustomerListViewCell());
        listView.setStyle("-fx-background-color:  #f2f5f9; -fx-background:  #f2f5f9;");

        // Set the placeholder text for the ListView

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
    public void newCustomer() throws IOException {
        final FXMLLoader loader;
        final Scene scene;

        loader = loadFXML("newCustomerView");
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

    public Customer getSelectedCustomer() {
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