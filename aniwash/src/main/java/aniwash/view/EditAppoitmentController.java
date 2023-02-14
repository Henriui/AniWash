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

    public void initialize() {
        setArg();

        // Initialize datepicker with selected date

        date.setValue(newEntry.getEntry().getStartDate());
        startTime.setValue(newEntry.getEntry().getStartTime());
        endTime.setValue(newEntry.getEntry().getEndTime());

        // Initialize the person table with the three columns.

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Add data to the table

        ObservableList<Customer> people = getPeople();
        servicesa = products.getCalendars();
        
        servicesa.forEach(service -> {
            services.getItems().addAll(service.getName());
        });


        // Wrap the ObservableList in a FilteredList (initially display all data).

        FilteredList<Customer> filteredData = new FilteredList<>(people, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (person.getPhone().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });

        });

        // Wrap the FilteredList in a SortedList.

        SortedList<Customer> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(personView.comparatorProperty());

        // Add sorted (and filtered) data to the table.

        personView.setItems(sortedData);
        personView.getColumns().addAll(firstNameColumn, phoneColumn, emailColumn);

        // Set the selection model to allow only one row to be selected at a time.

        searchField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                personView.getSelectionModel().select(0);
                System.out.println("Enter pressed");
                if (filteredData.isEmpty()) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("TESTI");
                    alert.setHeaderText("CREATE NEW CUSTOMER");
                    alert.setContentText("WOW");
                    alert.showAndWait();
                }
            }
        });

        // Listen for customer selection changes and show the person details when
        // changed.
    
        personView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Customer selectedPerson = (Customer) newValue;
                selectCustomer(selectedPerson);
                services.setDisable(false);

                selectedPerson.getAnimals().forEach(animal -> {
                    petList.getItems().addAll(animal.getDescription());
                });
            }
        });

        // Listen for service selection changes and show the person details when
        // changed.

        services.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Selected item: " + newValue);
            int selectedIndex = services.getSelectionModel().getSelectedIndex();
            selectService(newValue, selectedIndex);
            petList.setDisable(false);
        });

        // Listen for pet selection changes and show the person details when changed.

        petList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Selected item: " + newValue);
            if (newValue.contains("Create new pet")) {
                System.out.println("Create new pet");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("TESTI");
                alert.setHeaderText("CREATE NEW CUSTOMER");
                alert.setContentText("WOW");
                alert.showAndWait();
            }
            else{
                newEntry.getEntry().setLocation(newEntry.getEntry().getLocation() + " " + newValue);
            }
        });
        getInfo();

    }

    // Save the selected person and send entry .

    @FXML
    public void save() {
        newEntry.getEntry().setInterval(date.getValue(), startTime.getValue(), date.getValue(), endTime.getValue());
        EntryDetailsParameter temp = newEntry;
        if(newEntry.getEntry().getLocation() == null || newEntry.getEntry().getTitle().contains("New Entry")){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("TESTI");
            alert.setHeaderText("CREATE NEW CUSTOMER");
            alert.setContentText("WOW");
            alert.showAndWait();
        }
        else{
            System.out.println("save");
            Stage stage = (Stage) save.getScene().getWindow();
            stage.close();
            sendEntry();
        }
    }

    // Set entrys "Location" which is used to store customer name and pet.

    private void selectCustomer(Customer customer) {
        System.out.println("Selected person: " + customer.getName() + " "
                + customer.getEmail() + " " + customer.getId());
        newEntry.getEntry().setLocation(customer.getName());
        newEntry.getEntry().setId(String.valueOf(customer.getId()));
    }

    // Set entrys "Title" which is used to store service name.

    private void selectService(String newValue, int selectedIndex) {
        if (newValue.contains("Create new service")) {
            System.out.println("Create new service");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("TESTI");
            alert.setHeaderText("CREATE NEW CUSTOMER");
            alert.setContentText("WOW");
            alert.showAndWait();
        } else {
            Calendar service = servicesa.get(selectedIndex);
            newEntry.getEntry().setCalendar(service);
            newEntry.getEntry().setTitle(service.getName());

        }

    }

    public void getInfo(){
        personView.getSelectionModel().select(Integer.parseInt("3"));
        personView.scrollTo(3);
        int indexOfItemToSelect = services.getItems().indexOf(newEntry.getEntry().getCalendar().getName());
        System.out.println("!!" + newEntry.getEntry().getCalendar().getName() + "!! " + indexOfItemToSelect);
        services.getSelectionModel().select(indexOfItemToSelect);

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
        // entry.setId(String.valueOf(newEntry.getEntry().getId()));
        System.out.println("and this is? " + entry.getId());
        entry.setUserObject(selectedCustomer);
        products.addAppoitmEntry(entry, servicesa.get(selectedProduc));
        servicesa.get(selectedProduc+1).addEntry(entry);
        servicesa.get(selectedProduc).removeEntry(newEntry.getEntry());
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

        return customers;
    }
}
