package aniwash.view;

import aniwash.dao.CustomerDao;
import aniwash.dao.ICustomerDao;
import aniwash.entity.Animal;
import aniwash.entity.Appointment;
import aniwash.entity.Customer;
import aniwash.resources.model.CustomListViewCellAnimal;
import aniwash.resources.model.CustomListViewCellAppointment;
import aniwash.resources.utilities.ControllerUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

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
    private static final ObservableList<Animal> animals = FXCollections.observableArrayList();
    private static final ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
    @FXML
    private ListView<Animal> listView;
    @FXML
    private ListView<Appointment> appointmentListView;

    private static Customer customer;
    private final CustomersController customersController = new CustomersController();

    public void initialize() {
        customer = customersController.getSelectedCustomer();

        //TODO: Purkka?
        animals.clear();
        appointmentsList.clear();

        animals.addAll(customer.getAnimals());
        appointmentsList.addAll(customer.getAppointments());

        appointmentListView.setItems(appointmentsList);
        listView.setItems(animals);
        listView.setCellFactory(listView -> new CustomListViewCellAnimal());
        appointmentListView.setCellFactory(appointmentListView -> new CustomListViewCellAppointment());

        Background background = new Background(
                new BackgroundFill(Color.web("#f2f5f9"), CornerRadii.EMPTY, Insets.EMPTY));
        appointmentListView.setPlaceholder(new Label("No appointments") {
            @Override
            protected void updateBounds() {
                super.updateBounds();
                setBackground(background);
            }
        });

        listView.setStyle("-fx-background-color:  #f2f5f9; -fx-background:  #f2f5f9;");
        appointmentListView.setStyle("-fx-background-color:  #f2f5f9; -fx-background:  #f2f5f9;");

        nameField.setText(customer.getName());
        phoneField.setText(customer.getPhone());
        emailField.setText(customer.getEmail());
        addressField.setText(customer.getAddress());
        postalCodeField.setText(customer.getPostalCode());

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
            ControllerUtilities.showAlert("Please fill in all mandatory fields.");
            return;
        }

        if (!ControllerUtilities.isNumeric(phone) || !postalCodeField.getText().trim().isEmpty() && !ControllerUtilities.isNumeric(postalCode)) {
            // Show error message if phone or postal code fields contain non-numeric
            // characters
            ControllerUtilities.showAlert("Please enter only numbers in the Phone,Postal Code and pet Age fields.");
            return;
        }

        // All input values are valid, edit the Customer object

        customer.setAddress(address);
        customer.setEmail(email);
        customer.setName(name);
        customer.setPhone(phone);
        customer.setPostalCode(postalCode);

        ICustomerDao customerDao = new CustomerDao();
        customerDao.updateCustomer(customer);

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
        popupStage.setTitle("Create Animal");
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();

        popupStage.setOnHidden(view -> listView.setItems(FXCollections.observableList(customer.getAnimalList())));
        CreateNewAnimalController.setCustomer(customer);
    }
}
