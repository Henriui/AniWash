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
    private Customer selectedCustomer = null;
    private Customer selectedPerson;
    private ObservableList<Customer> people;
    private ObservableList<Customer> allPeople;

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

        petList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.contains("Create new pet")) {
                System.out.println("Create new pet");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("TESTI");
                alert.setHeaderText("CREATE NEW CUSTOMER");
                alert.setContentText("WOW");
                alert.showAndWait();
            } else {
                //newEntry.getEntry().setLocation(newEntry.getEntry().getLocation() + " " + newValue);
            }
        });
        services.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            int selectedIndex = services.getSelectionModel().getSelectedIndex();
            selectService(newValue, selectedIndex);
            petList.setDisable(false);
        });

        getInfo();
    }

    // Save the selected person and send entry .

    @FXML
    public void save() {
        newEntry.getEntry().setInterval(date.getValue(), startTime.getValue(), date.getValue(), endTime.getValue());
        if (newEntry.getEntry().getLocation() == null || newEntry.getEntry().getTitle().contains("New Entry")
                || petList.getSelectionModel().getSelectedIndex() == -1) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("TESTI");
            alert.setHeaderText("CREATE NEW CUSTOMER");
            alert.setContentText("WOW");
            alert.showAndWait();
        } else {
            Stage stage = (Stage) save.getScene().getWindow();
            stage.close();
            sendEntry();
        }
    }

    @FXML
    public void modifyEntry() {
        allPeople = getPeople();
        personView.getSelectionModel().clearSelection();
        petList.getSelectionModel().clearSelection();

        FilteredList<Customer> filteredData = new FilteredList<>(allPeople, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            petList.getSelectionModel().clearSelection();
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

        // Set the selection model to allow only one row to be selected at a time.

        searchField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                personView.getSelectionModel().select(0);
                if (filteredData.isEmpty()) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("TESTI");
                    alert.setHeaderText("CREATE NEW CUSTOMER");
                    alert.setContentText("WOW");
                    alert.showAndWait();
                } else {
                    ObservableList<String> items = petList.getItems();
                    items.removeAll(items.subList(1, items.size()));

                    selectedPerson.getAnimals().forEach(animal -> {
                        petList.getItems().addAll(animal.getDescription());
                    });
                }
            } else if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                // personView.getSelectionModel().clearSelection();
            }
        });

        personView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedPerson = (Customer) newValue;
                selectCustomer(selectedPerson);
                services.setDisable(false);
            }
        });

    }
    // Set entrys "Location" which is used to store customer name and pet.

    private void selectCustomer(Customer customer) {
        selectedCustomer = customer;

        newEntry.getEntry().setLocation(customer.getName());
        // newEntry.getEntry().setId(String.valueOf(customer.getId()));
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
            Calendar service = servicesa.get(selectedIndex - 1);
            newEntry.getEntry().setCalendar(service);
            newEntry.getEntry().setTitle(service.getName());
            selectedProduc = selectedIndex;
        }
    }

    public void getInfo() {

        ObservableList<Customer> tempCustomer = FXCollections.observableArrayList();
        Customer customer = people.get(Integer.parseInt(newEntry.getEntry().getId()));
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

    public void sendEntry() {
        newEntry.getEntry().changeStartDate(newEntry.getEntry().getStartDate());
        newEntry.getEntry().changeStartTime(newEntry.getEntry().getStartTime());
        newEntry.getEntry().changeEndDate(newEntry.getEntry().getStartDate());
        newEntry.getEntry().changeEndTime(newEntry.getEntry().getEndTime());
        newEntry.getEntry().setLocation(newEntry.getEntry().getLocation());
        newEntry.getEntry().setTitle(newEntry.getEntry().getTitle());

        if (selectedCustomer == null) {
            newEntry.getEntry().setId(String.valueOf(newEntry.getEntry().getId()));
        } else {
            Entry asd = newEntry.getEntry();
            asd.setUserObject(selectedPerson);
            newEntry.getEntry().setId(String.valueOf(selectedCustomer.getId()));
            // newEntry.getEntry().setLocation(newEntry.getEntry().getLocation());
        }
        System.out.println("tuutko tähän");
        System.out.println("entrys id this is?!?!??!?! " + newEntry.getEntry().getId());
        // newEntry.getEntry().setUserObject(selectedCustomer);

        products.addAppoitmEntry(newEntry.getEntry(),
                servicesa.get(services.getSelectionModel().getSelectedIndex() - 1));
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

        long id = 0;
        for (Customer customer : customers) {
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
