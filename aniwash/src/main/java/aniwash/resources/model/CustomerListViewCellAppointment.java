package aniwash.resources.model;

import aniwash.entity.Appointment;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CustomerListViewCellAppointment extends ListCell<Appointment> {

    private HBox animalInfoHBox;
    private HBox dateHBox;

    public CustomerListViewCellAppointment() {
        super();
        // Create the customer info HBox once

        Label dateLabel = new Label();
        Label descriptionLabel = new Label();
        descriptionLabel.setMinWidth(50);

        VBox dateBox = new VBox(dateLabel);
        VBox descriptionBox = new VBox(descriptionLabel);

        dateBox.setTranslateX(30);
        dateHBox = new HBox(dateBox);
        dateBox.setPadding(new Insets(0, 50, 0, 0));
        VBox.setMargin(dateHBox, new Insets(10, 0, 0, 0)); // add margin of 10 pixels to top

        animalInfoHBox = new HBox(dateHBox, descriptionBox);
        animalInfoHBox.setSpacing(100);
        VBox.setMargin(animalInfoHBox, new Insets(5, 0, 0, 0)); // add margin of 5 pixels to top
        animalInfoHBox.setStyle(
                "-fx-background-color: #ffffff; -fx-padding: 10px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");
    }

    @Override
    protected void updateItem(Appointment appointment, boolean empty) {
        super.updateItem(appointment, empty);

        if (empty || appointment == null) {
            setText(null);
            setGraphic(null);
        } else {

            // Update the cell content with the customer information

            Label dateLabel = (Label) ((VBox) dateHBox.getChildren().get(0)).getChildren().get(0);
            dateLabel.setText(String.valueOf(appointment.getDate().toLocalDate()));

            Label descriptionLabel = (Label) ((VBox) animalInfoHBox.getChildren().get(1)).getChildren().get(0);
            descriptionLabel.setText(String.valueOf(appointment.getDescription()));

            // Set cell content

            setText(null);
            setGraphic(animalInfoHBox);
            setStyle("-fx-background-color: #f2f5f9; -fx-pref-height: 45;");
        }
    }
}
