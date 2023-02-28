package aniwash.view;

import aniwash.entity.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewProductController {
    // Create text fields for Customer section
    @FXML
    private TextField nameField;
    @FXML
    private TextField decriptionField;
    @FXML
    private TextField priceField;
    @FXML
    private Button saveButton;

    public void initialize() {
        // Set the text fields to be editable
        saveButton.disableProperty().bind(
                // Bind the save button's disable property to a BooleanBinding
                // that checks if all mandatory fields have been filled
                nameField.textProperty().isEmpty()
                        .or(decriptionField.textProperty().isEmpty())
                        .or(priceField.textProperty().isEmpty()));

    }

    @FXML
    public void onSaveButtonClicked(ActionEvent event) {
        String name = nameField.getText().trim();
        String description = decriptionField.getText().trim();
        String price = priceField.getText().trim();

        if (name.isEmpty() || description.isEmpty() || price.isEmpty()) {
            // Show error message if mandatory fields are empty
            showAlert("Please fill in all mandatory fields.");
            return;
        }

        if (!isNumeric(price)) {
            // Show error message if description or postal code fields contain non-numeric
            // characters
            showAlert("Please enter only numbers in the Price field fields.");
            return;
        }

        // All input values are valid, create the Procuct object
        // TODO: Do something with the product object
        Product product = new Product(name, description, Integer.valueOf(price));

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
}
