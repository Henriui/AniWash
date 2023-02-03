package aniwash;

import aniwash.dao.IAnimalDao;
import aniwash.dao.ICustomerDao;
import aniwash.dao.IEmployeeDao;
import aniwash.entity.Animal;
import aniwash.entity.Customer;
import aniwash.entity.Employee;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainApp extends Application {

    @FXML
    private AnchorPane a;

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("view/mainView.fxml"));

        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        ICustomerDao customerDao = new aniwash.dao.CustomerDao();
        IAnimalDao animalDao = new aniwash.dao.AnimalDao();
        IEmployeeDao employeeDao = new aniwash.dao.EmployeeDao();
        Customer c = new Customer("Rasmus", 1, "123456", "ulina@ulinaaaa.fi");
        Animal a = new Animal("Koiruli", "Dog", "Husky", 2, "Very good dog");
        Employee e = new Employee("Rasmus", 1, "Boss", "123456", "tester@test.fi");

        System.out.println(customerDao.addCustomer(c));
        System.out.println(animalDao.addAnimal(a));
        System.out.println(customerDao.findByIdCustomer(c).toString());
        System.out.println(animalDao.findByIdAnimal(a.getId()).toString());
        c.addAnimal(a);
        customerDao.updateCustomer(c);
        c.getAnimals().forEach(animal -> System.out.println(animal.toString()));
        System.out.println();
        System.out.println(employeeDao.addEmployee(e));
        System.out.println(employeeDao.findByIdEmployee(e.getId()).toString());
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void startSimulation(String[] args) {
        launch();
    }

}
