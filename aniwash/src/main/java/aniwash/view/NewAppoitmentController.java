package aniwash.view;

import aniwash.dao.*;
import aniwash.entity.Animal;
import aniwash.entity.Appointment;
import aniwash.entity.Customer;
import aniwash.entity.Product;
import aniwash.resources.model.Calendars;
import aniwash.resources.model.CreatePopUp;
import aniwash.resources.utilities.ControllerUtilities;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DateControl.EntryDetailsParameter;
import com.calendarfx.view.TimeField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;

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
    private DatePicker date = new DatePicker();
    @FXML
    private TimeField startTime = new TimeField();
    @FXML
    private TimeField endTime = new TimeField();
    @FXML
    private Rectangle third;
    private EntryDetailsParameter newEntry;
    private ArrayList<Calendar> servicesa;
    private int selectedProduc;
    private Customer selectedCustomer;
    private Customer selectedPerson;
    // Save the selected person and send entry .

    public void initialize() {

        // Get the created entry from the calendar view.

        EntryDetailsParameter arg0 = getArg();
        newEntry = arg0;

        services.getItems().add("                                   Create new service  +");
        petList.getItems().add("                                   Create new pet  +");

        // Initialize datepicker with selected date
        System.out.println(newEntry.getEntry());

        date.setValue(newEntry.getEntry().getStartDate());
        startTime.setValue(newEntry.getEntry().getStartTime());
        endTime.setValue(newEntry.getEntry().getEndTime());

        // Initialize the person table with the three columns.

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Add data to the table

        servicesa = new ArrayList<>(products.getCalendars().values());
        ObservableList<Customer> people = getPeople();

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

                return person.getName().toLowerCase().contains(lowerCaseFilter);
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
                        // NEW CUSTOMER POPUP
                        ControllerUtilities.newCustomer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ObservableList<String> items = petList.getItems();
                    items.removeAll(items.subList(1, items.size()));

                    selectedPerson.getAnimals().forEach(animal -> {
                        petList.getItems().addAll(animal.getName());
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
                    // NEW ANIMAL POPUP
                    // ControllerUtilities.newAnimal(selectedPerson);
                } finally {
                    update();
                }
            } else {
                newEntry.getEntry().setLocation(newValue);
                three.styleProperty().set("-fx-fill: #47c496");
            }
        });

    }

    // Set entrys "Location" which is used to store customer name and pet.

    @FXML
    public void save() {
        newEntry.getEntry().setInterval(date.getValue(), startTime.getValue(), date.getValue(), endTime.getValue());
        if (newEntry.getEntry().getLocation() == null || newEntry.getEntry().getTitle().contains("New Entry") || petList.getSelectionModel().getSelectedIndex() == -1) {
            System.out.println("Please select Service and Pet");
        } else {
            Stage stage = (Stage) save.getScene().getWindow();
            stage.close();
            sendEntry();
        }
    }

    @FXML
    public void textChanged() {
        personView.getSelectionModel().clearSelection();
    }

    // Set entrys "Title" which is used to store service name.

    private void selectCustomer(Customer customer) {
        selectedCustomer = customer;
/*
        Animal[] animals = selectedCustomer.getAnimals().toArray(new Animal[selectedCustomer.getAnimals().size()]);
        newEntry.getEntry().setLocation(animals[petList.getSelectionModel().getSelectedIndex() - 1].getName());
*/
    }

    private void selectService(String newValue, int selectedIndex) {
        if (newValue.contains("Create new service")) {
            System.out.println("Create new service");
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
        entry.setId(saveAppointment(newEntry.getEntry().getStartAsZonedDateTime(), newEntry.getEntry().getEndAsZonedDateTime()));
        entry.setLocation(newEntry.getEntry().getLocation());
        entry.setTitle(newEntry.getEntry().getTitle());
        entry.setUserObject(selectedCustomer);
        entry.setCalendar(servicesa.get(selectedProduc - 1));
        newEntry.getEntry().removeFromCalendar();
        products.addAppoitmEntry(entry, servicesa.get(selectedProduc - 1));
    }

    private String saveAppointment(ZonedDateTime start, ZonedDateTime end) {
        Animal[] animals = selectedCustomer.getAnimals().toArray(new Animal[selectedCustomer.getAnimals().size()]);
        IProductDao productDao = new ProductDao();
        Product product = productDao.findByNameProduct(servicesa.get(selectedProduc - 1).getName());

        Appointment appointment = new Appointment(start, end, selectedCustomer.getName() + " " + product.getName());
        appointment.addCustomer(selectedCustomer);
        appointment.addAnimal(animals[petList.getSelectionModel().getSelectedIndex() - 1]);
        appointment.addProduct(product);

        IAppointmentDao appointmentDao = new AppointmentDao();
        appointmentDao.addAppointment(appointment);
        return "id" + appointment.getId();
    }

    private void update() {
/*
        personView.getSelectionModel().select(0);
        ObservableList<String> items = petList.getItems();
        items.removeAll(items.subList(1, items.size()));

        selectedPerson.getAnimals().forEach(animal -> {
            petList.getItems().addAll(animal.getName());
        });
*/
    }

    // Create some sample data.
    // TODO: Replace with real data.
    private ObservableList<Customer> getPeople() {
        ICustomerDao customerDao = new CustomerDao();
        ObservableList<Customer> customers = FXCollections.observableList(customerDao.findAllCustomer());
        return customers;
    }


}
