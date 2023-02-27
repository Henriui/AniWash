package aniwash.resources.model;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import aniwash.entity.Animal;
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

public class CustomerListViewCellAnimal extends ListCell<Animal> {

    private HBox customerInfoHBox;
    private VBox contactInfoBox;
    private HBox basicInfoHBox;

    public CustomerListViewCellAnimal() {
        super();

        // Create the customer info HBox once

        Label nameLabel = new Label();
        Label ageLabel = new Label();
        ageLabel.setMinWidth(50);

        VBox basicInfoBox = new VBox(nameLabel);
        VBox ageBox = new VBox(ageLabel);

        basicInfoBox.setTranslateX(30);
        basicInfoHBox = new HBox(basicInfoBox);
        basicInfoBox.setPadding(new Insets(0, 50, 0, 0));
        VBox.setMargin(basicInfoHBox, new Insets(10, 0, 0, 0)); // add margin of 10 pixels to top

        Label typeLabel = new Label();
        typeLabel.setMinWidth(50);

        contactInfoBox = new VBox(typeLabel);
        VBox.setMargin(contactInfoBox, new Insets(10, 0, 0, 0)); // add margin of 10 pixels to top

        Label breedLabel = new Label();
        Label descriptionLabel = new Label();
        VBox appointmentInfoBox = new VBox(breedLabel);
        VBox ddd = new VBox(descriptionLabel);
        breedLabel.setMinWidth(50);

        customerInfoHBox = new HBox(basicInfoHBox, ageBox, contactInfoBox, appointmentInfoBox, ddd);
        customerInfoHBox.setSpacing(100);
        VBox.setMargin(customerInfoHBox, new Insets(5, 0, 0, 0)); // add margin of 5 pixels to top
        customerInfoHBox.setStyle(
                "-fx-background-color: #ffffff; -fx-padding: 10px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");
    }

    @Override
    protected void updateItem(Animal animal, boolean empty) {
        super.updateItem(animal, empty);

        if (empty || animal == null) {
            setText(null);
            setGraphic(null);
        } else {

            // Update the cell content with the customer information

            Label nameLabel = (Label) ((VBox) basicInfoHBox.getChildren().get(0)).getChildren().get(0);
            nameLabel.setText(animal.getName());

            Label ageLabel = (Label) ((VBox) customerInfoHBox.getChildren().get(1)).getChildren().get(0);
            ageLabel.setText(String.valueOf(animal.getAnimalAge()));

            Label typeLabel = (Label) (contactInfoBox.getChildren().get(0));
            typeLabel.setText(animal.getType());

            Label breedLabel = (Label) ((VBox) customerInfoHBox.getChildren().get(3)).getChildren().get(0);
            breedLabel.setText(animal.getBreed());

            Label descriptionLabel = (Label) ((VBox) customerInfoHBox.getChildren().get(4)).getChildren().get(0);
            descriptionLabel.setText(animal.getDescription());

            // Set cell content
            
            setText(null);
            setGraphic(customerInfoHBox);
            setStyle("-fx-background-color: #f2f5f9; -fx-pref-height: 45;");
        }
    }
}
