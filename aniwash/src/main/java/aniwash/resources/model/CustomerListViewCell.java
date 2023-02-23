package aniwash.resources.model;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import aniwash.entity.Customer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

public class CustomerListViewCell extends ListCell<Customer> {

    private HBox customerInfoHBox;
    private VBox contactInfoBox;
    private HBox basicInfoHBox;

    public CustomerListViewCell() {
        super();

        // Create the customer info HBox once
        Label nameLabel = new Label();
        Label emailLabel = new Label();
        emailLabel.setMinWidth(150);

        VBox basicInfoBox = new VBox(nameLabel, emailLabel);
        basicInfoBox.setTranslateX(50);
        basicInfoHBox = new HBox(basicInfoBox);
        basicInfoHBox.setSpacing(100);
        basicInfoHBox.setSpacing(100); // set spacing to 20 pixels
        basicInfoBox.setPadding(new Insets(0, 50, 0, 0));
        VBox.setMargin(basicInfoHBox, new Insets(1100, 0, 0, 0)); // add margin of 10 pixels to top

        Label cityLabel = new Label();
        Label phoneLabel = new Label();
        cityLabel.setMinWidth(120);
        phoneLabel.setMinWidth(180);
        phoneLabel.setMinHeight(5);

        contactInfoBox = new VBox(phoneLabel);
        VBox.setMargin(contactInfoBox, new Insets(1100, 0, 0, 0)); // add margin of 10 pixels to top

        Label addressLabel = new Label();
        Label postalCodeLabel = new Label();
        VBox appointmentInfoBox = new VBox(addressLabel);
        VBox ddd = new VBox(postalCodeLabel);
        addressLabel.setMinWidth(200);

        customerInfoHBox = new HBox(basicInfoHBox, contactInfoBox, appointmentInfoBox, ddd);
        customerInfoHBox.setSpacing(100);
        VBox.setMargin(customerInfoHBox, new Insets(1100, 0, 0, 0)); // add margin of 5 pixels to top
        customerInfoHBox.setStyle("-fx-background-color: #ffffff; -fx-padding: 10px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");
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

            Label addressLabel = (Label) ((VBox) customerInfoHBox.getChildren().get(2)).getChildren().get(0);
            addressLabel.setText(customer.getAddress());

            Label postalCodeLabel = (Label) ((VBox) customerInfoHBox.getChildren().get(3)).getChildren().get(0);
            postalCodeLabel.setText(customer.getPostalcode());

            // Set cell content
            setText(null);
            setGraphic(customerInfoHBox);
            setStyle("-fx-background-color: #f2f5f9; -fx-pref-height: 65;");
        }
    }
}
