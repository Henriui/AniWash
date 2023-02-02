package aniwash;

import aniwash.dao.ICustomerDao;
import aniwash.entity.Customer;
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
/*
        ICustomerDao dao = new aniwash.dao.CustomerDao();
        Customer c = new Customer("Rasmus", 1, "123456", "ulina@ulinaaaa.fi", "12345678", "1234");
        System.out.println(dao.addCustomer(c));
        System.out.println(dao.findByIdCustomer(1).toString());
*/
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
