package aniwash.view.elements;

import aniwash.entity.Product;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * This is a custom JavaFX ListCell class for displaying product information in a ListView.
 */
public class CustomListViewCellProduct extends ListCell<Product> {

    private HBox produtInfoHBox;
    private HBox produtNameHBox;

    public CustomListViewCellProduct() {
        super();
        // Create the customer info HBox once
        this.itemProperty().get();
        Label nameLabel = new Label();
        Label descriptionLabel = new Label();
        nameLabel.setMinWidth(300);
        nameLabel.fontProperty().set(new javafx.scene.text.Font(15));
        descriptionLabel.setMinWidth(300);
        descriptionLabel.fontProperty().set(new javafx.scene.text.Font(15));

        VBox nameBox = new VBox(nameLabel);
        VBox ageBox = new VBox(descriptionLabel);

        nameBox.setTranslateX(75);
        produtNameHBox = new HBox(nameBox);
        nameBox.setPadding(new Insets(0, 50, 0, 0));
        VBox.setMargin(produtNameHBox, new Insets(10, 0, 0, 0)); // add margin of 10 pixels to top

        Label priceLabel = new Label();
        VBox priceBox = new VBox(priceLabel);
        priceLabel.setMinWidth(100);
        priceLabel.fontProperty().set(new javafx.scene.text.Font(15));

        produtInfoHBox = new HBox(produtNameHBox, ageBox, priceBox);
        produtInfoHBox.setSpacing(100);
        VBox.setMargin(produtInfoHBox, new Insets(5, 0, 0, 0)); // add margin of 5 pixels to top
        produtInfoHBox.setStyle(
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

            Label nameLabel = (Label) ((VBox) produtNameHBox.getChildren().get(0)).getChildren().get(0);
            nameLabel.setText(product.getLocalizations().get("en").getName());

            Label priceLabel = (Label) ((VBox) produtInfoHBox.getChildren().get(2)).getChildren().get(0);
            priceLabel.setText(String.valueOf(product.getPrice() + "€"));

            Label descriptionLabel = (Label) ((VBox) produtInfoHBox.getChildren().get(1)).getChildren().get(0);
            descriptionLabel.setText(product.getLocalizations().get("en").getDescription());

            // Set cell content

            setText(null);
            setGraphic(produtInfoHBox);
            setStyle("-fx-background-color: #f2f5f9; -fx-pref-height: 60;");
        }
    }

}
