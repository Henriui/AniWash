package aniwash.view;

import aniwash.dao.*;
import aniwash.entity.Animal;
import aniwash.entity.Appointment;
import aniwash.entity.Customer;
import aniwash.entity.Product;
import aniwash.resources.model.Calendars;
import aniwash.resources.model.CreatePopUp;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DateControl.EntryDetailsParameter;
import com.calendarfx.view.TimeField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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

        date.setValue(newEntry.getEntry().getStartDate());
        startTime.setValue(newEntry.getEntry().getStartTime());
        endTime.setValue(newEntry.getEntry().getEndTime());

        // Initialize the person table with the three columns.

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Add data to the table

        // ObservableList<Customer> people = getPeople();


        servicesa = new ArrayList<>(products.getCalendars().values());
        ICustomerDao customerDao = new CustomerDao();

        // Add data to the service list

        servicesa.forEach(service -> {
            services.getItems().addAll(service.getName());
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            petList.getSelectionModel().clearSelection();
            personView.getSelectionModel().clearSelection();
            if (newValue == null || newValue.isEmpty()) {
                personView.setItems(FXCollections.observableList(customerDao.findAllCustomer()));

            } else {
                List<Customer> cL = customerDao.findByNameCustomerList(newValue);
                personView.setItems(FXCollections.observableList(cL));
                if (cL != null && !cL.isEmpty()) {
                    ObservableList<String> items = petList.getItems();
                    items.removeAll(items.subList(1, items.size()));
                    cL.get(0).getAnimals().forEach(animal -> {
                        petList.getItems().addAll(animal.getName());
                    });
                }
            }
        });

        personView.getColumns().addAll(firstNameColumn, phoneColumn, emailColumn);

        searchField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                personView.getSelectionModel().select(0);
                if (personView.getItems().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("TESTI");
                    alert.setHeaderText("CREATE NEW CUSTOMER");
                    alert.setContentText("WOW");
                    alert.showAndWait();
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
                System.out.println("Create new pet");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("TESTI");
                alert.setHeaderText("CREATE NEW CUSTOMER");
                alert.setContentText("WOW");
                alert.showAndWait();
            } else {
                newEntry.getEntry().setLocation(newEntry.getEntry().getLocation() + " " + newValue);
                three.styleProperty().set("-fx-fill: #47c496");
            }
        });


        // Wrap the ObservableList in a FilteredList (initially display all data).

/*
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
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("TESTI");
                    alert.setHeaderText("CREATE NEW CUSTOMER");
                    alert.setContentText("WOW");
                    alert.showAndWait();
                }else{
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
                System.out.println("Create new pet");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("TESTI");
                alert.setHeaderText("CREATE NEW CUSTOMER");
                alert.setContentText("WOW");
                alert.showAndWait();
            } else {
                newEntry.getEntry().setLocation(newEntry.getEntry().getLocation() + " " + newValue);
                three.styleProperty().set("-fx-fill: #47c496");
            }
        });
*/

    }

    // Save the selected person and send entry .

    @FXML
    public void save() {
        newEntry.getEntry().setInterval(date.getValue(), startTime.getValue(), date.getValue(), endTime.getValue());
        if (newEntry.getEntry().getLocation() == null || newEntry.getEntry().getTitle().contains("New Entry") || petList.getSelectionModel().getSelectedIndex() == -1) {
            //Appointment entry = new Appointment(newEntry.getEntry().getStartAsZonedDateTime(), newEntry.getEntry().getLocation());
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
        //newEntry.getEntry().setId(String.valueOf(customer.getId()));
    }

    // Set entrys "Title" which is used to store service name.

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
        entry.setLocation(newEntry.getEntry().getLocation());
        entry.setTitle(newEntry.getEntry().getTitle());

        entry.setId(String.valueOf(selectedCustomer.getId()));
        entry.setUserObject(selectedCustomer);
        saveAppointment(entry.getStartAsZonedDateTime());
        products.addAppoitmEntry(entry, servicesa.get(selectedProduc - 1));
        newEntry.getEntry().removeFromCalendar();
    }

    @FXML
    public void textChanged() {
        personView.getSelectionModel().clearSelection();
    }

    private void saveAppointment(ZonedDateTime start) {
        Animal[] animals = selectedCustomer.getAnimals().toArray(new Animal[selectedCustomer.getAnimals().size()]);
        IProductDao productDao = new ProductDao();
        Product product = productDao.findByNameProduct(servicesa.get(selectedProduc - 1).getName());

        Appointment appointment = new Appointment(start, selectedCustomer.getName() + " " + product.getName());
        appointment.addCustomer(selectedCustomer);
        appointment.addAnimal(animals[petList.getSelectionModel().getSelectedIndex() - 1]);
        appointment.addProduct(product);

        IAppointmentDao appointmentDao = new AppointmentDao();
        appointmentDao.addAppointment(appointment);
    }
}
