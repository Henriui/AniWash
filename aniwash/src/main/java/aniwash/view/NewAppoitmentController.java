package aniwash.view;

import java.io.IOException;
import java.util.ArrayList;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DateControl.EntryDetailsParameter;
import com.calendarfx.view.TimeField;

import aniwash.MainApp;
import aniwash.entity.Animal;
import aniwash.entity.Customer;
import aniwash.resources.model.Calendars;
import aniwash.resources.model.CreatePopUp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewAppoitmentController extends CreatePopUp {
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
    private Customer selectedPerson;

    public void initialize() {

        // Get the created entry from the calendar view.

        EntryDetailsParameter arg0 = getArg();
        newEntry = arg0;

        services.getItems().add("                                   Create new service  +");
        petList.getItems().add("                                   Create new pet  +");

        // Initialize datepicker with selected date

        System.out.println("!!!!!!!!!!!!!!!!!!!!!" + newEntry.getEntry().getStartDate());
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

        // Add data to the service list

        servicesa.forEach(service -> {
            services.getItems().addAll(service.getName());
        });

        // Wrap the ObservableList in a FilteredList (initially display all data).

        FilteredList<Customer> filteredData = new FilteredList<>(people, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            petList.getSelectionModel().clearSelection();
            personView.getSelectionModel().clearSelection();
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
                if (filteredData.isEmpty()) {
                    try {
                        newCustomer();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    ObservableList<String> items = petList.getItems();
                    items.removeAll(items.subList(1, items.size()));

                    selectedPerson.getAnimals().forEach(animal -> {
                        petList.getItems().addAll(animal.getDescription());
                    });
                }
            }
        });

        // Listen for customer selection changes and show the person details when
        // changed.

        personView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedPerson = (Customer) newValue;
                selectCustomer(selectedPerson);
                services.setDisable(false);
                one.styleProperty().set("-fx-fill: #47c496");
                first.styleProperty().set("-fx-fill: #47c496");
            }

        });

        // Listen for service selection changes and show the person details when
        // changed.

        services.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int selectedIndex = services.getSelectionModel().getSelectedIndex();
            selectService(newValue, selectedIndex);
            petList.setDisable(false);
            two.styleProperty().set("-fx-fill: #47c496");
            second.styleProperty().set("-fx-fill: #47c496");
        });

        // Listen for pet selection changes and show the person details when changed.

        petList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains("Create new pet")) {
                try {
                    newAnimal();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                newEntry.getEntry().setLocation(newEntry.getEntry().getLocation() + " " + newValue);
                three.styleProperty().set("-fx-fill: #47c496");
            }
        });

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

    // Set entrys "Location" which is used to store customer name and pet.

    private void selectCustomer(Customer customer) {
        selectedCustomer = customer;

        newEntry.getEntry().setLocation(customer.getName());
    }

    // Set entrys "Title" which is used to store service name.

    private void selectService(String newValue, int selectedIndex) {
        if (newValue.contains("Create new service")) {
            try {
                newProduct();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Calendar service = servicesa.get(selectedIndex - 1);
            newEntry.getEntry().setCalendar(service);
            newEntry.getEntry().setTitle(service.getName());
            selectedProduc = selectedIndex;
        }

    }

    public void sendEntry() {
        Entry<Object> entry = new Entry();
        entry.changeStartDate(newEntry.getEntry().getStartDate());
        entry.changeStartTime(newEntry.getEntry().getStartTime());
        entry.changeEndDate(newEntry.getEntry().getStartDate());
        entry.changeEndTime(newEntry.getEntry().getEndTime());
        entry.setLocation(newEntry.getEntry().getLocation());
        entry.setTitle(newEntry.getEntry().getTitle());

        entry.setId(String.valueOf(selectedCustomer.getId()));
        entry.setUserObject(selectedCustomer);

        products.addAppoitmEntry(entry, servicesa.get(selectedProduc - 1));
        newEntry.getEntry().removeFromCalendar();
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

    @FXML
    public void textChanged() {
        personView.getSelectionModel().clearSelection();
    }

    public void newCustomer() throws IOException {
        final FXMLLoader loader;
        final Scene scene;

        loader = loadFXML("newCustomerView");
        scene = new Scene((javafx.scene.Parent) loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Create Customer");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        stage.setOnHidden(event -> {
            // TODO: Get customers from database so the listview reloads
        });
    }

    public void newAnimal() throws IOException {
        final FXMLLoader loader;
        final Scene scene;

        loader = loadFXML("createNewAnimalView");
        scene = new Scene((javafx.scene.Parent) loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Create Animal");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        CreateNewAnimalController.setCustomer(selectedPerson);

        stage.setOnHidden(event -> {
            // TODO: Get customers from database so the listview reloads
        });
    }

    public void newProduct() throws IOException {
        final FXMLLoader loader;
        final Scene scene;

        loader = loadFXML("newProductView");
        scene = new Scene((javafx.scene.Parent) loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Create Product");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        CreateNewAnimalController.setCustomer(selectedPerson);

        stage.setOnHidden(event -> {
            // TODO: Get customers from database so the listview reloads
        });
    }

    protected static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/" + fxml + ".fxml"));
        return fxmlLoader;
    }
}
