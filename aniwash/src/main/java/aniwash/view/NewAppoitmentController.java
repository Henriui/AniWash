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
import java.util.Map;

public class NewAppoitmentController extends CreatePopUp {
    private Calendars modelViewController = new Calendars();
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
    private Entry<Appointment> newEntry;
    private ObservableList<Calendar> calendarObservableList;
    private ObservableList<Customer> customerObservableList;
    private Map<String, IDao> daoMap;

    public void initialize() {

        // Get the created entry from the calendar view.
        EntryDetailsParameter arg0 = getArg();
        newEntry = (Entry<Appointment>) arg0.getEntry();

        services.getItems().add("                                   Create new service  +");
        petList.getItems().add("                                   Create new pet  +");

        // Initialize datepicker with selected date
        date.setValue(newEntry.getStartDate());
        startTime.setValue(newEntry.getStartTime());
        endTime.setValue(newEntry.getEndTime().plusMinutes(30));

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

        daoMap = modelViewController.getDaoMap();

        calendarObservableList = FXCollections.observableArrayList(modelViewController.getCalendarMap().values());
        calendarObservableList.forEach(service -> services.getItems().addAll(service.getName()));

        customerObservableList = getPeople();
        personList.setItems(null);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;

            personList.setItems(customerObservableList.filtered(person -> person.getName().toLowerCase().contains(newValue.toLowerCase())));

            if (newValue.isEmpty()) personList.setItems(null);
        });

        searchField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (searchField.getText().isEmpty() || personList.getItems().isEmpty()) {
                    try {
                        // CREATE NEW CUSTOMER
                        Stage stage = new Stage();
                        stage.setOnHidden(e -> {
                            selectCustomer(newestCustomer());
                            updatePets();
                        });
                        ControllerUtilities.newCustomer(stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    personList.getSelectionModel().select(0);
                    selectCustomer(personList.getSelectionModel().getSelectedItem());
                    updatePets();
                    services.setDisable(false);
                }
            }
        });

        personList.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                try {
                    // NEW CUSTOMER POPUP
                    Stage stage = new Stage();
                    stage.setOnHidden(e -> {
                        selectCustomer(newestCustomer());
                        updatePets();
                    });
                    ControllerUtilities.newCustomer(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (personList.getSelectionModel().getSelectedItem() != null) {
                    selectCustomer(personList.getSelectionModel().getSelectedItem());
                    updatePets();
                    services.setDisable(false);
                }
            }
        });

        services.setOnMouseClicked(mouseEvent -> {
            if (services.getSelectionModel().getSelectedItem().contains("+")) {
                try {
                    // NEW SERVICE POPUP
                    Stage stage = new Stage();
                    stage.setOnHidden(e -> {
                        updateServices();
                    });
                    ControllerUtilities.newProduct(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (services.getSelectionModel().getSelectedItem() != null) {
                    selectService(services.getSelectionModel().getSelectedItem());
                    petList.setDisable(false);
                }
            }
        });

        petList.setOnMouseClicked(mouseEvent -> {
            if (petList.getSelectionModel().getSelectedItem().contains("+")) {
                try {
                    // NEW ANIMAL POPUP
                    Stage stage = new Stage();
                    stage.setOnHidden(event1 -> updatePets());
                    ControllerUtilities.newAnimal((Customer) newEntry.getProperties().get("customer"), stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (petList.getSelectionModel().getSelectedItem() != null) {
                    selectPet(petList.getSelectionModel().getSelectedItem());
                }
            }
        });
    }

    @FXML
    public void save() {
        if (personList.getSelectionModel().getSelectedItem() == null || newEntry.getLocation() == null || newEntry.getTitle().contains("New Entry") || petList.getSelectionModel().getSelectedIndex() == -1) {
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

    private void selectCustomer(Customer customer) {
        personList.getSelectionModel().select(customer);
        personList.scrollTo(customer);
        newEntry.getProperties().putIfAbsent("customer", customer);
        newEntry.getProperties().replace("customer", customer);
    }

    private void selectPet(String newValue) {
        newEntry.setLocation(newValue); // TODO: muuta käyttöä locationille
        petList.getSelectionModel().select(newValue);
        petList.scrollTo(newValue);
        Customer customer = (Customer) newEntry.getProperties().get("customer");
        int index = petList.getSelectionModel().getSelectedIndex() - 1;
        if (index >= 0) {
            newEntry.getProperties().putIfAbsent("animal", customer.findAllAnimals().get(index));
            newEntry.getProperties().replace("animal", customer.findAllAnimals().get(index));
        }
    }

    private void selectService(String newValue) {
        Calendar service = calendarObservableList.get(services.getSelectionModel().getSelectedIndex() - 1);
        newEntry.setCalendar(service);
        newEntry.setTitle(service.getName()); // TODO: muuta käyttöä titlelle
        services.scrollTo(newValue);
    }

    public void sendEntry() {
        newEntry.setInterval(date.getValue(), startTime.getValue(), date.getValue(), endTime.getValue());
        newEntry.setId(createAppointment(newEntry.getStartAsZonedDateTime(), newEntry.getEndAsZonedDateTime()));
    }

    private String createAppointment(ZonedDateTime zdtStart, ZonedDateTime zdtEnd) {
        IAppointmentDao appointmentDao = (AppointmentDao) daoMap.get("appointment");
        IProductDao productDao = (ProductDao) daoMap.get("product");

        Customer selectedCustomer = (Customer) newEntry.getProperties().get("customer");
        Animal animal = selectedCustomer.getAnimals().toArray(new Animal[selectedCustomer.getAnimals().size()])[petList.getSelectionModel().getSelectedIndex() - 1];
        Product product = productDao.findByNameProduct(calendarObservableList.get(services.getSelectionModel().getSelectedIndex() - 1).getName());
        Appointment appointment = new Appointment(zdtStart, zdtEnd, selectedCustomer.getName() + " " + product.getName());

        appointment.addCustomer(selectedCustomer);
        newEntry.getProperties().putIfAbsent("customer", selectedCustomer);
        appointment.addAnimal(animal);
        newEntry.getProperties().putIfAbsent("animal", animal);
        appointment.addProduct(product);
        newEntry.getProperties().putIfAbsent("product", product);
        appointmentDao.addAppointment(appointment);

        newEntry.setUserObject(appointment);
        return "id" + appointment.getId();
    }

    private Customer newestCustomer() {
        customerObservableList = getPeople();
        ICustomerDao customerDao = (CustomerDao) daoMap.get("customer");
        Customer c = customerDao.findNewestCustomer();
        personList.setItems(customerObservableList.filtered(customer -> customer.getName().contains(c.getName())));
        return c;
    }

    private void updatePets() {
        personList.setItems(customerObservableList.filtered(customer -> customer.getName().contains(((Customer) newEntry.getProperties().get("customer")).getName())));
        petList.getItems().removeAll(petList.getItems());
        petList.getItems().add("                                   Create new pet  +");
        ((Customer) newEntry.getProperties().get("customer")).getAnimals().forEach(animal -> petList.getItems().add(animal.getName()));

        IAnimalDao animalDao = (AnimalDao) daoMap.get("animal");
        Animal animal = animalDao.findNewestAnimal();
        petList.getSelectionModel().select(animal.getName());

        newEntry.setLocation(animal.getName()); // TODO: muuta käyttöä locationille
        newEntry.getProperties().replace("animal", animal);
    }

    private void updateServices() {
        calendarObservableList = FXCollections.observableArrayList(modelViewController.getCalendarMap().values());
        services.getItems().removeAll(services.getItems());
        services.getItems().add("                                   Create new service  +");
        calendarObservableList.forEach(service -> services.getItems().add(service.getName()));

        IProductDao productDao = (ProductDao) daoMap.get("product");
        Product product = productDao.findNewestProduct();
        services.getSelectionModel().select(product.getName());

        newEntry.setCalendar(calendarObservableList.get(services.getSelectionModel().getSelectedIndex() - 1));
        newEntry.setTitle(product.getName()); // TODO: muuta käyttöä titlelle
    }

    private ObservableList<Customer> getPeople() {
        ICustomerDao customerDao = (CustomerDao) daoMap.get("customer");
        return FXCollections.observableList(customerDao.findAllCustomer());
    }
}
