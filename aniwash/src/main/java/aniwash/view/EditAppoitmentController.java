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
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Map;

public class EditAppoitmentController extends CreatePopUp {
    private final Calendars modelViewController = new Calendars();
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
    private DatePicker date = new DatePicker();
    @FXML
    private TimeField startTime = new TimeField();
    @FXML
    private TimeField endTime = new TimeField();
    private Entry<Appointment> newEntry;
    private ObservableList<Calendar<Product>> calendarObservableList;
    private ObservableList<Customer> customerObservableList;
    private final Map<String, IDao> daoMap = modelViewController.getDaoMap();

    public void initialize() {
        newEntry = (Entry<Appointment>) getArg();
        services.getItems().add("                                   Create new service  +");
        petList.getItems().add("                                   Create new pet  +");
        date.setValue(newEntry.getStartDate());
        startTime.setValue(newEntry.getStartTime());
        endTime.setValue(newEntry.getEndTime());
        // Initialize the person table with the three columns.
        personList.setCellFactory(personList -> new CustomListViewCellCustomer());
        personList.setStyle("-fx-background-color: #f4f4f4; -fx-background: #f4f4f4;");
        // Set the placeholder text for the ListView
        Background background = new Background(new BackgroundFill(Color.web("#f4f4f4"), CornerRadii.EMPTY, Insets.EMPTY));
        personList.setPlaceholder(new Label("No items") {
            @Override
            protected void updateBounds() {
                super.updateBounds();
                setBackground(background);
            }
        });
        // Add data to the table
        calendarObservableList = FXCollections.observableArrayList(modelViewController.getCalendarMap().values());
        calendarObservableList.forEach(service -> services.getItems().addAll(service.getName()));
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
                    stage.setOnHidden(e -> updateServices());
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
                    ControllerUtilities.newAnimal(personList.getSelectionModel().getSelectedItem(), stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (petList.getSelectionModel().getSelectedItem() != null) {
                    selectPet(petList.getSelectionModel().getSelectedItem());
                }
            }
        });
        getCurrentAppointment();
    }
    // Save the selected person and send entry .

    @FXML
    public void save() {
        if (personList.getSelectionModel().getSelectedItem() == null || newEntry.getLocation() == null || newEntry.getTitle().contains("New Entry") || petList.getSelectionModel().getSelectedIndex() == -1) {
            System.out.println("Please select a service and a pet");
            // TODO: Alert popup for missing fields ;)
        } else {
            Stage stage = (Stage) save.getScene().getWindow();
            stage.close();
            sendEntry();
        }
    }

    @FXML
    public void modifyEntry() {
        customerObservableList = getPeople();
        personList.setItems(customerObservableList);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null)
                return;
            personList.setItems(customerObservableList.filtered(person -> person.getName().toLowerCase().contains(newValue.toLowerCase())));
            if (newValue.isEmpty())
                personList.setItems(null);
        });
        // Set the selection model to allow only one row to be selected at a time.
        searchField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (searchField.getText().isEmpty() || personList.getItems().isEmpty()) {
                    personList.getSelectionModel().clearSelection();
                    petList.getSelectionModel().clearSelection();
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
                }
            }
        });

    }
    // Set entrys "Location" which is used to store customer name and pet.

    private void selectCustomer(Customer customer) {
        personList.getSelectionModel().select(customer);
        personList.scrollTo(customer);
        //newEntry.getProperties().replace("customer", customer);
        //newEntry.getProperties().putIfAbsent("customer", customer);
    }

    private void selectPet(String newValue) {
        newEntry.setLocation(newValue); // TODO: muuta käyttöä locationille
        petList.getSelectionModel().select(newValue);
        petList.scrollTo(newValue);
        //Customer customer = (Customer) newEntry.getProperties().get("customer");
        //int index = petList.getSelectionModel().getSelectedIndex() - 1;
        //if (index >= 0) {
        //    newEntry.getProperties().putIfAbsent("animal", customer.getAnimalList().get(index));
        //    newEntry.getProperties().replace("animal", customer.getAnimalList().get(index));
        //}
    }

    private void selectService(String newValue) {
        Calendar<Product> service = calendarObservableList.get(services.getSelectionModel().getSelectedIndex() - 1);
        newEntry.setCalendar(service);
        newEntry.setTitle(service.getName()); // TODO: muuta käyttöä titlelle
        services.scrollTo(newValue);
    }

    public void getCurrentAppointment() {
        customerObservableList = getPeople();
        Appointment appointment = newEntry.getUserObject();
        Customer customer = appointment.getCustomerList().get(0);
        Animal a = appointment.getAnimalList().get(0);
        //Customer customer = (Customer) newEntry.getProperties().get("customer");
        personList.setItems(customerObservableList.filtered(person -> person.getName().equals(customer.getName())));
        personList.getSelectionModel().select(customer);
        services.getSelectionModel().select(newEntry.getCalendar().getName());
        customer.getAnimals().forEach(animal -> petList.getItems().add(animal.getName()));
        petList.getSelectionModel().select(a.getName());
    }

    public void sendEntry() {
        newEntry.setInterval(date.getValue(), startTime.getValue(), date.getValue(), endTime.getValue());
        updateAppointment(newEntry.getStartAsZonedDateTime(), newEntry.getEndAsZonedDateTime(), newEntry.getUserObject());
    }

    private void updateAppointment(ZonedDateTime zdtStart, ZonedDateTime zdtEnd, Appointment appointment) {
        IAppointmentDao appointmentDao = (AppointmentDao) daoMap.get("appointment");
        appointment.setStartDate(zdtStart);
        appointment.setEndDate(zdtEnd);
        Product product = (Product) newEntry.getCalendar().getUserObject();
        if (!(appointment.getProducts().contains(product))) {
            appointment.removeProduct(appointment.getProductList().get(0));
            //Product product = (Product) newEntry.getCalendar().getUserObject();
            appointment.addProduct(product);
            //newEntry.getProperties().replace("product", product);
        }
        //Customer c = ((Customer) newEntry.getProperties().get("customer"));
        Customer c = personList.getSelectionModel().getSelectedItem();
        if (!(appointment.getCustomers().contains(c))) {
            appointment.removeCustomer(appointment.getCustomerList().get(0));
            // ICustomerDao customerDao = (CustomerDao) daoMap.get("customer");
            //Customer customer = customerDao.findByIdCustomer(c.getId());
            appointment.addCustomer(c);
        }
        //Animal a = ((Animal) newEntry.getProperties().get("animal"));
        Animal a = c.getAnimalList().get(petList.getSelectionModel().getSelectedIndex() - 1);
        if (!(appointment.getAnimals().contains(a))) {
            appointment.removeAnimal(appointment.getAnimalList().get(0));
            // IAnimalDao animalDao = (AnimalDao) daoMap.get("animal");
            //Animal animal = animalDao.findByIdAnimal(a.getId());
            appointment.addAnimal(a);
        }
        // Database update for appointment
        appointmentDao.updateAppointment(appointment);
    }

    private Customer newestCustomer() {
        ICustomerDao customerDao = (CustomerDao) daoMap.get("customer");
        Customer c = customerDao.findNewestCustomer();
        customerObservableList.add(c);
        personList.setItems(customerObservableList.filtered(customer -> customer.getName().contains(c.getName())));
        return c;
    }

    private void updatePets() {
        Customer c = personList.getSelectionModel().getSelectedItem();
        //Customer c = (Customer) newEntry.getProperties().get("customer");
        personList.setItems(customerObservableList.filtered(customer -> customer.getName().contains(c.getName())));
        petList.getItems().removeAll(petList.getItems());
        petList.getItems().add("                                   Create new pet  +");
        c.getAnimals().forEach(animal -> petList.getItems().add(animal.getName()));
        IAnimalDao animalDao = (AnimalDao) daoMap.get("animal");
        Animal animal = animalDao.findNewestAnimal();
        petList.getSelectionModel().select(animal.getName());
        newEntry.setLocation(animal.getName()); // TODO: muuta käyttöä locationille
        //newEntry.getProperties().replace("animal", animal);
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
