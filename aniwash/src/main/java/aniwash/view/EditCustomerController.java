package aniwash.view;

import java.io.IOException;

import aniwash.entity.Animal;
import aniwash.entity.Customer;
import aniwash.resources.model.CustomerListViewCellAnimal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class EditCustomerController {
    // Create text fields for Customer section
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField postalCodeField;

    // Create text fields for Pets section
    @FXML
    private TextField petNameField;
    @FXML
    private TextField petTypeField;
    @FXML
    private TextField petBreedField;
    @FXML
    private TextField petAgeField;
    @FXML
    private TextField petDescriptionField;

    @FXML
    private Button saveButton;
    private final ObservableList<Animal> animals = FXCollections.observableArrayList();
    @FXML
    private ListView<Animal> listView;
    private static Customer customer;
    private CustomersController customersController = new CustomersController();

    public void initialize() {
        customer = customersController.getSelectedCustomer();
        animals.addAll(customer.getAnimals());

        listView.setItems(animals);
        listView.setCellFactory(listView -> new CustomerListViewCellAnimal());
        listView.setStyle("-fx-background-color:  #f2f5f9; -fx-background:  #f2f5f9;");

        nameField.setText(customer.getName());
        phoneField.setText(customer.getPhone());
        emailField.setText(customer.getEmail());
        addressField.setText(customer.getAddress());
        postalCodeField.setText(customer.getPostalcode());

        // Set the text fields to be editable
        saveButton.disableProperty().bind(
                // Bind the save button's disable property to a BooleanBinding
                // that checks if all mandatory fields have been filled
                nameField.textProperty().isEmpty()
                        .or(phoneField.textProperty().isEmpty())
                        .or(emailField.textProperty().isEmpty()));
    }

    @FXML
    public void onSaveButtonClicked(ActionEvent event) {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();
        String postalCode = postalCodeField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            // Show error message if mandatory fields are empty
            showAlert("Please fill in all mandatory fields.");
            return;
        }

        if (!isNumeric(phone) || !postalCodeField.getText().trim().isEmpty() && !isNumeric(postalCode)) {
            // Show error message if phone or postal code fields contain non-numeric
            // characters
            showAlert("Please enter only numbers in the Phone,Postal Code and pet Age fields.");
            return;
        }

        // All input values are valid, edit the Customer object

        customer.setAddress(address);
        customer.setEmail(email);
        customer.setName(name);
        customer.setPhone(phone);
        customer.setPostalcode(postalCode);

        // TODO: Do something with the customer object

        System.out.println("TODO SAVE TO DATABASE: " + customer);
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    @FXML
    public void cancelButtonClicked(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void setCustomer(Customer customers) {
        customer = customers;
    }

    public Customer getCustomer() {
        return customer;
    }

    @FXML
    public void createNewAnimal() throws IOException {
        Stage popupStage = new Stage();
        Parent popupRoot = FXMLLoader.load(getClass().getResource("createNewAnimalView.fxml"));
        Scene popupScene = new Scene(popupRoot);
        popupStage.setScene(popupScene);
        popupStage.show();

    }

    public void reloadView() {
        // TODO: Get animals from database again.
    }
}
