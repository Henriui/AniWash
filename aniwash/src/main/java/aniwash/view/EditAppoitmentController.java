package aniwash.view;

import java.time.ZonedDateTime;
import java.util.ArrayList;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.TimeField;
import com.calendarfx.view.DateControl.EntryDetailsParameter;

import aniwash.entity.Animal;
import aniwash.entity.Customer;
import aniwash.resources.model.Calendars;
import aniwash.resources.model.CreatePopUp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class EditAppoitmentController extends CreatePopUp {
    private Calendars products = new Calendars();
    @FXML
    private TableColumn personTable;
    @FXML
    private Button save;
    @FXML
    private TableColumn<Customer, String> firstNameColumn = new TableColumn<>("First Name");
    @FXML
    private TableColumn<Customer, String> phoneColumn = new TableColumn<>("Phone number");
    @FXML
    private TableColumn<Customer, String> emailColumn = new TableColumn<>("Email");
    @FXML
    private TableView<Customer> personView;
    @FXML
    private ListView<String> services;
    @FXML
    private ListView<String> petList;
    @FXML
    private TextField searchField;
    @FXML
    private AnchorPane servicePane;
    @FXML
    private Circle one;
    @FXML
    private Circle two;
    @FXML
    private Circle three;
    @FXML
    private Rectangle first;
    @FXML
    private Rectangle second;
    @FXML
    private DatePicker date;
    @FXML
    private TimeField startTime;
    @FXML
    private TimeField endTime;
    @FXML
    private Rectangle third;
    private EntryDetailsParameter newEntry;
    private ArrayList<Calendar> servicesa;
    private int selectedProduc;
    private Customer selectedCustomer;
    private ObservableList<Customer> people;
    public void initialize() {
        setArg();
        
        services.getItems().add("                                   Create new service  +");
        petList.getItems().add("                                   Create new pet  +");

        date.setValue(newEntry.getEntry().getStartDate());
        startTime.setValue(newEntry.getEntry().getStartTime());
        endTime.setValue(newEntry.getEntry().getEndTime());

        // Initialize the person table with the three columns.

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Add data to the table

        people = getPeople();
        servicesa = products.getCalendars();

        servicesa.forEach(service -> {
            services.getItems().addAll(service.getName());
        });
        
        personView.getColumns().addAll(firstNameColumn, phoneColumn, emailColumn);
        getInfo();

    }

    // Save the selected person and send entry .

    @FXML
    public void save() {

    }

    // Set entrys "Location" which is used to store customer name and pet.

    private void selectCustomer(Customer customer) {

    }

    // Set entrys "Title" which is used to store service name.

    private void selectService(String newValue, int selectedIndex) {

    }

    public void getInfo() {
        System.out.println("getInfo " + newEntry.getEntry().getId());

        ObservableList<Customer> tempCustomer = FXCollections.observableArrayList();
        Customer customer = people.get(Integer.parseInt(newEntry.getEntry().getId())- 1);
        tempCustomer.add(0, customer);
        personView.setItems(tempCustomer);
        personView.getSelectionModel().select(0);

        int indexOfItemToSelect = services.getItems().indexOf(newEntry.getEntry().getCalendar().getName());

        services.getSelectionModel().select(indexOfItemToSelect);
        services.scrollTo(indexOfItemToSelect);

        customer.getAnimals().forEach(animal -> {
            petList.getItems().addAll(animal.getDescription());
        });

        String textToMatch = newEntry.getEntry().getLocation();
        System.out.println("textToMatch " + textToMatch);
        for (int i = 0; i < petList.getItems().size(); i++) {
            String itemText = petList.getItems().get(i);
            if (textToMatch.contains(itemText)) {
                System.out.println("petList " + itemText);
                petList.getSelectionModel().select(i);
                break;
            }
        }

    }
    // Get infromation about the entry

    public void setArg() {
        EntryDetailsParameter arg0 = getArg();
        newEntry = arg0;
    }

    // FIXME: This is a temporary method.

    public void sendEntry() {
        Entry<Object> entry = new Entry();
        entry.changeStartDate(newEntry.getEntry().getStartDate());
        entry.changeStartTime(newEntry.getEntry().getStartTime());
        entry.changeEndDate(newEntry.getEntry().getStartDate());
        entry.changeEndTime(newEntry.getEntry().getEndTime());
        entry.setLocation(newEntry.getEntry().getLocation());
        entry.setTitle(newEntry.getEntry().getTitle());

        System.out.println("newEntrys id this is?!?!??!?! " + newEntry.getEntry().getId());
        entry.setId(String.valueOf(selectedCustomer.getId()));
        System.out.println("tuutko tähän");
        System.out.println("entrys id this is?!?!??!?! " + selectedCustomer);
        entry.setUserObject(selectedCustomer);

        // products.addAppoitmEntry(entry, servicesa.get(selectedProduc));

        // servicesa.get(selectedProduc).removeEntry(newEntry.getEntry());
    }

    // Create some sample data.
    // TODO: Replace with real data.
    private ObservableList<Customer> getPeople() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        customers.add(new Customer("name", "phone", "email"));
        customers.add(new Customer("asd1", "112", "jonne.borgman@metropolia.if"));
        customers.add(new Customer("asd2", "112", "jonne.borgman@metropolia.if"));
        customers.add(new Customer("asd3", "112", "jonne.borgman@metropolia.if"));
        customers.add(new Customer("asd4", "112", "jonne.borgman@metropolia.if"));
        customers.add(new Customer("asd5", "112", "jonne.borgman@metropolia.if"));
        customers.add(new Customer("asd6", "112", "jonne.borgman@metropolia.if"));

        for (Customer customer : customers) {
            long id = 1;
            customer.setId(id);
            id++;
        }
        customers.get(0).addAnimal(new Animal("dog", "dog", "dog", 10, "koere"));
        customers.get(1).addAnimal(new Animal("dog", "dog", "dog", 10, "asd"));
        customers.get(2).addAnimal(new Animal("dog", "dog", "dog", 10, "dsa"));
        customers.get(3).addAnimal(new Animal("dog", "dog", "dog", 10, "qew"));
        customers.get(4).addAnimal(new Animal("dog", "dog", "dog", 10, "qew"));
        customers.get(5).addAnimal(new Animal("dog", "dog", "dog", 10, "qew"));

        return customers;
    }
}
