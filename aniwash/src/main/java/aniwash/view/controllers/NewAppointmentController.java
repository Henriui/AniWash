package aniwash.view.controllers;

import aniwash.entity.Animal;
import aniwash.entity.Appointment;
import aniwash.entity.Customer;
import aniwash.entity.Product;
import aniwash.view.elements.CreatePopUp;
import aniwash.view.elements.CustomListViewCellCustomer;
import aniwash.view.elements.CustomListViewCellExtraProduct;
import aniwash.viewmodels.DiscountProduct;
import aniwash.viewmodels.MainViewModel;
import aniwash.viewmodels.ShoppingCart;
import com.calendarfx.model.Entry;
import com.calendarfx.view.TimeField;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static aniwash.view.utilities.ControllerUtilities.*;

public class NewAppointmentController extends CreatePopUp {

    private final MainViewModel mainViewModel = new MainViewModel();
    @FXML
    private TextArea descriptionArea, servicePane;
    @FXML
    private AnchorPane selectedProductPane;
    @FXML
    private Text selectedProductTitle, selectedProduct, selectedProductCost, selectedProductCostDiscount,
            extraProductTitle, priceText, setDiscountTitle;
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
    private Circle one, two, three;
    @FXML
    private Rectangle first, second, third, mainProductRect;
    @FXML
    private DatePicker date = new DatePicker();
    @FXML
    private TimeField startTime = new TimeField();
    @FXML
    private TimeField endTime = new TimeField();

    private Entry<Appointment> newEntry;
    private ObservableList<Customer> customerObservableList;
    private ShoppingCart cart = new ShoppingCart();

    public void initialize() {
        // Get the created entry from the calendar view.
        newEntry = (Entry<Appointment>) getArg();
        newEntry.setHidden(true);
        petList.getItems().add("                                   Create new pet  +");
        // Initialize datepicker with selected date
        date = new DatePicker();
        date.setValue(newEntry.getStartDate());
        startTime = new TimeField();
        endTime = new TimeField();
        startTime.setValue(newEntry.getStartTime());
        endTime.setValue(newEntry.getEndTime().plusMinutes(30));
        // Initialize the person table with the three columns.
        personList.setCellFactory(personList -> new CustomListViewCellCustomer());
        // Initialize the extra product table.
        extraProducts.setCellFactory(extraProducts -> new CustomListViewCellExtraProduct(services, priceText, cart,
                newEntry.getUserObject()));
        personList.setStyle("-fx-background-color:  #d7d7d7; -fx-background:  #d7d7d7;");
        // Set the placeholder text for the ListView
        Background background = new Background(
                new BackgroundFill(Color.web("#d7d7d7"), CornerRadii.EMPTY, Insets.EMPTY));
        personList.setPlaceholder(new Label("No items") {
            @Override
            protected void updateBounds() {
                super.updateBounds();
                setBackground(background);
            }
        });

        mainViewModel.getCalendarMap().values().forEach(service -> services.getItems().addAll(service.getName()));
        services.getSelectionModel().select(0);
        // String name = ((Product) newEntry.getCalendar().getUserObject()).getName();
        customerObservableList = mainViewModel.getPeople();
        personList.setItems(null);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null)
                return;
            personList.setItems(customerObservableList
                    .filtered(person -> person.getName().toLowerCase().contains(newValue.toLowerCase())));
            if (newValue.isEmpty())
                personList.setItems(null);
        });
        searchField.setOnKeyPressed(getSearchFieldKeyEvent(mainViewModel, searchField, personList,
                customerObservableList, petList, services, newEntry));
        personList.setOnMouseClicked(
                getPersonMouseEvent(mainViewModel, customerObservableList, personList, petList, newEntry, services));
        services.setOnMouseClicked(getProductMouseEvent(mainViewModel, services, newEntry, petList, selectedProductPane,
                selectedProduct, selectedProductCost, selectedProductCostDiscount, deleteSelectedProductBtn,
                extraProducts, priceText, cart));
        petList.setOnMouseClicked(
                getAnimalMouseEvent(mainViewModel, customerObservableList, personList, petList, newEntry));
        deleteSelectedProductBtn.setOnAction(deleteMainProduct(services, selectedProductPane, newEntry, selectedProduct,
                selectedProductCost, selectedProductCostDiscount, priceText, cart, newEntry.getUserObject()));
        applyBtn.setOnAction(applyDiscount(setDiscount, extraProducts, selectedProductCost, selectedProductCostDiscount,
                newEntry, selectedProduct, priceText, cart));
        extraProducts.setOnMouseClicked(selectExtraProduct(selectedProduct));
        mainProductRect.setOnMousePressed(selectMainProduct(selectedProduct, extraProducts));
    }

    @FXML
    public void save() {
        if ((personList.getSelectionModel().getSelectedItem() == null || newEntry.getLocation() == null
             || newEntry.getTitle().contains("New Entry") || petList.getSelectionModel().getSelectedIndex() == -1)
            || !selectedProductPane.isVisible()) {
            System.out.println("Please select Service and Pet");
        } else {
            Stage stage = (Stage) saveBtn.getScene().getWindow();
            stage.close();
            sendEntry();
        }
    }

    public void sendEntry() {
        newEntry.setInterval(date.getValue(), startTime.getValue(), date.getValue(), endTime.getValue());
        Customer selectedCustomer = personList.getSelectionModel().getSelectedItem();
        Animal animal = selectedCustomer.getAnimals()
                .toArray(new Animal[0])[petList.getSelectionModel().getSelectedIndex() - 1];
        newEntry.setUserObject(mainViewModel.createAppointment(newEntry.getStartAsZonedDateTime(),
                newEntry.getEndAsZonedDateTime(), selectedCustomer, animal,
                ((Product) newEntry.getCalendar().getUserObject()).getId(), cart.getProductList(), descriptionArea));
        newEntry.setId("id" + newEntry.getUserObject().getId());
        newEntry.setLocation(null);
        newEntry.setHidden(false);
    }

}
