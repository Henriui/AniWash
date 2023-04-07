package aniwash.view;

import aniwash.entity.Animal;
import aniwash.entity.Appointment;
import aniwash.entity.Customer;
import aniwash.entity.Product;
import aniwash.resources.model.CreatePopUp;
import aniwash.resources.model.CustomListViewCellCustomer;
import aniwash.viewmodels.MainViewModel;
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
import javafx.stage.Stage;

import static aniwash.resources.utilities.ControllerUtilities.*;

public class EditAppointmentController extends CreatePopUp {
    private final MainViewModel mainViewModel = new MainViewModel();
    @FXML
    private Button save;
    @FXML
    private ListView<String> services;
    @FXML
    private ListView<String> petList;
    @FXML
    private ListView<Customer> personList;
    @FXML
    private TextField searchField;
    @FXML
    private AnchorPane servicePane;
    @FXML
    private DatePicker date = new DatePicker();
    @FXML
    private TimeField startTime = new TimeField();
    @FXML
    private TimeField endTime = new TimeField();
    private Entry<Appointment> newEntry;
    private ObservableList<Calendar<Product>> calendarObservableList;
    private ObservableList<Customer> customerObservableList;

    public void initialize() {
        newEntry = (Entry<Appointment>) getArg();
        //services.getItems().add("                                   Create new service  +");
        petList.getItems().add("                                   Create new pet  +");
        date.setValue(newEntry.getStartDate());
        startTime.setValue(newEntry.getStartTime());
        endTime.setValue(newEntry.getEndTime());
        // Initialize the person table with the three columns.
        personList.setCellFactory(personList -> new CustomListViewCellCustomer());
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
        services.setOnMouseClicked(getProductMouseEvent(mainViewModel, services, newEntry, petList));
        petList.setOnMouseClicked(getAnimalMouseEvent(mainViewModel, customerObservableList, personList, petList, newEntry));
        getCurrentAppointment();
    }
    // Save the selected person and send entry .

    @FXML
    public void save() {
        if (personList.getSelectionModel().getSelectedItem() == null || newEntry.getLocation() == null || newEntry.getTitle().contains("New Entry") || petList.getSelectionModel().getSelectedIndex() == -1) {
            System.out.println("Please select a service and a pet");
            // TODO: Alert popup for missing fields ;)
        } else {
            Stage stage = (Stage) save.getScene().getWindow();
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
        services.getSelectionModel().select(newEntry.getCalendar().getName());
        customer.getAnimals().forEach(animal -> petList.getItems().add(animal.getName()));
        petList.getSelectionModel().select(a.getName());
    }

    public void sendEntry() {
        newEntry.setInterval(date.getValue(), startTime.getValue(), date.getValue(), endTime.getValue());
        Appointment appointment = newEntry.getUserObject();
        Customer customer = appointment.getCustomerList().get(0);
        Animal a = appointment.getAnimalList().get(0);
        Product p = (Product) newEntry.getCalendar().getUserObject();
        mainViewModel.updateAppointment(newEntry.getStartAsZonedDateTime(), newEntry.getEndAsZonedDateTime(), newEntry.getUserObject(), customer, a, p);
    }
}
