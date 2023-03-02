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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Set;

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
    private DatePicker date = new DatePicker();
    @FXML
    private TimeField startTime = new TimeField();
    @FXML
    private TimeField endTime = new TimeField();
    @FXML
    private Rectangle third;
    private EntryDetailsParameter newEntry;
    private ArrayList<Calendar> servicesa;
    private Customer selectedPerson;
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

        servicesa = new ArrayList<>(products.getCalendarMap().values());

        servicesa.forEach(service -> {
            services.getItems().addAll(service.getName());
        });

        personView.getColumns().addAll(firstNameColumn, phoneColumn, emailColumn);

        petList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.contains("Create new pet")) {
                // CREATE NEW PET
            } else {
                newEntry.getEntry().setLocation(newValue);
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
        if (newEntry.getEntry().getLocation() == null || newEntry.getEntry().getTitle().contains("New Entry") || petList.getSelectionModel().getSelectedIndex() == -1) {
            System.out.println("Please select a service and a pet");
        } else {
            Stage stage = (Stage) save.getScene().getWindow();
            stage.close();
            sendEntry();
        }
    }

    @FXML
    public void modifyEntry() {
        allPeople = getPeople();

        FilteredList<Customer> filteredData = new FilteredList<>(allPeople, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            personView.getSelectionModel().clearSelection();
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
                        petList.getItems().addAll(animal.getName());
                    });
                }
            } else if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                // personView.getSelectionModel().clearSelection();
            }
        });

        personView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectCustomer(newValue);
                services.setDisable(false);
            }
        });
    }
    // Set entrys "Location" which is used to store customer name and pet.

    private void selectCustomer(Customer customer) {
        selectedPerson = customer;
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
        }
    }

    public void getInfo() {
        ObservableList<Customer> tempCustomer = FXCollections.observableArrayList();
        selectedPerson = (Customer) newEntry.getEntry().getUserObject();
        tempCustomer.add(0, selectedPerson);
        personView.setItems(tempCustomer);
        personView.getSelectionModel().select(0);

        int indexOfItemToSelect = services.getItems().indexOf(newEntry.getEntry().getCalendar().getName());

        services.getSelectionModel().select(indexOfItemToSelect);

        services.scrollTo(indexOfItemToSelect);

        selectedPerson.getAnimals().forEach(animal -> {
            petList.getItems().addAll(animal.getName());
        });

        String textToMatch = newEntry.getEntry().getLocation();
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
        Entry entry = newEntry.getEntry();
        entry.changeStartDate(newEntry.getEntry().getStartDate());
        entry.changeStartTime(newEntry.getEntry().getStartTime());
        entry.changeEndDate(newEntry.getEntry().getStartDate());
        entry.changeEndTime(newEntry.getEntry().getEndTime());
        entry.setLocation(newEntry.getEntry().getLocation());
        entry.setTitle(newEntry.getEntry().getTitle());
        entry.setId(newEntry.getEntry().getId());
        entry.setUserObject(selectedPerson);

        updateAppointment(entry.getStartAsZonedDateTime(), entry.getEndAsZonedDateTime(), entry.getId(), selectedPerson, entry.getTitle(), entry.getLocation());
        products.addAppointmentEntry(entry, servicesa.get(services.getSelectionModel().getSelectedIndex() - 1));
        // servicesa.get(selectedProduc).removeEntry(newEntry.getEntry());
    }


    private void updateAppointment(ZonedDateTime start, ZonedDateTime end, String appointmentId, Customer customer, String productName, String animalName) {
        IAppointmentDao appointmentDao = new AppointmentDao();
        Appointment appointment = appointmentDao.findByIdAppointment(ControllerUtilities.longifyStringId(appointmentId));

        Set<Product> products = appointment.getProducts();
        for (Product p : products) {
            if (!p.getName().equals(productName)) {
                appointment.removeProduct(p);
            }
        }

        if (products.isEmpty()) {
            IProductDao productDao = new ProductDao();
            Product product = productDao.findByNameProduct(productName);
            products.add(product);
        }

        Set<Customer> customers = appointment.getCustomers();
        for (Customer c : customers) {
            if (c.getId() != customer.getId()) {
                appointment.removeCustomer(c);
            }
        }

        if (customers.isEmpty()) {
            customers.add(customer);
        }

        Set<Animal> animals = appointment.getAnimals();
        for (Animal a : animals) {
            if (a.getId() != appointment.findAllAnimals().get(0).getId()) {
                appointment.removeAnimal(a);
            }
        }

        if (animals.isEmpty()) {
            IAnimalDao animalDao = new AnimalDao();
            Animal animal = animalDao.findByNameAnimal(animalName);
            animals.add(animal);
        }

        appointment.setStartDate(start);
        appointment.setEndDate(end);
        appointment.setCustomers(customers);
        appointment.setProducts(products);
        appointment.setAnimals(animals);

        appointmentDao.updateAppointment(appointment);
    }

    private ObservableList<Customer> getPeople() {
        ICustomerDao customerDao = new CustomerDao();
        ObservableList<Customer> customers = FXCollections.observableList(customerDao.findAllCustomer());
        return customers;
    }
}
