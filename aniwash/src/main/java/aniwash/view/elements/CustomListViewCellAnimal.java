package aniwash.view.elements;

import aniwash.entity.Animal;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CustomListViewCellAnimal extends ListCell<Animal> {

    private HBox animalInfoHBox;
    private VBox typeBox;
    private HBox basicInfoHBox;

    public CustomListViewCellAnimal() {
        super();

        // Create the customer info HBox once

        Label nameLabel = new Label();
        Label ageLabel = new Label();
        nameLabel.setMinWidth(100);

        VBox basicInfoBox = new VBox(nameLabel);
        VBox ageBox = new VBox(ageLabel);

        basicInfoBox.setTranslateX(30);
        basicInfoHBox = new HBox(basicInfoBox);
        basicInfoBox.setPadding(new Insets(0, 50, 0, 0));
        VBox.setMargin(basicInfoHBox, new Insets(10, 0, 0, 0)); // add margin of 10 pixels to top

        Label typeLabel = new Label();
        typeLabel.setMinWidth(50);

        typeBox = new VBox(typeLabel);
        VBox.setMargin(typeBox, new Insets(10, 0, 0, 0)); // add margin of 10 pixels to top

        Label breedLabel = new Label();
        Label descriptionLabel = new Label();
        VBox breedBox = new VBox(breedLabel);
        VBox descriptionBox = new VBox(descriptionLabel);
        breedLabel.setMinWidth(50);

        animalInfoHBox = new HBox(basicInfoHBox, ageBox, typeBox, breedBox, descriptionBox);
        animalInfoHBox.setSpacing(100);
        VBox.setMargin(animalInfoHBox, new Insets(5, 0, 0, 0)); // add margin of 5 pixels to top
        animalInfoHBox.setStyle(
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

            Label ageLabel = (Label) ((VBox) animalInfoHBox.getChildren().get(1)).getChildren().get(0);
            ageLabel.setText(String.valueOf(1));

            Label typeLabel = (Label) (typeBox.getChildren().get(0));
            typeLabel.setText(animal.getType());

            Label breedLabel = (Label) ((VBox) animalInfoHBox.getChildren().get(3)).getChildren().get(0);
            breedLabel.setText(animal.getBreed());

            Label descriptionLabel = (Label) ((VBox) animalInfoHBox.getChildren().get(4)).getChildren().get(0);
            descriptionLabel.setText(animal.getDescription());

            // Set cell content

            setText(null);
            setGraphic(animalInfoHBox);
            setStyle("-fx-background-color: #f2f5f9; -fx-pref-height: 45;");
        }
    }

}
