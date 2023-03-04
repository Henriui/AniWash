package aniwash.view;

import aniwash.dao.AnimalDao;
import aniwash.dao.CustomerDao;
import aniwash.dao.IAnimalDao;
import aniwash.dao.ICustomerDao;
import aniwash.entity.Animal;
import aniwash.entity.Customer;
import aniwash.resources.utilities.ControllerUtilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateNewAnimalController {
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
    private static Customer customer;

    public void initialize() {
        // Set the text fields to be editable
        saveButton.disableProperty().bind(
                // Bind the save button's disable property to a BooleanBinding
                // that checks if all mandatory fields have been filled
                petNameField.textProperty().isEmpty().or(petTypeField.textProperty().isEmpty()).or(petBreedField.textProperty().isEmpty()).or(petAgeField.textProperty().isEmpty()).or(petDescriptionField.textProperty().isEmpty()));

    }

    public static void setCustomer(Customer selCustomer) {
        customer = selCustomer;
    }

    @FXML
    public void onSaveButtonClicked(ActionEvent event) {

        String petName = petNameField.getText().trim();
        String petType = petTypeField.getText().trim();
        String petBreed = petBreedField.getText().trim();
        String petAge = petAgeField.getText().trim();
        String petDescription = petDescriptionField.getText().trim();

        if (petName.isEmpty() || petType.isEmpty() || petBreed.isEmpty() || petAge.isEmpty() || petDescription.isEmpty()) {
            // Show error message if mandatory fields are empty
            ControllerUtilities.showAlert("Please fill in all mandatory fields.");
            return;
        }

        if (!ControllerUtilities.isNumeric(petAge)) {
            // Show error message if petAge fields contain non-numeric
            // characters
            ControllerUtilities.showAlert("Please enter only numbers in the Phone,Postal Code and pet Age fields.");
            return;
        }

        // All input values are valid, create the Customer object

        Animal animal = new Animal(petName, petType, petBreed, Integer.valueOf(petAge), petDescription);
        ICustomerDao customerDao = new CustomerDao();
        IAnimalDao animalDao = new AnimalDao();
        animalDao.addAnimal(animal); // add the animal to the database
        customer.addAnimal(animal); // add the animal to the customer
        customerDao.updateCustomer(customer); // update the customer in the database


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
