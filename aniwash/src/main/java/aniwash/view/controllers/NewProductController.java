package aniwash.view.controllers;

import aniwash.entity.Product;
import aniwash.entity.localization.LocalizedId;
import aniwash.entity.localization.LocalizedProduct;
import aniwash.view.utilities.ControllerUtilities;
import aniwash.viewmodels.MainViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
                nameField.textProperty().isEmpty().or(decriptionField.textProperty().isEmpty()).or(priceField.textProperty().isEmpty()));

    }

    @FXML
    public void onSaveButtonClicked(ActionEvent event) {
        String name = nameField.getText().trim();
        String description = decriptionField.getText().trim();
        String price = priceField.getText().trim();
        if (name.isEmpty() || description.isEmpty() || price.isEmpty()) {
            // Show error message if mandatory fields are empty
            ControllerUtilities.showAlert("Please fill in all mandatory fields.");
            return;
        }
        if (!ControllerUtilities.isNumeric(price)) {
            // Show error message if description or postal code fields contain non-numeric
            // characters
            ControllerUtilities.showAlert("Please enter only numbers in the Price field fields.");
            return;
        }
        int styleInt = (int) (Math.random() * 8 - 1) + 1;
        // All input values are valid, create the Procuct object
        Product product = new Product(name, description, Integer.parseInt(price), "style" + styleInt);
        LocalizedProduct localizedProduct = new LocalizedProduct(product, name, description);
        localizedProduct.setId(new LocalizedId("en"));
        product.getLocalizations().put("en", localizedProduct);
        MainViewModel mainViewModel = new MainViewModel();
        mainViewModel.createCalendar(product);
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
