package aniwash.view;

import aniwash.dao.*;
import aniwash.entity.Animal;
import aniwash.entity.Appointment;
import aniwash.entity.Customer;
import aniwash.entity.Product;
import aniwash.resources.model.Calendars;
import aniwash.resources.model.CreatePopUp;
import aniwash.resources.model.CustomListViewCellCustomer;
import aniwash.resources.utilities.ControllerUtilities;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DateControl.EntryDetailsParameter;
import com.calendarfx.view.TimeField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZonedDateTime;

public class NewAppoitmentController extends CreatePopUp {
    private Calendars products = new Calendars();
    @FXML
    private Button save;
    @FXML
    private ListView<String> services;
    @FXML
    private ListView<String> petList;
    @FXML
    private ListView<Customer> personList;
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
    private ObservableList<Calendar> calendarList;
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
        date.setValue(newEntry.getEntry().getStartDate());
        startTime.setValue(newEntry.getEntry().getStartTime());
        endTime.setValue(newEntry.getEntry().getEndTime());

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

        // Add data to the table

        calendarList = FXCollections.observableArrayList(products.getCalendarMap().values());

        // Add data to the service list
        calendarList.forEach(service -> {
            services.getItems().addAll(service.getName());
        });

        update();
        personList.setItems(null);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }

            personList.setItems(allPeople.filtered(person -> person.getName().toLowerCase().contains(newValue.toLowerCase())));

            if (newValue.isEmpty()) {
                personList.setItems(null);
            }
        });

        // Set the selection model to allow only one row to be selected at a time.
        searchField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (searchField.getText().isEmpty() || personList.getItems().isEmpty()) {
                    personList.getSelectionModel().clearSelection();
                    petList.getSelectionModel().clearSelection();
                    try {
                        // NEW CUSTOMER POPUP
                        Stage stage = new Stage();
                        stage.setOnHidden(e -> {
                            update();
                            ICustomerDao customerDao = new CustomerDao();
                            selectCustomer(customerDao.findNewestCustomer());
                            updatePets();
                        });
                        ControllerUtilities.newCustomer(stage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    personList.getSelectionModel().select(0);
                    updatePets();
                }
            }
        });

        personList.setOnMouseClicked(mouseEvent -> {
            selectCustomer(personList.getSelectionModel().getSelectedItem());
            updatePets();
        });

        // Listen for customer selection changes and show the person details when
        // changed.

        personList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
            if (newValue != null) {
                selectService(newValue);
                petList.setDisable(false);
                two.styleProperty().set("-fx-fill: #47c496");
                second.styleProperty().set("-fx-fill: #47c496");
            }
        });

        // Listen for pet selection changes and show the person details when changed.

        petList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectPet(newValue);
            }
        });

    }

    private void selectPet(String newValue) {
        if (newValue.contains("Create new pet") && selectedCustomer != null) {
            try {
                // NEW ANIMAL POPUP
                Stage stage = new Stage();
                stage.setOnHidden(e -> {
                    update();
                    updatePets();
                });
                ControllerUtilities.newAnimal(selectedCustomer, stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            newEntry.getEntry().setLocation(newValue);
            three.styleProperty().set("-fx-fill: #47c496");
        }

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
        personList.getSelectionModel().clearSelection();
    }

    // Set entrys "Title" which is used to store service name.

    private void selectCustomer(Customer customer) {
        selectedCustomer = customer;
    }

    private void selectService(String newValue) {
        if (newValue.contains("Create new service")) {
            try {
                // NEW SERVICE POPUP
                Stage stage = new Stage();

                stage.setOnHidden(event -> {
                    updateServices();
                });

                ControllerUtilities.newProduct(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Calendar service = calendarList.get(services.getSelectionModel().getSelectedIndex() - 1);
            newEntry.getEntry().setCalendar(service);
            newEntry.getEntry().setTitle(service.getName());
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
        entry.setCalendar(calendarList.get(services.getSelectionModel().getSelectedIndex() - 1));
        newEntry.getEntry().removeFromCalendar();
        products.addAppointmentEntry(entry, entry.getCalendar());
    }

    private String createAppointment(ZonedDateTime start, ZonedDateTime end) {
        Animal[] animals = selectedCustomer.getAnimals().toArray(new Animal[selectedCustomer.getAnimals().size()]);

        IProductDao productDao = new ProductDao();
        Product product = productDao.findByNameProduct(calendarList.get(services.getSelectionModel().getSelectedIndex() - 1).getName());

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
        personList.setItems(allPeople);
    }

    private void updatePets() {
        personList.getSelectionModel().select(selectedCustomer);
        personList.setItems(allPeople.filtered(customer -> customer.getName().contains(selectedCustomer.getName())));
        ObservableList<String> items = petList.getItems();
        items.removeAll(items.subList(1, items.size()));
        selectedCustomer.getAnimals().forEach(animal -> {
            petList.getItems().addAll(animal.getName());
        });
        IAnimalDao animalDao = new AnimalDao();
        Animal animal = animalDao.findNewestAnimal();
        petList.getSelectionModel().select(animal.getName());
        newEntry.getEntry().setLocation(animal.getName());
    }

    private void updateServices() {
        calendarList = FXCollections.observableArrayList(products.getCalendarMap().values());
        services.getItems().removeAll(services.getItems());
        calendarList.forEach(service -> services.getItems().addAll(service.getName()));
        IProductDao productDao = new ProductDao();
        Product product = productDao.findNewestProduct();
        services.getSelectionModel().select(product.getName());
        newEntry.getEntry().setCalendar(calendarList.get(services.getSelectionModel().getSelectedIndex() - 1));
        newEntry.getEntry().setTitle(product.getName());
    }

    private ObservableList<Customer> getPeople() {
        ICustomerDao customerDao = new CustomerDao();
        return FXCollections.observableList(customerDao.findAllCustomer());
    }

}
