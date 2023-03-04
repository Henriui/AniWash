package aniwash.view;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DateControl.EntryDetailsParameter;
import com.calendarfx.view.TimeField;

import aniwash.dao.AnimalDao;
import aniwash.dao.AppointmentDao;
import aniwash.dao.CustomerDao;
import aniwash.dao.IAnimalDao;
import aniwash.dao.IAppointmentDao;
import aniwash.dao.ICustomerDao;
import aniwash.dao.IProductDao;
import aniwash.dao.ProductDao;
import aniwash.entity.Animal;
import aniwash.entity.Appointment;
import aniwash.entity.Customer;
import aniwash.entity.Product;
import aniwash.resources.model.Calendars;
import aniwash.resources.model.CreatePopUp;
import aniwash.resources.model.CustomListViewCellCustomer;
import aniwash.resources.utilities.ControllerUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

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
    private EntryDetailsParameter newEntry;
    private ArrayList<Calendar> servicesa;
    private Customer selectedPerson;
    private ObservableList<Customer> allPeople;
    private IProductDao productDao = new ProductDao();

    public void initialize() {
        setArg();

        services.getItems().add("                                   Create new service  +");
        petList.getItems().add("                                   Create new pet  +");

        date.setValue(newEntry.getEntry().getStartDate());
        startTime.setValue(newEntry.getEntry().getStartTime());
        endTime.setValue(newEntry.getEntry().getEndTime());

        // Initialize the person table with the three columns.

        personList.setCellFactory(personList -> new CustomListViewCellCustomer());
        personList.setStyle("-fx-background-color: #f4f4f4; -fx-background: #f4f4f4;");

        // Set the placeholder text for the ListView

        Background background = new Background(
                new BackgroundFill(Color.web("#f4f4f4"), CornerRadii.EMPTY, Insets.EMPTY));
        personList.setPlaceholder(new Label("No items") {
            @Override
            protected void updateBounds() {
                super.updateBounds();
                setBackground(background);
            }
        });

        // Add data to the table

        servicesa = new ArrayList<>(products.getCalendarMap().values());

        servicesa.forEach(service -> {
            services.getItems().addAll(service.getName());
        });

        petList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains("Create new pet") && selectedPerson != null) {
                try {
                    // NEW ANIMAL POPUP
                    Stage stage = new Stage();
                    stage.setOnHidden(e -> {
                        update();
                        updatePets();
                        newEntry.getEntry().setLocation(newValue);
                    });
                    ControllerUtilities.newAnimal(selectedPerson, stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
        if (personList.getSelectionModel().getSelectedItem() == null || newEntry.getEntry().getLocation() == null
                || newEntry.getEntry().getTitle().contains("New Entry")
                || petList.getSelectionModel().getSelectedIndex() == -1) {
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

            personList.setItems(
                    allPeople.filtered(person -> person.getName().toLowerCase().contains(newValue.toLowerCase())));

            if (newValue.isEmpty()) {
                personList.setItems(null);
            }

        });

        // Set the selection model to allow only one row to be selected at a time.
        searchField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (searchField.getText().isEmpty() || personList.getItems().isEmpty()) {
                    personList.getSelectionModel().clearSelection();
                    try {
                        // CREATE NEW CUSTOMER
                        Stage stage = new Stage();
                        stage.setOnHidden(e -> {
                            update();
                            selectCustomer(personList.getItems().get(personList.getItems().size() - 1));
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
            personList.getSelectionModel().getSelectedItem();
            ObservableList<String> items = petList.getItems();
            items.removeAll(items.subList(1, items.size()));

            selectedPerson.getAnimals().forEach(animal -> {
                petList.getItems().addAll(animal.getName());
            });
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

    private void selectService(String newValue, int selectedIndex) {
        if (newValue.contains("Create new service")) {
            try {
                // NEW SERVICE POPUP
                Stage stage = new Stage();
                stage.setOnHidden(event -> {
                    List<Product> productList = productDao.findAllProduct();
                    ObservableList<String> nameList = FXCollections.observableList(
                            productList.stream().map(Product::getName).collect(Collectors.toList()));
                    for (int i = services.getItems().size() - 1; i > 0; i--) {
                        services.getItems().remove(i);
                    }
                    services.getItems().addAll(nameList);
                    services.getSelectionModel().selectLast();

                });
                ControllerUtilities.newProduct(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
        personList.setItems(tempCustomer);
        personList.getSelectionModel().select(0);

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

        updateAppointment(entry.getStartAsZonedDateTime(), entry.getEndAsZonedDateTime(), entry.getId(), selectedPerson,
                entry.getTitle(), entry.getLocation());
        products.addAppointmentEntry(entry, servicesa.get(services.getSelectionModel().getSelectedIndex() - 1));
        // servicesa.get(selectedProduc).removeEntry(newEntry.getEntry());
    }

    private void updateAppointment(ZonedDateTime start, ZonedDateTime end, String appointmentId, Customer customer,
            String productName, String animalName) {
        IAppointmentDao appointmentDao = new AppointmentDao();
        Appointment appointment = appointmentDao
                .findByIdAppointment(ControllerUtilities.longifyStringId(appointmentId));

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
    }

    private void updatePets() {
        personList.getSelectionModel().select(selectedPerson);
        personList.setItems(allPeople.filtered(customer -> customer.getName().contains(selectedPerson.getName())));
        ObservableList<String> items = petList.getItems();
        items.removeAll(items.subList(1, items.size()));

        selectedPerson.getAnimals().forEach(animal -> {
            petList.getItems().addAll(animal.getName());
        });
    }

    private ObservableList<Customer> getPeople() {
        ICustomerDao customerDao = new CustomerDao();
        ObservableList<Customer> customers = FXCollections.observableList(customerDao.findAllCustomer());
        return customers;
    }
}
