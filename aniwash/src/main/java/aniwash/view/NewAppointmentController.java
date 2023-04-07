package aniwash.view;

import aniwash.entity.Animal;
import aniwash.entity.Appointment;
import aniwash.entity.Customer;
import aniwash.entity.Product;
import aniwash.entity.ShoppingCart;
import aniwash.resources.model.CreatePopUp;
import aniwash.resources.model.CustomListViewCellCustomer;
import aniwash.resources.model.CustomListViewCellExtraProduct;
import aniwash.resources.model.CustomListViewCellProduct;
import aniwash.resources.model.MainViewModel;
import com.calendarfx.model.Entry;
import com.calendarfx.view.TimeField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static aniwash.resources.utilities.ControllerUtilities.*;

import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

public class NewAppointmentController extends CreatePopUp {
    private final MainViewModel mainViewModel = new MainViewModel();
    @FXML
    private TextArea description, servicePane;
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
    private ListView<Product> extraProducts;
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
    private ShoppingCart cart;

    public void initialize() {
        // Get the created entry from the calendar view.
        cart = getShoppingCart();
        newEntry = (Entry<Appointment>) getArg();
        newEntry.setHidden(true);
        services.getItems().add("                                   Create new service  +");
        petList.getItems().add("                                   Create new pet  +");
        // Initialize datepicker with selected date
        date.setValue(newEntry.getStartDate());
        startTime.setValue(newEntry.getStartTime());
        endTime.setValue(newEntry.getEndTime().plusMinutes(30));
        // Initialize the person table with the three columns.
        personList.setCellFactory(personList -> new CustomListViewCellCustomer());
        // Initialize the extra product table.
        extraProducts.setCellFactory(extraProducts -> new CustomListViewCellExtraProduct(services));
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
                extraProducts));
        petList.setOnMouseClicked(
                getAnimalMouseEvent(mainViewModel, customerObservableList, personList, petList, newEntry));
        deleteSelectedProductBtn.setOnAction(deleteMainProduct());
        applyBtn.setOnAction(applyDiscount());
        extraProducts.setOnMouseClicked(selectExtraProduct());
        mainProductRect.setOnMousePressed(selectMainProduct());
    }

    private EventHandler<? super MouseEvent> selectExtraProduct() {
        return event -> {
            selectedProduct.setFill(Color.web("#000000"));
        };
    }

    private EventHandler<? super MouseEvent> selectMainProduct() {
        return event -> {
            selectedProduct.setFill(Color.web("#47c496"));
            extraProducts.getSelectionModel().clearSelection();
        };
    }


    @FXML
    public void save() {
        if (personList.getSelectionModel().getSelectedItem() == null || newEntry.getLocation() == null
                || newEntry.getTitle().contains("New Entry") || petList.getSelectionModel().getSelectedIndex() == -1) {
            System.out.println("Please select Service and Pet");
            // TODO: Alert popup for missing fields ;)
        } else {
            Stage stage = (Stage) saveBtn.getScene().getWindow();
            stage.close();
            sendEntry();
        }
    }

    @FXML
    public void textChanged() {
        personList.getSelectionModel().clearSelection();
    }

    public void sendEntry() {
        newEntry.setInterval(date.getValue(), startTime.getValue(), date.getValue(), endTime.getValue());
        Customer selectedCustomer = personList.getSelectionModel().getSelectedItem();
        Animal animal = selectedCustomer.getAnimals() 
                .toArray(new Animal[0])[petList.getSelectionModel().getSelectedIndex() - 1];
        newEntry.setUserObject(
                mainViewModel.createAppointment(newEntry.getStartAsZonedDateTime(), newEntry.getEndAsZonedDateTime(),
                        selectedCustomer, animal, (Product) newEntry.getCalendar().getUserObject()));
        newEntry.setId("id" + newEntry.getUserObject().getId());
        newEntry.setHidden(false);
    }

    public EventHandler<ActionEvent> applyDiscount() {
        return event -> {
            if (!setDiscount.getText().isEmpty() && extraProducts.getSelectionModel().getSelectedItem() == null) {
                Product mainProduct = (Product) newEntry.getCalendar().getUserObject();
                String discount = setDiscount.getText();
                double newPrice = mainProduct.getPrice() * (0.01 * Double.parseDouble(discount));

                selectedProductCost.strikethroughProperty().set(true);
                selectedProductCostDiscount.setVisible(true);
                selectedProductCostDiscount.setText(String.format("%.2f", newPrice) + "€");

                cart.editDiscount(mainProduct, discount);
            } else {
                System.out.println("Please select a product" +   extraProducts.getSelectionModel().getSelectedIndex());
                Product product = extraProducts.getSelectionModel().getSelectedItem();
                String discount = setDiscount.getText();
                double newPrice = product.getPrice() * (0.01 * Double.parseDouble(discount));
                System.out.println("!?!?!?!?!?!??!?!?!" + newPrice); 
                //extraProducts.getItems().get(extraProducts.getSelectionModel().getSelectedIndex()).setPrice(newPrice);
                cart.editDiscount(product, discount);
                
                // Get the selected index in the ListView
                //int selectedIndex = extraProducts.getSelectionModel().getSelectedIndex();
                
                //ListCell<Product> seleCell = extraProducts.getSelectionModel().getSelectedItem();
                //seleCell.
                
                // Get a reference to the ListCell that represents the selected item
                //ListCell<Product> selectedCell = extraProducts;

                //ListCell<Product> selectedCell = extraProducts.getCellFactory().call(extraProducts);
                //System.out.println("selectedCell" + selectedCell.getItem()); //antaako ulos productin?
                //selectedCell.getProperties().put("price", newPrice);
                //System.out.println(selectedCell.getProperties());
                
                //((CustomListViewCellExtraProduct) selectedCell).setDiscountedPrice(String.valueOf(newPrice));
                // selectedCellCustomListViewCellExtraProduct@ed347e1[styleClass=cell indexed-cell list-cell]'null'
                // {}

                extraProducts.refresh(); // Should update all the cells in the ListView








                // If the selected cell is not null, call the setDiscountedPrice method on it
              /*   if (selectedCell != null) {
                    ((CustomListViewCellExtraProduct) selectedCell).setDiscountedPrice(newPrice);
                } */
                // Store the discount for the selected product in the cart

            }
        };
    }

    public EventHandler<ActionEvent> deleteMainProduct() {
        return event -> {
            services.getItems().addAll(selectedProduct.getText());
            selectedProductPane.setVisible(false);
            System.out.println("deleteMainProduct" + newEntry.getCalendar().getUserObject());
            cart.removeMainProduct((Product) newEntry.getCalendar().getUserObject());
            selectedProductCost.strikethroughProperty().set(false);
            selectedProductCostDiscount.setVisible(false);
            newEntry.setCalendar(null);
        };

    }
}
