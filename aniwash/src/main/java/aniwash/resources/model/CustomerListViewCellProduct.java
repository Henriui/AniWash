package aniwash.resources.model;

import aniwash.entity.Appointment;
import aniwash.entity.Product;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CustomerListViewCellProduct extends ListCell<Product> {

    private HBox customerInfoHBox;
    private HBox basicInfoHBox;

    public CustomerListViewCellProduct() {
        super();
        // Create the customer info HBox once

        Label dateLabel = new Label();
        Label descriptionLabel = new Label();
        descriptionLabel.setMinWidth(50);

        VBox basicInfoBox = new VBox(dateLabel);
        VBox ageBox = new VBox(descriptionLabel);

        basicInfoBox.setTranslateX(30);
        basicInfoHBox = new HBox(basicInfoBox);
        basicInfoBox.setPadding(new Insets(0, 50, 0, 0));
        VBox.setMargin(basicInfoHBox, new Insets(10, 0, 0, 0)); // add margin of 10 pixels to top

        Label priceLabel = new Label();
        VBox appointmentInfoBox = new VBox(priceLabel);
        priceLabel.setMinWidth(50);

        customerInfoHBox = new HBox(basicInfoHBox, ageBox, appointmentInfoBox);
        customerInfoHBox.setSpacing(100);
        VBox.setMargin(customerInfoHBox, new Insets(5, 0, 0, 0)); // add margin of 5 pixels to top
        customerInfoHBox.setStyle(
                "-fx-background-color: #ffffff; -fx-padding: 10px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");
    }

    @Override
    protected void updateItem(Product product, boolean empty) {
        super.updateItem(product, empty);

        if (empty || product == null) {
            setText(null);
            setGraphic(null);
        } else {

            // Update the cell content with the customer information

            Label dateLabel = (Label) ((VBox) basicInfoHBox.getChildren().get(0)).getChildren().get(0);
            dateLabel.setText(product.getName());

            Label priceLabel = (Label) ((VBox) customerInfoHBox.getChildren().get(2)).getChildren().get(0);
            priceLabel.setText(String.valueOf(product.getPrice()));

            Label descriptionLabel = (Label) ((VBox) customerInfoHBox.getChildren().get(1)).getChildren().get(0);
            descriptionLabel.setText(product.getDescription());


            // Set cell content
            
            setText(null);
            setGraphic(customerInfoHBox);
            setStyle("-fx-background-color: #f2f5f9; -fx-pref-height: 45;");
        }
    }
}