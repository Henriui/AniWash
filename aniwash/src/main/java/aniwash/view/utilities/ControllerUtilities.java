package aniwash.view.utilities;

import aniwash.MainApp;
import aniwash.entity.Appointment;
import aniwash.entity.Customer;
import aniwash.entity.Discount;
import aniwash.entity.Product;
import aniwash.view.controllers.CreateNewAnimalController;
import aniwash.viewmodels.DiscountProduct;
import aniwash.viewmodels.MainViewModel;
import aniwash.viewmodels.ShoppingCart;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * The ControllerUtilities class contains various static methods for loading FXML files, creating
 * pop-up windows, updating lists, and handling events in a JavaFX application.
 */
public class ControllerUtilities {
    private static final ResourceBundle bundle = MainApp.getBundle();

    public static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/" + fxml + ".fxml"));
        fxmlLoader.setResources(MainApp.getBundle());
        return fxmlLoader;
    }

    public static void newCustomer(Stage stage) throws IOException {
        final FXMLLoader loader;
        final Scene scene;
        loader = loadFXML("newCustomerView");
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle(bundle.getString("createCustomerLabel"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public static void editCustomer(Stage stage) throws IOException {
        final FXMLLoader loader;
        final Scene scene;
        loader = loadFXML("editCustomerView");
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle(bundle.getString("editCustomerLabel"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public static void newAnimalPopUp(Customer selectedPerson, Stage stage) throws IOException {
        final FXMLLoader loader;
        final Scene scene;
        loader = loadFXML("createNewAnimalView");
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle(bundle.getString("createAnimalLabel"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        CreateNewAnimalController.setCustomer(selectedPerson);
    }

    public static void newProduct(Stage stage) throws IOException {
        final FXMLLoader loader;
        final Scene scene;
        loader = loadFXML("newProductView");
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle(bundle.getString("createProductLabel"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public static void newAnimal(Stage stage) throws IOException {
        final FXMLLoader loader;
        final Scene scene;
        loader = loadFXML("createNewAnimalView");
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle(bundle.getString("createAnimalLabel"));
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
        alert.setTitle(bundle.getString("errorAlert"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void updateAnimals(Customer c, ListView<String> petList, ListView<Customer> personList,
                                     ObservableList<Customer> customerObservableList) {
        personList.setItems(customerObservableList.filtered(customer -> customer.getName().contains(c.getName())));
        petList.getItems().clear();
        petList.getItems().add("                                   Create new pet  +");
        c.getAnimals().forEach(animal -> petList.getItems().add(animal.getName()));
    }

    /**
     * This function adds a new customer to a list and updates the corresponding UI elements.
     * 
     * @param mainViewModel An instance of the MainViewModel class, which likely contains data and
     * methods related to the application's main view or functionality.
     * @param customerObservableList An ObservableList of Customer objects that is used to display and
     * manage the list of customers in the UI.
     * @param personList A ListView that displays a list of customers.
     * @param petList A ListView of Strings representing the pets of a selected customer.
     * @param newEntry An Entry object representing a new appointment that is being created. The method
     * sets the location of the appointment to the name of the selected customer in the personList
     * ListView.
     * @return An EventHandler of type WindowEvent is being returned.
     */
    public static EventHandler<WindowEvent> getCustomerEvent(MainViewModel mainViewModel,
                                                             ObservableList<Customer> customerObservableList, ListView<Customer> personList, ListView<String> petList,
                                                             Entry<Appointment> newEntry) {
        return customerEvent -> {
            Customer c = mainViewModel.newestCustomer();
            customerObservableList.add(c);
            personList.setItems(customerObservableList.filtered(customer -> customer.getName().contains(c.getName())));
            personList.getSelectionModel().select(c);
            personList.scrollTo(c);
            String newAnimal = mainViewModel.newestPet().getName();
            ControllerUtilities.updateAnimals(personList.getSelectionModel().getSelectedItem(), petList, personList,
                    customerObservableList);
            petList.getSelectionModel().select(newAnimal);
            newEntry.setLocation(personList.getSelectionModel().getSelectedItem().getName()); // TODO: muuta käyttöä
            // titlelle
        };
    }

    /**
     * This function returns an event handler that clears a ListView, populates it with product names,
     * selects the newest product, sets the calendar and title of a new entry based on the selected
     * product.
     * 
     * @param mainViewModel An instance of the MainViewModel class, which likely contains data and
     * methods related to the application's main view and functionality.
     * @param services A ListView of String type that displays the names of services.
     * @param newEntry An Entry object representing a new appointment entry to be added to a calendar.
     * @return An EventHandler of type WindowEvent.
     */
    public static EventHandler<WindowEvent> getProductEvent(MainViewModel mainViewModel, ListView<String> services,
                                                            Entry<Appointment> newEntry) {
        return productEvent -> {
            services.getItems().clear();
            mainViewModel.getCalendarMap().values().forEach(service -> services.getItems().addAll(service.getName()));
            String newProduct = mainViewModel.newestProduct().getName(MainApp.getLocale().getLanguage());
            services.getSelectionModel().select(newProduct);
            services.scrollTo(newProduct);
            Calendar<Product> productCalendar = mainViewModel.getCalendarMap()
                    .get(services.getSelectionModel().getSelectedItem());
            newEntry.setCalendar(productCalendar);
            newEntry.setTitle(productCalendar.getName()); // TODO: muuta käyttöä titlelle
        };
    }

    /**
     * This function returns an event handler that handles key events for a search field and performs
     * actions based on the input.
     * 
     * @param mainViewModel an instance of the MainViewModel class, which likely contains data and
     * methods related to the application's main view and functionality.
     * @param searchField A TextField where the user can enter a search query.
     * @param personList A ListView of Customer objects.
     * @param customerObservableList An ObservableList of Customer objects.
     * @param petList A ListView of strings representing the pets owned by the selected customer in the
     * personList.
     * @param services A ListView of String objects representing the available services for an
     * appointment.
     * @param newEntry An Entry object representing a new appointment.
     * @return An EventHandler<KeyEvent> object is being returned.
     */
    public static EventHandler<KeyEvent> getSearchFieldKeyEvent(MainViewModel mainViewModel, TextField searchField,
                                                                ListView<Customer> personList, ObservableList<Customer> customerObservableList, ListView<String> petList,
                                                                ListView<String> services, Entry<Appointment> newEntry) {
        return event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (searchField.getText().isEmpty() || personList.getItems().isEmpty()) {
                    try {
                        // CREATE NEW CUSTOMER FROM SEARCH FIELD
                        Stage stage = new Stage();
                        stage.setOnHidden(
                                getCustomerEvent(mainViewModel, customerObservableList, personList, petList, newEntry));
                        ControllerUtilities.newCustomer(stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    personList.getSelectionModel().select(0);
                    ControllerUtilities.updateAnimals(personList.getSelectionModel().getSelectedItem(), petList,
                            personList, customerObservableList);
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

    /**
     * This function handles mouse events for selecting products and adding them to a shopping cart,
     * updating the total price.
     * 
     * @param mainViewModel An instance of the MainViewModel class, which likely contains data and
     * methods related to the application's main view and functionality.
     * @param services A ListView of String objects representing available services/products.
     * @param newEntry An Entry object representing a new appointment entry in a calendar.
     * @param petList A ListView of pets that the user can select for the appointment.
     * @param selectedProductPane An AnchorPane that represents the main product selected by the user.
     * @param selectedProductLabel A Text object representing the label for the currently selected
     * product.
     * @param selectedProductPriceLabel A Text object representing the label that displays the price of
     * the selected product in the user interface.
     * @param selectedProductDurationLabel A Text object representing the duration of the selected
     * product in the shopping cart.
     * @param deleteSelectedProduct A Button object that represents the button used to delete a
     * selected product from the shopping cart.
     * @param extraProducts A ListView that displays extra products added to the shopping cart.
     * @param totalPrice A Text object representing the total price of all selected products in the
     * shopping cart.
     * @param shoppingCart A ShoppingCart object that stores the selected products and their discounts.
     * @return An EventHandler of type MouseEvent is being returned.
     */
    public static EventHandler<MouseEvent> getProductMouseEvent(MainViewModel mainViewModel, ListView<String> services,
                                                                Entry<Appointment> newEntry, ListView<String> petList, AnchorPane selectedProductPane,
                                                                Text selectedProductLabel, Text selectedProductPriceLabel, Text selectedProductDurationLabel,
                                                                Button deleteSelectedProduct, ListView extraProducts, Text totalPrice, ShoppingCart shoppingCart) {
        return mouseEvent -> {
            extraProducts.setStyle("-fx-background-color:  #d7d7d7; -fx-background:  #d7d7d7;");
            // Set the placeholder text for the ListView
            Background background = new Background(
                    new BackgroundFill(Color.web("#d7d7d7"), CornerRadii.EMPTY, Insets.EMPTY));
            extraProducts.setPlaceholder(new Label("No items") {
                @Override
                protected void updateBounds() {
                    super.updateBounds();
                    setBackground(background);
                }
            });

            if (services.getSelectionModel().getSelectedItem() != null) {
                Calendar<Product> productCalendar = mainViewModel.getCalendarMap()
                        .get(services.getSelectionModel().getSelectedItem());

                // Main product

                if (!selectedProductPane.isVisible()) {

                    // Set title and set petList to enabled

                    newEntry.setCalendar(productCalendar);
                    newEntry.setTitle(productCalendar.getName()); // TODO: muuta käyttöä titlelle
                    services.scrollTo(services.getSelectionModel().getSelectedItem());
                    petList.setDisable(false);

                    // Sets main product text label to firsty selected product, and sets its price.

                    selectedProductLabel.setText(services.getSelectionModel().getSelectedItem());
                    selectedProductPriceLabel.setText(productCalendar.getUserObject().getPrice() + " €");

                    // Adds mainProduct to shopping cart with default discount.

                    Discount basicDiscount = new Discount(productCalendar.getUserObject().getId(), 0.0);

                    shoppingCart.addProduct(productCalendar.getUserObject(), basicDiscount);

                    // Removes selected product from the product listview and sets mainProduct
                    // AnchorPane to visible.

                    services.getItems().remove(services.getSelectionModel().getSelectedItem());
                    services.getSelectionModel().clearSelection();
                    selectedProductPane.setVisible(true);
                } else {

                    // All other items selected from the product listView are extra products.
                    // Adds selected product to the shopping cart with default discount.

                    Discount extraDiscount = new Discount(productCalendar.getUserObject().getId(), 0.0);
                    shoppingCart.addProduct(productCalendar.getUserObject(), extraDiscount);

                    // Make "adapter" product so discount price can be directly be added to the
                    // product, so view is showing correct price.

                    DiscountProduct discountProduct = new DiscountProduct(
                            productCalendar.getUserObject().getName(MainApp.getLocale().getLanguage()),
                            productCalendar.getUserObject().getPrice());

                    // Add product to the extra product listView.

                    extraProducts.getItems().add(discountProduct);

                    System.out.println(shoppingCart.getDiscount(productCalendar.getUserObject()) + " "
                            + shoppingCart.getTotalDiscountedPrice());

                    // Remove selected product from the product listView.

                    services.getItems().remove(services.getSelectionModel().getSelectedItem());
                    services.getSelectionModel().clearSelection();

                }
            }

            // Set totalPrice text to match all selected product price.

            totalPrice.setText("Price " + shoppingCart.getTotalDiscountedPrice() + "€");
        };
    }

    /**
     * This function applies a discount to selected products in a shopping cart and updates the prices
     * accordingly.
     * 
     * @param setDiscount A TextField where the user can input a discount percentage.
     * @param extraProducts A ListView of DiscountProduct objects, representing the extra products
     * added to the shopping cart.
     * @param selectedProductCost A Text object representing the original price of the selected
     * product.
     * @param selectedProductCostDiscount A Text object representing the discounted price of the
     * selected product.
     * @param newEntry An Entry object representing a calendar entry for a product.
     * @param selectedProduct A Text object representing the name of the selected product in the
     * shopping cart.
     * @param totalPrice A Text object representing the total price of all selected products in the
     * shopping cart.
     * @param shoppingCart It is an instance of the ShoppingCart class, which represents the user's
     * shopping cart and contains methods for adding, removing, and editing products and discounts.
     * @return An EventHandler<ActionEvent> object is being returned.
     */
    public static EventHandler<ActionEvent> applyDiscount(TextField setDiscount,
                                                          ListView<DiscountProduct> extraProducts, Text selectedProductCost, Text selectedProductCostDiscount,
                                                          Entry newEntry, Text selectedProduct, Text totalPrice, ShoppingCart shoppingCart) {
        return event -> {

            // If discount is applied and MainProduct is selected.

            if (!setDiscount.getText().isEmpty() && extraProducts.getSelectionModel().getSelectedItem() == null
                    && selectedProduct.getFill().equals(Color.web("#47c496ff"))) {
                // Get main product from newEntry and calculate discount for the product.

                Product mainProduct = (Product) newEntry.getCalendar().getUserObject();
                String discount = setDiscount.getText();
                double newPrice = mainProduct.getPrice()
                        - (mainProduct.getPrice() * (0.01 * Double.parseDouble(discount)));

                // Strike original price from the main product and set discount text to visible
                // and set a discounted price to it.

                selectedProductCost.strikethroughProperty().set(true);
                selectedProductCostDiscount.setVisible(true);
                selectedProductCostDiscount.setText(String.format("%.2f", newPrice) + "€");

                // Edit discount to the shopping cart.

                Discount editDiscount = new Discount(mainProduct.getId(), Double.parseDouble(discount));

                shoppingCart.editDiscount(mainProduct, editDiscount);
            }

            // If discount is applied and nothing is selected.

            else if (!setDiscount.getText().isEmpty() && extraProducts.getSelectionModel().getSelectedItem() == null
                    && !selectedProduct.getFill().equals(Color.web("#47c496ff"))) {

                Product mainProduct = (Product) newEntry.getCalendar().getUserObject();
                String discount = setDiscount.getText();
                double newPrice = mainProduct.getPrice()
                        - (mainProduct.getPrice() * (0.01 * Double.parseDouble(discount)));

                shoppingCart.addToAllProducts(setDiscount.getText());

                extraProducts.getItems().forEach(asd -> {
                    String newDiscount = setDiscount.getText();
                    Product original = shoppingCart.getProduct(asd.getName());

                    double newPrices = original.getPrice()
                            - (original.getPrice() * (0.01 * Double.parseDouble(newDiscount)));
                    asd.setPrice(newPrices);
                });

                // Strike original price from the main product and set discount text to visible
                // and set a discounted price to it.

                selectedProductCost.strikethroughProperty().set(true);
                selectedProductCostDiscount.setVisible(true);
                selectedProductCostDiscount.setText(String.format("%.2f", newPrice) + "€");

                extraProducts.refresh(); // Should update all the cells in the ListView
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

                // Set a price for the selected item, and modify the shopping cart the match the
                // discounted %.

                extraProducts.getItems().get(extraProducts.getSelectionModel().getSelectedIndex()).setPrice(newPrice);

                Discount editDiscount = new Discount(original.getId(), Double.parseDouble(discount));
                shoppingCart.editDiscountString(product.getName(), editDiscount);

                // Refresh the listview to show new price for discounted product.

                extraProducts.refresh(); // Should update all the cells in the ListView

            }
            // Set totalPrice text to match all selected product price.

            totalPrice.setText(bundle.getString("priceLabel") + ": " + shoppingCart.getTotalDiscountedPrice() + " €");
        };

    }

    /**
     * This function returns an event handler for a mouse click on an animal in a list, which opens a
     * new popup window to add a new animal or selects an existing animal and updates the appointment
     * location accordingly.
     * 
     * @param mainViewModel An instance of the MainViewModel class, which likely contains data and
     * methods related to the main view of the application.
     * @param customerObservableList An ObservableList of Customer objects.
     * @param personList A ListView that displays a list of customers.
     * @param petList A ListView of Strings representing the list of pets for a selected customer.
     * @param newEntry An Entry object representing a new appointment entry.
     * @return An EventHandler of type MouseEvent is being returned.
     */
    public static EventHandler<MouseEvent> getAnimalMouseEvent(MainViewModel mainViewModel,
                                                               ObservableList<Customer> customerObservableList, ListView<Customer> personList, ListView<String> petList,
                                                               Entry<Appointment> newEntry) {

        return mouseEvent -> {
            if (petList.getSelectionModel().getSelectedItem().contains("+")) {
                try {
                    // NEW ANIMAL POPUP
                    Stage stage = new Stage();
                    stage.setOnHidden(e -> {
                        ControllerUtilities.updateAnimals(personList.getSelectionModel().getSelectedItem(), petList,
                                personList, customerObservableList);
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

    /**
     * This function handles the selection of extra products in a ListView and updates the appearance
     * of the main product and selected product text accordingly.
     * 
     * @param selectedProduct A Text object representing the currently selected product.
     * @param mainProduct A Rectangle object representing the main product.
     * @param extraProducts A ListView of DiscountProduct objects, which likely contains additional
     * products that can be selected by the user.
     * @return An EventHandler of type MouseEvent or a subtype of MouseEvent.
     */
    public static EventHandler<? super MouseEvent> selectExtraProduct(Text selectedProduct, Rectangle mainProduct,
                                                                      ListView<DiscountProduct> extraProducts) {
        return event -> {

            // Set MainProduct text to default, to showcase that it has been Unselected.

            if (!selectedProduct.getFill().equals(Color.web("#32965D"))
                    && event.getClickCount() == 2) {
                extraProducts.getSelectionModel().clearSelection();
            }
            selectedProduct.setFill(Color.web("#000000"));
            mainProduct.setStroke(Color.web("#a4a4a4"));
        };
    }

    /**
     * This function sets the selected main product's text color and border to light green and clears
     * the selection of any extra products in a ListView.
     * 
     * @param selectedProduct A Text object representing the main product that has been selected.
     * @param extraProducts A ListView of DiscountProduct objects, which likely contains additional
     * products related to the main product being selected.
     * @param mainProduct A Rectangle object representing the main product that has been selected.
     * @return An EventHandler that takes a MouseEvent as input and performs the actions specified in
     * the method body.
     */
    public static EventHandler<? super MouseEvent> selectMainProduct(Text selectedProduct,
                                                                     ListView<DiscountProduct> extraProducts, Rectangle mainProduct) {
        return event -> {

            // Set MainProduct text light green, to showcase that it has been selected.

            selectedProduct.setFill(Color.web("#32965D"));
            mainProduct.setStroke(Color.valueOf("#32965D"));
            extraProducts.getSelectionModel().clearSelection();
        };
    }

    /**
     * This Java function deletes a main product from a shopping cart and updates the total price.
     * 
     * @param services A ListView of available services/products.
     * @param selectedProductPane An AnchorPane that represents the selected product in the UI.
     * @param newEntry An Entry object representing an appointment in a calendar. It contains a
     * reference to a Product object that represents the main product selected for the appointment.
     * @param selectedProduct A Text object representing the name of the selected product.
     * @param selectedProductCost A Text object representing the cost of the selected product before
     * any discounts are applied.
     * @param selectedProductCostDiscount A Text object representing the discounted cost of the
     * selected product.
     * @param totalPrice A Text object representing the total price of all products in the shopping
     * cart.
     * @param shoppingCart A ShoppingCart object that contains the products selected by the user.
     * @param appointment An instance of the Appointment class, representing an appointment that the
     * user has scheduled.
     * @return An EventHandler<ActionEvent> object is being returned.
     */
    public static EventHandler<ActionEvent> deleteMainProduct(ListView<String> services, AnchorPane selectedProductPane,
                                                              Entry<Appointment> newEntry, Text selectedProduct, Text selectedProductCost,
                                                              Text selectedProductCostDiscount, Text totalPrice, ShoppingCart shoppingCart, Appointment appointment) {
        return event -> {

            // Add MainProduct back to the product ListView and hide the MainProduct
            // AnchorPane.

            services.getItems().addAll(selectedProduct.getText());
            selectedProductPane.setVisible(false);

            // Remove product from the shopping cart.

            shoppingCart.removeProduct((Product) newEntry.getCalendar().getUserObject());

            if (appointment != null)
                appointment.removeProduct((Product) newEntry.getCalendar().getUserObject());
            // Set strike text to false and hide the discount text.

            selectedProductCost.strikethroughProperty().set(false);
            selectedProductCostDiscount.setVisible(false);

            // Set totalPrice text to match all selected product price.

            totalPrice.setText(bundle.getString("priceLabel") + ": " + shoppingCart.getTotalDiscountedPrice() + " €");
        };

    }

    /**
     * This function returns a mouse event handler that either opens a new customer popup or updates
     * the selected customer's information.
     * 
     * @param mainViewModel It is an instance of the MainViewModel class, which is a view model that
     * manages the data and behavior of the main view of the application.
     * @param customerObservableList An ObservableList of Customer objects, used to populate the
     * ListView of customers in the UI.
     * @param personList A ListView that displays a list of customers.
     * @param petList A ListView of String objects representing the pets owned by a selected customer.
     * @param newEntry An Entry object representing a new appointment to be added.
     * @param services A ListView of String objects representing the available services for
     * appointments.
     * @return An EventHandler of type MouseEvent is being returned.
     */
    public static EventHandler<MouseEvent> getPersonMouseEvent(MainViewModel mainViewModel,
                                                               ObservableList<Customer> customerObservableList, ListView<Customer> personList, ListView<String> petList,
                                                               Entry<Appointment> newEntry, ListView<String> services) {

        return mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                try {
                    // NEW CUSTOMER POPUP
                    Stage stage = new Stage();
                    stage.setOnHidden(
                            getCustomerEvent(mainViewModel, customerObservableList, personList, petList, newEntry));
                    ControllerUtilities.newCustomer(stage);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (personList.getSelectionModel().getSelectedItem() != null) {
                    Customer customer = personList.getSelectionModel().getSelectedItem();
                    personList.getSelectionModel().select(customer);
                    personList.scrollTo(customer);
                    ControllerUtilities.updateAnimals(personList.getSelectionModel().getSelectedItem(), petList,
                            personList, customerObservableList);
                    services.setDisable(false);
                }
            }
        };
    }

}
