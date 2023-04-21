package aniwash.view.model;

import aniwash.entity.Customer;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CustomListViewCellCustomer extends ListCell<Customer> {

    private HBox customerInfoHBox;
    private VBox contactInfoBox;
    private HBox basicInfoHBox;

    public CustomListViewCellCustomer() {
        super();

        // Create the customer info HBox once

        Label nameLabel = new Label();
        Label emailLabel = new Label();
        emailLabel.setMinWidth(200);

        VBox basicInfoBox = new VBox(nameLabel, emailLabel);
        basicInfoBox.setTranslateX(20);
        basicInfoHBox = new HBox(basicInfoBox);
        // basicInfoHBox.setSpacing(100);
        // basicInfoBox.setPadding(new Insets(0, 50, 0, 0));
        VBox.setMargin(basicInfoHBox, new Insets(10, 0, 0, 0)); // add margin of 10 pixels to top

        Label phoneLabel = new Label();
        phoneLabel.setMinWidth(30);
        phoneLabel.setMinHeight(5);

        contactInfoBox = new VBox(phoneLabel);
        VBox.setMargin(contactInfoBox, new Insets(10, 0, 0, 0)); // add margin of 10 pixels to top

        customerInfoHBox = new HBox(basicInfoHBox, contactInfoBox);
        customerInfoHBox.setSpacing(30);
        VBox.setMargin(customerInfoHBox, new Insets(5, 0, 0, 0)); // add margin of 5 pixels to top
        customerInfoHBox.setStyle(
                "-fx-background-color: #ffffff; -fx-padding: 10px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");
    }

    @Override
    protected void updateItem(Customer customer, boolean empty) {
        super.updateItem(customer, empty);

        if (empty || customer == null) {
            setText(null);
            setGraphic(null);
        } else {

            // Update the cell content with the customer information

            Label nameLabel = (Label) ((VBox) basicInfoHBox.getChildren().get(0)).getChildren().get(0);
            nameLabel.setText(customer.getName());

            Label emailLabel = (Label) ((VBox) basicInfoHBox.getChildren().get(0)).getChildren().get(1);
            emailLabel.setText(customer.getEmail());

            Label phoneLabel = (Label) (contactInfoBox.getChildren().get(0));
            phoneLabel.setText(customer.getPhone());

            // Set cell content

            setText(null);
            setGraphic(customerInfoHBox);
            setStyle("-fx-background-color: #d7d7d7; -fx-pref-height: 60;");
        }
    }
}
