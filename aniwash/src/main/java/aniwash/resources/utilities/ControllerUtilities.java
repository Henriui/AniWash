package aniwash.resources.utilities;

import aniwash.MainApp;
import aniwash.entity.*;
import aniwash.view.CreateNewAnimalController;
import aniwash.viewmodels.MainViewModel;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class ControllerUtilities {

    private static ObservableList<Product> extraProductObservableList;
    public static ShoppingCart shoppingCart = new ShoppingCart();

    public static FXMLLoader loadFXML(String fxml) throws IOException {
        return new FXMLLoader(MainApp.class.getResource("view/" + fxml + ".fxml"));
    }

    public static void newCustomer(Stage stage) throws IOException {
        final FXMLLoader loader;
        final Scene scene;
        loader = loadFXML("newCustomerView");
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Create Customer");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public static void editCustomer(Stage stage) throws IOException {
        final FXMLLoader loader;
        final Scene scene;
        loader = loadFXML("editCustomerView");
        scene = new Scene((javafx.scene.Parent) loader.load());
        stage.setScene(scene);
        stage.setTitle("Create Customer");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public static void newAnimalPopUp(Customer selectedPerson, Stage stage) throws IOException {
        final FXMLLoader loader;
        final Scene scene;
        loader = loadFXML("createNewAnimalView");
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Create Animal");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        CreateNewAnimalController.setCustomer(selectedPerson);
    }

    public static void newProduct(Stage stage) throws IOException {
        final FXMLLoader loader;
        final Scene scene;
        loader = loadFXML("newProductView");
        scene = new Scene((javafx.scene.Parent) loader.load());
        stage.setScene(scene);
        stage.setTitle("Create Product");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public static long removeStringFromId(String id) {
        StringBuilder sb = new StringBuilder(id);
        sb.deleteCharAt(0);
        sb.deleteCharAt(0);
        return Long.parseLong(sb.toString());
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void updateAnimals(Customer c, ListView<String> petList, ListView<Customer> personList, ObservableList<Customer> customerObservableList) {
        personList.setItems(customerObservableList.filtered(customer -> customer.getName().contains(c.getName())));
        petList.getItems().clear();
        petList.getItems().add("                                   Create new pet  +");
        c.getAnimals().forEach(animal -> petList.getItems().add(animal.getName()));
    }

    public static EventHandler<WindowEvent> getCustomerEvent(MainViewModel mainViewModel, ObservableList<Customer> customerObservableList, ListView<Customer> personList, ListView<String> petList, Entry<Appointment> newEntry) {
        return customerEvent -> {
            Customer c = mainViewModel.newestCustomer();
            customerObservableList.add(c);
            personList.setItems(customerObservableList.filtered(customer -> customer.getName().contains(c.getName())));
            personList.getSelectionModel().select(c);
            personList.scrollTo(c);
            String newAnimal = mainViewModel.newestPet().getName();
            ControllerUtilities.updateAnimals(personList.getSelectionModel().getSelectedItem(), petList, personList, customerObservableList);
            petList.getSelectionModel().select(newAnimal);
            newEntry.setLocation(personList.getSelectionModel().getSelectedItem().getName()); // TODO: muuta käyttöä
            // titlelle
        };
    }

    public static EventHandler<WindowEvent> getProductEvent(MainViewModel mainViewModel, ListView<String> services, Entry<Appointment> newEntry) {
        return productEvent -> {
            services.getItems().clear();
            mainViewModel.getCalendarMap().values().forEach(service -> services.getItems().addAll(service.getName()));
            String newProduct = mainViewModel.newestProduct().getLocalizations().get("en").getName();
            services.getSelectionModel().select(newProduct);
            services.scrollTo(newProduct);
            Calendar<Product> productCalendar = mainViewModel.getCalendarMap().get(services.getSelectionModel().getSelectedItem());
            newEntry.setCalendar(productCalendar);
            newEntry.setTitle(productCalendar.getName()); // TODO: muuta käyttöä titlelle
        };
    }

    public static EventHandler<KeyEvent> getSearchFieldKeyEvent(MainViewModel mainViewModel, TextField searchField, ListView<Customer> personList, ObservableList<Customer> customerObservableList, ListView<String> petList, ListView<String> services, Entry<Appointment> newEntry) {
        return event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (searchField.getText().isEmpty() || personList.getItems().isEmpty()) {
                    try {
                        // CREATE NEW CUSTOMER FROM SEARCH FIELD
                        Stage stage = new Stage();
                        stage.setOnHidden(getCustomerEvent(mainViewModel, customerObservableList, personList, petList, newEntry));
                        ControllerUtilities.newCustomer(stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    personList.getSelectionModel().select(0);
                    ControllerUtilities.updateAnimals(personList.getSelectionModel().getSelectedItem(), petList, personList, customerObservableList);
                    if (petList.getSelectionModel().getSelectedItem() != null) {
                        String newAnimal = petList.getSelectionModel().getSelectedItem();
                        newEntry.setLocation(newAnimal); // TODO: muuta käyttöä locationille
                        petList.getSelectionModel().select(newAnimal);
                        petList.scrollTo(newAnimal);
                    }
                    services.setDisable(false);
                }
            }
        };
    }

    public static EventHandler<MouseEvent> getProductMouseEvent(MainViewModel mainViewModel, ListView<String> services, Entry<Appointment> newEntry, ListView<String> petList, AnchorPane selectedProductPane, Text selectedProductLabel, Text selectedProductPriceLabel, Text selectedProductDurationLabel, Button deleteSelectedProduct, ListView extraProducts, Text totalPrice) {
        return mouseEvent -> {
            extraProducts.setStyle("-fx-background-color:  #d7d7d7; -fx-background:  #d7d7d7;");
            // Set the placeholder text for the ListView
            Background background = new Background(new BackgroundFill(Color.web("#d7d7d7"), CornerRadii.EMPTY, Insets.EMPTY));
            extraProducts.setPlaceholder(new Label("No items") {
                @Override
                protected void updateBounds() {
                    super.updateBounds();
                    setBackground(background);
                }
            });

            if (services.getSelectionModel().getSelectedItem() != null) {
                Calendar<Product> productCalendar = mainViewModel.getCalendarMap().get(services.getSelectionModel().getSelectedItem());

                // Main product

                if (selectedProductPane.isVisible() == false) {

                    // Set title and set petList to enabled

                    newEntry.setCalendar(productCalendar);
                    newEntry.setTitle(productCalendar.getName()); // TODO: muuta käyttöä titlelle
                    services.scrollTo(services.getSelectionModel().getSelectedItem());
                    petList.setDisable(false);

                    // Sets main product text label to firsty selected product, and sets its price.

                    selectedProductLabel.setText(services.getSelectionModel().getSelectedItem());
                    selectedProductPriceLabel.setText(productCalendar.getUserObject().getPrice() + " €");

                    // Adds mainProduct to shopping cart with default discount.

                    shoppingCart.addProduct(productCalendar.getUserObject(), "0");

                    // Removes selected product from the product listview and sets mainProduct
                    // AnchorPane to visible.

                    services.getItems().remove(services.getSelectionModel().getSelectedItem());
                    services.getSelectionModel().clearSelection();
                    selectedProductPane.setVisible(true);
                } else {

                    // All other items selected from the product listView are extra products.
                    // Adds selected product to the shopping cart with default discount.

                    shoppingCart.addProduct(productCalendar.getUserObject(), "0");

                    // Make "adapter" product so discount price can be directly be added to the
                    // product, so view is showing correct price.

                    DiscountProduct discountProduct = new DiscountProduct(productCalendar.getUserObject().getLocalizations().get("en").getName(), productCalendar.getUserObject().getLocalizations().get("en").getDescription(), productCalendar.getUserObject().getPrice(), productCalendar.getUserObject().getStyle());

                    // Add product to the extra product listView.

                    extraProducts.getItems().add(discountProduct);

                    System.out.println(shoppingCart.getDiscount(productCalendar.getUserObject()) + " " + shoppingCart.getTotalDiscountedPrice());

                    // Remove selected product from the product listView.

                    services.getItems().remove(services.getSelectionModel().getSelectedItem());
                    services.getSelectionModel().clearSelection();

                }
            }
            System.out.println("Price " + String.valueOf(shoppingCart.getTotalDiscountedPrice() + "€"));

            // Set totalPrice text to match all selected product price.

            totalPrice.setText("Price " + String.valueOf(shoppingCart.getTotalDiscountedPrice() + "€"));
        };
    }

    public static EventHandler<ActionEvent> applyDiscount(TextField setDiscount, ListView<DiscountProduct> extraProducts, Text selectedProductCost, Text selectedProductCostDiscount, Entry newEntry, Text selectedProduct, Text totalPrice) {
        return event -> {

            // If discount is applied and MainProduct is selected.

            if (!setDiscount.getText().isEmpty() && extraProducts.getSelectionModel().getSelectedItem() == null && selectedProduct.getFill().equals(Color.web("#47c496ff"))) {

                // Get main product from newEntry and calculate discount for the product.

                Product mainProduct = (Product) newEntry.getCalendar().getUserObject();
                String discount = setDiscount.getText();
                double newPrice = mainProduct.getPrice() - (mainProduct.getPrice() * (0.01 * Double.parseDouble(discount)));

                // Strike original price from the main product and set discount text to visible
                // and set a discounted price to it.

                selectedProductCost.strikethroughProperty().set(true);
                selectedProductCostDiscount.setVisible(true);
                selectedProductCostDiscount.setText(String.format("%.2f", newPrice) + "€");

                // Edit discount to the shopping cart.

                shoppingCart.editDiscount(mainProduct, discount);
            }

            // If discount is applied and nothing is selected.

            else if (!setDiscount.getText().isEmpty() && extraProducts.getSelectionModel().getSelectedItem() == null && !selectedProduct.getFill().equals(Color.web("#47c496ff"))) {
                System.out.println("nothing selected");
                // TODO: add dicount to current price (To all items).
            }

            // If discount is applied and extraProduct is selected.

            else if (!setDiscount.getText().isEmpty() && extraProducts.getSelectionModel().getSelectedItem() != null) {
                System.out.println("Please select a product" + extraProducts.getSelectionModel().getSelectedIndex());

                // Get "adapter" product from the listView and calculate a discounted price.

                DiscountProduct product = extraProducts.getSelectionModel().getSelectedItem();
                String discount = setDiscount.getText();
                Product original = shoppingCart.getProduct(product.getName());
                double newPrice = original.getPrice() - (original.getPrice() * (0.01 * Double.parseDouble(discount)));

                System.out.println("Please select a product what is in entry" + original.getPrice());
                System.out.println("!?!?!?!?!?!??!?!?!" + newPrice);

                // Set a price for the selected item, and modify the shopping cart the match the
                // discounted %.

                extraProducts.getItems().get(extraProducts.getSelectionModel().getSelectedIndex()).setPrice(newPrice);
                shoppingCart.editDiscountString(product.getName(), discount);

                // Refresh the listview to show new price for discounted product.

                extraProducts.refresh(); // Should update all the cells in the ListView

            }
            System.out.println("Price " + String.valueOf(shoppingCart.getTotalDiscountedPrice() + "€"));

            // Set totalPrice text to match all selected product price.

            totalPrice.setText("Price " + String.valueOf(shoppingCart.getTotalDiscountedPrice() + "€"));
        };
    }

    public static ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public static EventHandler<MouseEvent> getAnimalMouseEvent(MainViewModel mainViewModel, ObservableList<Customer> customerObservableList, ListView<Customer> personList, ListView<String> petList, Entry<Appointment> newEntry) {

        return mouseEvent -> {
            if (petList.getSelectionModel().getSelectedItem().contains("+")) {
                try {
                    // NEW ANIMAL POPUP
                    Stage stage = new Stage();
                    stage.setOnHidden(e -> {
                        ControllerUtilities.updateAnimals(personList.getSelectionModel().getSelectedItem(), petList, personList, customerObservableList);
                        String newAnimal = mainViewModel.newestPet().getName();
                        newEntry.setLocation(newAnimal); // TODO: muuta käyttöä locationille
                        petList.getSelectionModel().select(newAnimal);
                        petList.scrollTo(newAnimal);
                    });
                    ControllerUtilities.newAnimalPopUp(personList.getSelectionModel().getSelectedItem(), stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (petList.getSelectionModel().getSelectedItem() != null) {
                    String newAnimal = petList.getSelectionModel().getSelectedItem();
                    newEntry.setLocation(newAnimal); // TODO: muuta käyttöä locationille
                    petList.getSelectionModel().select(newAnimal);
                    petList.scrollTo(newAnimal);
                }
            }
        };
    }

    public static EventHandler<? super MouseEvent> selectExtraProduct(Text selectedProduct) {
        return event -> {

            // Set MainProduct text to default, to showcase that it has been Unselected.

            selectedProduct.setFill(Color.web("#000000"));
        };
    }

    public static EventHandler<? super MouseEvent> selectMainProduct(Text selectedProduct, ListView<DiscountProduct> extraProducts) {
        return event -> {

            // Set MainProduct text light green, to showcase that it has been selected.

            selectedProduct.setFill(Color.web("#47c496"));
            extraProducts.getSelectionModel().clearSelection();
        };
    }

    public static EventHandler<ActionEvent> deleteMainProduct(ListView<String> services, AnchorPane selectedProductPane, Entry<Appointment> newEntry, Text selectedProduct, Text selectedProductCost, Text selectedProductCostDiscount, Text totalPrice) {
        return event -> {

            // Add MainProduct back to the product ListView and hide the MainProduct
            // AnchorPane.

            services.getItems().addAll(selectedProduct.getText());
            selectedProductPane.setVisible(false);
            System.out.println("deleteMainProduct" + newEntry.getCalendar().getUserObject());

            // Remove product from the shopping cart.

            shoppingCart.removeMainProduct((Product) newEntry.getCalendar().getUserObject());

            // Set strike text to false and hide the discount text.

            selectedProductCost.strikethroughProperty().set(false);
            selectedProductCostDiscount.setVisible(false);

            // Set Calendar to null.

            //newEntry.setCalendar(null);

            // Set totalPrice text to match all selected product price.

            totalPrice.setText("Price " + String.valueOf(shoppingCart.getTotalDiscountedPrice() + "€"));
        };

    }

    public static EventHandler<MouseEvent> getPersonMouseEvent(MainViewModel mainViewModel, ObservableList<Customer> customerObservableList, ListView<Customer> personList, ListView<String> petList, Entry<Appointment> newEntry, ListView<String> services) {

        return mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                try {
                    // NEW CUSTOMER POPUP
                    Stage stage = new Stage();
                    stage.setOnHidden(getCustomerEvent(mainViewModel, customerObservableList, personList, petList, newEntry));
                    ControllerUtilities.newCustomer(stage);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (personList.getSelectionModel().getSelectedItem() != null) {
                    Customer customer = personList.getSelectionModel().getSelectedItem();
                    personList.getSelectionModel().select(customer);
                    personList.scrollTo(customer);
                    ControllerUtilities.updateAnimals(personList.getSelectionModel().getSelectedItem(), petList, personList, customerObservableList);
                    services.setDisable(false);
                }
            }
        };
    }

}
