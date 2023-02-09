package aniwash.view;

import com.calendarfx.view.DateControl.EntryDetailsParameter;

import aniwash.entity.Customer;
import aniwash.resources.model.CreatePopUp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class NewAppoitmentController extends CreatePopUp{
    @FXML
    private TableColumn personTable;
    @FXML
    private TableColumn<Customer, String> firstNameColumn = new TableColumn<>("First Name");
    @FXML
    private TableColumn<Customer, String> phoneColumn = new TableColumn<>("Phone number");
    @FXML
    private TableColumn<Customer, String> emailColumn = new TableColumn<>("Email");
    @FXML
    private TableView<Customer> personView;
    @FXML
    private TextField searchField;
    @FXML
    private AnchorPane servicePane;
    @FXML
    private AnchorPane petPane;
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
    private Rectangle third;
    private EntryDetailsParameter newEntry;

    public void initialize() {
        setArg();
        // Initialize the person table with the three columns.

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Add data to the table

        ObservableList<Customer> people = getPeople();

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

            }
        });

        // Listen for selection changes and show the person details when changed.

        personView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Customer selectedPerson = (Customer) newValue;
                selectCustomer(selectedPerson);
                servicePane.setDisable(false);
                one.styleProperty().set("-fx-fill: #47c496");
                first.styleProperty().set("-fx-fill: #47c496");
            }
        });

    }

    // Save the selected person.
    @FXML
    public void test(){
        System.out.println("test");
    }
    private void selectCustomer(Customer customer) {
        // TODO Auto-generated method stub
        System.out.println("Selected person: " + customer.getName() + " "
                + customer.getEmail());
        newEntry.getEntry().setLocation(customer.getName());
        sendEntry();

    }

    public void setArg(){
        System.out.println("arg0");
        EntryDetailsParameter arg0 = getArg();
        System.out.println("newEntry " + arg0.getEntry().getTitle());
        newEntry = arg0;
        System.out.println("asdasdasdasd " + newEntry.getEntry().getTitle());

    }

    public void sendEntry(){
        saveEntry(newEntry);
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
        return customers;
    }
}
