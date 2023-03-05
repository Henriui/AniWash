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
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZonedDateTime;

public class EditAppoitmentController extends CreatePopUp {
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
    private DatePicker date = new DatePicker();
    @FXML
    private TimeField startTime = new TimeField();
    @FXML
    private TimeField endTime = new TimeField();
    private EntryDetailsParameter oldEntry;
    private ObservableList<Calendar> calendarList;
    private Customer selectedPerson;
    private ObservableList<Customer> allPeople;

    public void initialize() {
        setArg();

        services.getItems().add("                                   Create new service  +");
        petList.getItems().add("                                   Create new pet  +");

        date.setValue(oldEntry.getEntry().getStartDate());
        startTime.setValue(oldEntry.getEntry().getStartTime());
        endTime.setValue(oldEntry.getEntry().getEndTime());

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

        calendarList = FXCollections.observableArrayList(products.getCalendarMap().values());

        calendarList.forEach(service -> {
            services.getItems().addAll(service.getName());
        });

        petList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectPet(newValue);
            }
        });

        services.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectService(newValue);
                petList.setDisable(false);
            }
        });

        getInfo();
    }

    private void selectPet(String newValue) {
        if (newValue.contains("Create new pet") && selectedPerson != null) {
            try {
                // NEW ANIMAL POPUP
                Stage stage = new Stage();
                stage.setOnHidden(e -> {
                    update();
                    updatePets();
                });
                ControllerUtilities.newAnimal(selectedPerson, stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            oldEntry.getEntry().setLocation(newValue);
        }

    }

    // Save the selected person and send entry .

    @FXML
    public void save() {
        oldEntry.getEntry().setInterval(date.getValue(), startTime.getValue(), date.getValue(), endTime.getValue());
        if (personList.getSelectionModel().getSelectedItem() == null || oldEntry.getEntry().getLocation() == null || oldEntry.getEntry().getTitle().contains("New Entry") || petList.getSelectionModel().getSelectedIndex() == -1) {
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
        personList.setItems(allPeople);

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
                        // CREATE NEW CUSTOMER
                        Stage stage = new Stage();
                        stage.setOnHidden(e -> {
                            update();
                            ICustomerDao customerDao = new CustomerDao();
                            selectCustomer(customerDao.findNewestCustomer());
                            personList.scrollTo(selectedPerson);
                            updatePets();
                        });
                        ControllerUtilities.newCustomer(stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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

        personList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
            System.out.println("Selected service: " + service.getName());
            oldEntry.getEntry().setCalendar(service);
            oldEntry.getEntry().setTitle(service.getName());
        }
    }

    public void getInfo() {
        ObservableList<Customer> tempCustomer = FXCollections.observableArrayList();
        selectedPerson = (Customer) oldEntry.getEntry().getUserObject();
        tempCustomer.add(0, selectedPerson);
        personList.setItems(tempCustomer);
        personList.getSelectionModel().select(0);

        int indexOfItemToSelect = services.getItems().indexOf(oldEntry.getEntry().getCalendar().getName());

        services.getSelectionModel().select(indexOfItemToSelect);

        services.scrollTo(indexOfItemToSelect);

        selectedPerson.getAnimals().forEach(animal -> {
            petList.getItems().addAll(animal.getName());
        });

        String textToMatch = oldEntry.getEntry().getLocation();
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
        oldEntry = arg0;
    }

    public void sendEntry() {
/*
        Entry<Object> updatedEntry = new Entry<>();
        updatedEntry.changeStartDate(oldEntry.getEntry().getStartDate());
        updatedEntry.changeStartTime(oldEntry.getEntry().getStartTime());
        updatedEntry.changeEndDate(oldEntry.getEntry().getStartDate());
        updatedEntry.changeEndTime(oldEntry.getEntry().getEndTime());
        updatedEntry.setLocation(oldEntry.getEntry().getLocation());
        updatedEntry.setTitle(oldEntry.getEntry().getTitle());
        updatedEntry.setId(oldEntry.getEntry().getId());
        updatedEntry.setUserObject(selectedPerson);
        updatedEntry.setCalendar(oldEntry.getEntry().getCalendar());
*/
        updateAppointment(oldEntry.getEntry().getStartAsZonedDateTime(), oldEntry.getEntry().getEndAsZonedDateTime(), oldEntry.getEntry().getId(), (Customer) oldEntry.getEntry().getUserObject(), oldEntry.getEntry().getTitle(), oldEntry.getEntry().getLocation());
        //oldEntry.getEntry().removeFromCalendar();
    }

    private void updateAppointment(ZonedDateTime start, ZonedDateTime end, String appointmentId, Customer customer, String productName, String animalName) {
        IAppointmentDao appointmentDao = new AppointmentDao();
        Appointment appointment = appointmentDao.findByIdAppointment(ControllerUtilities.longifyStringId(appointmentId));

        if (!appointment.findAllProducts().get(0).getName().equals(productName)) {
            appointment.removeProduct(appointment.findAllProducts().get(0));
            IProductDao productDao = new ProductDao();
            Product product = productDao.findByNameProduct(productName);
            appointment.addProduct(product);
        }

        if (!appointment.findAllCustomers().get(0).getName().equals(customer.getName())) {
            appointment.removeCustomer(appointment.findAllCustomers().get(0));
            appointment.addCustomer(customer);
        }

        if (!appointment.findAllAnimals().get(0).getName().equals(animalName)) {
            appointment.removeAnimal(appointment.findAllAnimals().get(0));
            IAnimalDao animalDao = new AnimalDao();
            Animal animal = animalDao.findByNameAnimal(animalName);
            appointment.addAnimal(animal);
        }

        appointment.setStartDate(start);
        appointment.setEndDate(end);

        appointmentDao.updateAppointment(appointment);
    }

    private void update() {
        allPeople = getPeople();
        personList.setItems(allPeople);
        personList.getSelectionModel().select(selectedPerson);
    }

    private void updatePets() {
        personList.getSelectionModel().select(selectedPerson);
        personList.setItems(allPeople.filtered(customer -> customer.getName().contains(selectedPerson.getName())));

        ObservableList<String> items = petList.getItems();
        items.removeAll(items.subList(1, items.size()));

        selectedPerson.getAnimals().forEach(animal -> {
            petList.getItems().addAll(animal.getName());
        });

        IAnimalDao animalDao = new AnimalDao();
        Animal animal = animalDao.findNewestAnimal();
        petList.getSelectionModel().select(animal.getName());
        oldEntry.getEntry().setLocation(animal.getName());
    }

    private void updateServices() {
        calendarList = FXCollections.observableArrayList(products.getCalendarMap().values());
        services.getItems().removeAll(services.getItems());
        calendarList.forEach(service -> services.getItems().addAll(service.getName()));
        IProductDao productDao = new ProductDao();
        Product product = productDao.findNewestProduct();
        services.getSelectionModel().select(product.getName());
        oldEntry.getEntry().setCalendar(calendarList.get(services.getSelectionModel().getSelectedIndex() - 1));
        oldEntry.getEntry().setTitle(product.getName());
    }

    private ObservableList<Customer> getPeople() {
        ICustomerDao customerDao = new CustomerDao();
        return FXCollections.observableList(customerDao.findAllCustomer());
    }
}
