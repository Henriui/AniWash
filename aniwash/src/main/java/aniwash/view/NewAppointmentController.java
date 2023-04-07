package aniwash.view;

import aniwash.entity.Animal;
import aniwash.entity.Appointment;
import aniwash.entity.Customer;
import aniwash.entity.Product;
import aniwash.resources.model.CreatePopUp;
import aniwash.resources.model.CustomListViewCellCustomer;
import aniwash.viewmodels.MainViewModel;
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
import javafx.stage.Stage;

import static aniwash.resources.utilities.ControllerUtilities.*;

public class NewAppointmentController extends CreatePopUp {

    private final MainViewModel mainViewModel = new MainViewModel();
    @FXML
    private Button save;
    @FXML
    private ListView<String> services, petList;
    @FXML
    private ListView<Customer> personList;
    @FXML
    private TextField searchField;
    @FXML
    private AnchorPane servicePane;
    @FXML
    private Circle one, two, three;
    @FXML
    private Rectangle first, second, third;
    @FXML
    private DatePicker date;
    @FXML
    private TimeField startTime, endTime;
    private Entry<Appointment> newEntry;
    private ObservableList<Customer> customerObservableList;

    public void initialize() {
        // Get the created entry from the calendar view.
        newEntry = (Entry<Appointment>) getArg();
        newEntry.setHidden(true);
        //services.getItems().add("                                   Create new service  +");
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
        personList.setStyle("-fx-background-color:  #d7d7d7; -fx-background:  #d7d7d7;");
        // Set the placeholder text for the ListView
        Background background = new Background(new BackgroundFill(Color.web("#d7d7d7"), CornerRadii.EMPTY, Insets.EMPTY));
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
            personList.setItems(customerObservableList.filtered(person -> person.getName().toLowerCase().contains(newValue.toLowerCase())));
            if (newValue.isEmpty())
                personList.setItems(null);
        });
        searchField.setOnKeyPressed(getSearchFieldKeyEvent(mainViewModel, searchField, personList, customerObservableList, petList, services, newEntry));
        personList.setOnMouseClicked(getPersonMouseEvent(mainViewModel, customerObservableList, personList, petList, newEntry, services));
        services.setOnMouseClicked(getProductMouseEvent(mainViewModel, services, newEntry, petList));
        petList.setOnMouseClicked(getAnimalMouseEvent(mainViewModel, customerObservableList, personList, petList, newEntry));
    }

    @FXML
    public void save() {
        if (personList.getSelectionModel().getSelectedItem() == null || newEntry.getLocation() == null || newEntry.getTitle().contains("New Entry") || petList.getSelectionModel().getSelectedIndex() == -1) {
            System.out.println("Please select Service and Pet");
            // TODO: Alert popup for missing fields ;)
        } else {
            Stage stage = (Stage) save.getScene().getWindow();
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
        Animal animal = selectedCustomer.getAnimals().toArray(new Animal[0])[petList.getSelectionModel().getSelectedIndex() - 1];
        newEntry.setUserObject(mainViewModel.createAppointment(newEntry.getStartAsZonedDateTime(), newEntry.getEndAsZonedDateTime(), selectedCustomer, animal, (Product) newEntry.getCalendar().getUserObject()));
        newEntry.setId("id" + newEntry.getUserObject().getId());
        newEntry.setHidden(false);
    }

}
