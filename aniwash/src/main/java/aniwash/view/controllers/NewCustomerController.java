package aniwash.view.controllers;

import aniwash.MainApp;
import aniwash.dao.*;
import aniwash.entity.Animal;
import aniwash.entity.Customer;
import aniwash.view.utilities.ControllerUtilities;
import aniwash.viewmodels.MainViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Map;
import java.util.ResourceBundle;

/**
 * This is a Java class that controls the creation of a new customer and their pet, with input
 * validation and saving to a database.
 */
public class NewCustomerController {

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

    private ResourceBundle bundle;

    public void initialize() {
        bundle = MainApp.getBundle();
        // Set the text fields to be editable
        saveButton.disableProperty().bind(
                // Bind the save button's disable property to a BooleanBinding
                // that checks if all mandatory fields have been filled
                nameField.textProperty().isEmpty().or(phoneField.textProperty().isEmpty()).or(emailField.textProperty().isEmpty()).or(petNameField.textProperty().isEmpty()).or(petTypeField.textProperty().isEmpty()).or(petBreedField.textProperty().isEmpty()).or(petAgeField.textProperty().isEmpty()).or(petDescriptionField.textProperty().isEmpty()));

    }

    /**
     * This function saves customer and animal information to a database and checks for valid input.
     * 
     * @param event An ActionEvent object that represents the event that triggered the method call. It
     * contains information about the event, such as the source of the event and any additional data
     * associated with the event.
     */
    @FXML
    public void onSaveButtonClicked(ActionEvent event) {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();
        String postalCode = postalCodeField.getText().trim();

        String petName = petNameField.getText().trim();
        String petType = petTypeField.getText().trim();
        String petBreed = petBreedField.getText().trim();
        String petAge = petAgeField.getText().trim();
        String petDescription = petDescriptionField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || petName.isEmpty() || petType.isEmpty() || petBreed.isEmpty() || petAge.isEmpty() || petDescription.isEmpty()) {
            // Show error message if mandatory fields are empty
            ControllerUtilities.showAlert(bundle.getString("fillAllFieldsText"));
            return;
        }

        if (!ControllerUtilities.isNumeric(phone) || !postalCodeField.getText().trim().isEmpty() && !ControllerUtilities.isNumeric(postalCode) || !ControllerUtilities.isNumeric(petAge)) {
            // Show error message if phone or postal code fields contain non-numeric
            // characters
            ControllerUtilities.showAlert(bundle.getString("fillNumericText"));
            return;
        }

        // All input values are valid, create the Customer object
        Customer customer = new Customer(name, phone, email, address, postalCode);
        Animal animal = new Animal(petName, petType, petBreed, petDescription);

        MainViewModel calendar = new MainViewModel();
        Map<String, IDao> daoMap = calendar.getDaoMap();
        ICustomerDao customerDao = (CustomerDao) daoMap.get("customer");
        IAnimalDao animalDao = (AnimalDao) daoMap.get("animal");
        customerDao.add(customer);
        animalDao.add(animal);
        customer.addAnimal(animal);
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void cancelButtonClicked(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

}
