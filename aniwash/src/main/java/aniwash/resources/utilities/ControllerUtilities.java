package aniwash.resources.utilities;

import aniwash.MainApp;
import aniwash.entity.Appointment;
import aniwash.entity.Customer;
import aniwash.entity.Product;
import aniwash.entity.ShoppingCart;
import aniwash.resources.model.CustomListViewCellCustomer;
import aniwash.resources.model.CustomListViewCellExtraProduct;
import aniwash.resources.model.CustomListViewCellProduct;
import aniwash.resources.model.MainViewModel;
import aniwash.view.CreateNewAnimalController;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

    public static void updateAnimals(Customer c, ListView<String> petList, ListView<Customer> personList,
            ObservableList<Customer> customerObservableList) {
        personList.setItems(customerObservableList.filtered(customer -> customer.getName().contains(c.getName())));
        petList.getItems().clear();
        petList.getItems().add("                                   Create new pet  +");
        c.getAnimals().forEach(animal -> petList.getItems().add(animal.getName()));
    }

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

    public static EventHandler<WindowEvent> getProductEvent(MainViewModel mainViewModel, ListView<String> services,
            Entry<Appointment> newEntry) {
        return productEvent -> {
            services.getItems().clear();
            services.getItems().add("                                   Create new service  +");
            mainViewModel.getCalendarMap().values().forEach(service -> services.getItems().addAll(service.getName()));
            String newProduct = mainViewModel.newestProduct().getName();
            services.getSelectionModel().select(newProduct);
            services.scrollTo(newProduct);
            Calendar<Product> productCalendar = mainViewModel.getCalendarMap()
                    .get(services.getSelectionModel().getSelectedItem());
            newEntry.setCalendar(productCalendar);
            newEntry.setTitle(productCalendar.getName()); // TODO: muuta käyttöä titlelle
        };
    }

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

    public static EventHandler<MouseEvent> getProductMouseEvent(MainViewModel mainViewModel, ListView<String> services,
            Entry<Appointment> newEntry, ListView<String> petList, AnchorPane selectedProductPane,
            Text selectedProductLabel, Text selectedProductPriceLabel, Text selectedProductDurationLabel,
            Button deleteSelectedProduct, ListView extraProducts) {
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
            if (services.getSelectionModel().getSelectedItem().contains("+")) {
                try {
                    // NEW SERVICE POPUP
                    Stage stage = new Stage();
                    stage.setOnHidden(getProductEvent(mainViewModel, services, newEntry));
                    ControllerUtilities.newProduct(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (services.getSelectionModel().getSelectedItem() != null) {
                    Calendar<Product> productCalendar = mainViewModel.getCalendarMap()
                            .get(services.getSelectionModel().getSelectedItem());
                    newEntry.setCalendar(productCalendar);
                    newEntry.setTitle(productCalendar.getName()); // TODO: muuta käyttöä titlelle
                    services.scrollTo(services.getSelectionModel().getSelectedItem());
                    petList.setDisable(false);
                    if (selectedProductPane.isVisible() == false) {
                        selectedProductLabel.setText(services.getSelectionModel().getSelectedItem());
                        selectedProductPriceLabel.setText(productCalendar.getUserObject().getPrice() + " €");
                        shoppingCart.addProduct(productCalendar.getUserObject(), "");
                        services.getItems().remove(services.getSelectionModel().getSelectedItem());
                        services.getSelectionModel().clearSelection();
                        selectedProductPane.setVisible(true);
                    } else {
                        // extraProductObservableList.add(productCalendar.getUserObject());
                        shoppingCart.addProduct(productCalendar.getUserObject(), "");
                        extraProducts.getItems().add(productCalendar.getUserObject());
                        System.out.println(shoppingCart.getDiscount(productCalendar.getUserObject()) + " " + shoppingCart.getTotalDiscountedPrice());
                        services.getItems().remove(services.getSelectionModel().getSelectedItem());
                        services.getSelectionModel().clearSelection();
                    }
                }
            }
        };
    }

    public static ShoppingCart getShoppingCart(){
        return shoppingCart;
    }

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
