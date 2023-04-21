package aniwash.view.controllers;

import aniwash.entity.*;
import aniwash.view.model.CreatePopUp;
import aniwash.view.model.CustomListViewCellCustomer;
import aniwash.view.model.CustomListViewCellExtraProduct;
import aniwash.viewmodels.DiscountProduct;
import aniwash.viewmodels.MainViewModel;
import aniwash.viewmodels.ShoppingCart;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.TimeField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

import static aniwash.view.utilities.ControllerUtilities.*;

public class EditAppointmentController extends CreatePopUp {

    private final MainViewModel mainViewModel = new MainViewModel();

    @FXML
    private TextArea description, servicePane;
    @FXML
    private AnchorPane selectedProductPane;
    @FXML
    private Text selectedProductTitle, selectedProduct, selectedProductCost, selectedProductCostDiscount, extraProductTitle, priceText, setDiscountTitle;
    @FXML
    private TextField setDiscount, searchField;
    @FXML
    private Button deleteSelectedProductBtn, saveBtn, applyBtn;
    @FXML
    private ListView<DiscountProduct> extraProducts;
    @FXML
    private ListView<String> services, petList;
    @FXML
    private ListView<Customer> personList;
    @FXML
    private Rectangle mainProductRect;
    @FXML
    private DatePicker date = new DatePicker();
    @FXML
    private TimeField startTime = new TimeField();
    @FXML
    private TimeField endTime = new TimeField();
    private Entry<Appointment> newEntry;
    private ObservableList<Calendar<Product>> calendarObservableList;
    private ObservableList<Customer> customerObservableList;
    private ShoppingCart cart;

    public void initialize() {
        cart = getShoppingCart();
        newEntry = (Entry<Appointment>) getArg();
        //services.getItems().add("                                   Create new service  +");
        petList.getItems().add("                                   Create new pet  +");
        date.setValue(newEntry.getStartDate());
        startTime.setValue(newEntry.getStartTime());
        endTime.setValue(newEntry.getEndTime());
        // Initialize the person table with the three columns.
        personList.setCellFactory(personList -> new CustomListViewCellCustomer());
        extraProducts.setCellFactory(extraProducts -> new CustomListViewCellExtraProduct(services, priceText, cart));
        personList.setStyle("-fx-background-color: #f4f4f4; -fx-background: #f4f4f4;");
        // Set the placeholder text for the ListView
        Background background = new Background(new BackgroundFill(Color.web("#f4f4f4"), CornerRadii.EMPTY, Insets.EMPTY));
        personList.setPlaceholder(new Label("No items") {
            @Override
            protected void updateBounds() {
                super.updateBounds();
                setBackground(background);
            }
        });
        // Add data to the table
        calendarObservableList = FXCollections.observableArrayList(mainViewModel.getCalendarMap().values());
        calendarObservableList.forEach(service -> services.getItems().addAll(service.getName()));
        personList.setOnMouseClicked(getPersonMouseEvent(mainViewModel, customerObservableList, personList, petList, newEntry, services));
        //services.setOnMouseClicked(getProductMouseEvent(mainViewModel, services, newEntry, petList));
        petList.setOnMouseClicked(getAnimalMouseEvent(mainViewModel, customerObservableList, personList, petList, newEntry));
        getCurrentAppointment();

        services.setOnMouseClicked(getProductMouseEvent(mainViewModel, services, newEntry, petList, selectedProductPane, selectedProduct, selectedProductCost, selectedProductCostDiscount, deleteSelectedProductBtn, extraProducts, priceText));
        deleteSelectedProductBtn.setOnAction(deleteMainProduct(services, selectedProductPane, newEntry, selectedProduct, selectedProductCost, selectedProductCostDiscount, priceText));
        applyBtn.setOnAction(applyDiscount(setDiscount, extraProducts, selectedProductCost, selectedProductCostDiscount, newEntry, selectedProduct, priceText));
        extraProducts.setOnMouseClicked(selectExtraProduct(selectedProduct));
        mainProductRect.setOnMousePressed(selectMainProduct(selectedProduct, extraProducts));
    }
    // Save the selected person and send entry .

    @FXML
    public void save() {
        if (personList.getSelectionModel().getSelectedItem() == null || newEntry.getLocation() == null || newEntry.getTitle().contains("New Entry") || petList.getSelectionModel().getSelectedIndex() == -1) {
            System.out.println("Please select a service and a pet");
            // TODO: Alert popup for missing fields ;)
        } else {
            Stage stage = (Stage) saveBtn.getScene().getWindow();
            stage.close();
            sendEntry();
        }
    }

    @FXML
    public void modifyEntry() {
        customerObservableList = mainViewModel.getPeople();
        personList.setItems(customerObservableList);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null)
                return;
            personList.setItems(customerObservableList.filtered(person -> person.getName().toLowerCase().contains(newValue.toLowerCase())));
            if (newValue.isEmpty())
                personList.setItems(null);
        });
        // Set the selection model to allow only one row to be selected at a time.
        searchField.setOnKeyPressed(getSearchFieldKeyEvent(mainViewModel, searchField, personList, customerObservableList, petList, services, newEntry));
    }

    public void getCurrentAppointment() {
        customerObservableList = mainViewModel.getPeople();
        Appointment appointment = newEntry.getUserObject();
        Customer customer = appointment.getCustomerList().get(0);
        Animal a = appointment.getAnimalList().get(0);
        personList.setItems(customerObservableList.filtered(person -> person.getName().equals(customer.getName())));
        personList.getSelectionModel().select(customer);
        services.getItems().remove(newEntry.getCalendar().getName());

        selectedProduct.setText(newEntry.getCalendar().getName());
        selectedProductCost.setText(((Product) newEntry.getCalendar().getUserObject()).getPrice() + " €");

        customer.getAnimals().forEach(animal -> petList.getItems().add(animal.getName()));
        petList.getSelectionModel().select(a.getName());
        priceText.setText("Price: " + ((Product) newEntry.getCalendar().getUserObject()).getPrice() + " €");
        selectedProductPane.setVisible(true);
    }

    public void sendEntry() {
        newEntry.setInterval(date.getValue(), startTime.getValue(), date.getValue(), endTime.getValue());
        Appointment appointment = newEntry.getUserObject();
        Customer customer = appointment.getCustomerList().get(0);
        Animal a = appointment.getAnimalList().get(0);

        //MainProductId
        appointment.setMainProductId(((Product) newEntry.getCalendar().getUserObject()).getId());
        /*TODO: Use this map to get the discount for the product
            new Discount(long productId, double amount); */
        Map<Product, Discount> p = new HashMap<>();
        p.put((Product) newEntry.getCalendar().getUserObject(), new Discount(((Product) newEntry.getCalendar().getUserObject()).getId(), 0));
        mainViewModel.updateAppointment(newEntry.getStartAsZonedDateTime(), newEntry.getEndAsZonedDateTime(), newEntry.getUserObject(), customer, a, p);
    }

}
