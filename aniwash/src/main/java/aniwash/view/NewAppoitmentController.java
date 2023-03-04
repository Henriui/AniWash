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
    // Save the selected person and send entry .
    private ObservableList<Customer> allPeople;

    public void initialize() {

        // Get the created entry from the calendar view.

        EntryDetailsParameter arg0 = getArg();
        newEntry = arg0;

        services.getItems().add("                                   Create new service  +");
        petList.getItems().add("                                   Create new pet  +");

        // Initialize datepicker with selected date
        System.out.println("NewAppointmentController newEntry: " + newEntry.getEntry());

        date.setValue(newEntry.getEntry().getStartDate());
        startTime.setValue(newEntry.getEntry().getStartTime());
        endTime.setValue(newEntry.getEntry().getEndTime());

        // Initialize the person table with the three columns.

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        personView.getColumns().addAll(firstNameColumn, phoneColumn, emailColumn);

        // Add data to the table

        servicesa = new ArrayList<>(products.getCalendarMap().values());

        // Add data to the service list
        servicesa.forEach(service -> {
            services.getItems().addAll(service.getName());
        });

        update();
        personView.setItems(null);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }

            personView.setItems(allPeople.filtered(person -> person.getName().toLowerCase().contains(newValue.toLowerCase())));

            if (newValue.isEmpty()) {
                personView.setItems(null);
            }
        });

        // Set the selection model to allow only one row to be selected at a time.
        searchField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (searchField.getText().isEmpty() || personView.getItems().isEmpty()) {
                    personView.getSelectionModel().clearSelection();
                    try {
                        // NEW CUSTOMER POPUP
                        Stage stage = new Stage();
                        stage.setOnHidden(e -> {
                            update();
                            selectCustomer(personView.getItems().get(personView.getItems().size() - 1));
                            updatePets();
                        });
                        ControllerUtilities.newCustomer(stage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    personView.getSelectionModel().select(0);
                    updatePets();
                }
            }
        });


        personView.setOnMouseClicked(mouseEvent -> {
            selectCustomer(personView.getSelectionModel().getSelectedItem());
            updatePets();
        });

        // Listen for customer selection changes and show the person details when
        // changed.

        personView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectCustomer(newValue);
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
            if (newValue.contains("Create new pet") && selectedCustomer != null) {
                try {
                    // NEW ANIMAL POPUP
                    Stage stage = new Stage();
                    stage.setOnHidden(e -> {
                        update();
                        updatePets();
                        newEntry.getEntry().setLocation(newValue);
                    });
                    ControllerUtilities.newAnimal(selectedCustomer, stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
    }

    private void selectService(String newValue, int selectedIndex) {
        if (newValue.contains("Create new service")) {
            System.out.println("Create new service");
            try {
                // NEW SERVICE POPUP
                Stage stage = new Stage();
                /* stage.setOnHidden(e -> {
                    update();
                    selectService(services.getItems().get(services.getItems().size() - 1), services.getItems().size() - 1); */
              /*   }); */
                ControllerUtilities.newProduct(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
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
        entry.setId(createAppointment(newEntry.getEntry().getStartAsZonedDateTime(), newEntry.getEntry().getEndAsZonedDateTime()));
        entry.setLocation(newEntry.getEntry().getLocation());
        entry.setTitle(newEntry.getEntry().getTitle());
        entry.setUserObject(selectedCustomer);
        entry.setCalendar(servicesa.get(selectedProduc - 1));
        newEntry.getEntry().removeFromCalendar();
        products.addAppointmentEntry(entry, servicesa.get(selectedProduc - 1));
    }

    private String createAppointment(ZonedDateTime start, ZonedDateTime end) {
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
        allPeople = getPeople();
        personView.setItems(allPeople);
    }

    private void updatePets() {
        personView.getSelectionModel().select(selectedCustomer);
        personView.setItems(allPeople.filtered(customer -> customer.getName().contains(selectedCustomer.getName())));
        ObservableList<String> items = petList.getItems();
        items.removeAll(items.subList(1, items.size()));

        selectedCustomer.getAnimals().forEach(animal -> {
            petList.getItems().addAll(animal.getName());
        });
    }

    private ObservableList<Customer> getPeople() {
        ICustomerDao customerDao = new CustomerDao();
        return FXCollections.observableList(customerDao.findAllCustomer());
    }


}
