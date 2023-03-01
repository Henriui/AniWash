package aniwash.resources.utilies;

import java.io.IOException;

import aniwash.MainApp;
import aniwash.entity.Customer;
import aniwash.view.CreateNewAnimalController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControllerUtilies {

    public static void newCustomer() throws IOException {
        final FXMLLoader loader;
        final Scene scene;

        loader = MainApp.loadFXML("newCustomerView");
        scene = new Scene((javafx.scene.Parent) loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Create Customer");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        stage.setOnHidden(event -> {
            // TODO: Get customers from database so the listview reloads
            System.out.println("Hidden");

        });
    }
    public static void editCustomer() throws IOException {
        final FXMLLoader loader;
        final Scene scene;

        loader = MainApp.loadFXML("editCustomerView");
        scene = new Scene((javafx.scene.Parent) loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Create Customer");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        stage.setOnHidden(event -> {
            // TODO: Get customers from database so the listview reloads
            System.out.println("Hidden");

        });
    }

    public static void newAnimal(Customer selectedPerson) throws IOException {
        final FXMLLoader loader;
        final Scene scene;

        loader = MainApp.loadFXML("createNewAnimalView");
        scene = new Scene((javafx.scene.Parent) loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Create Animal");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        CreateNewAnimalController.setCustomer(selectedPerson);

        stage.setOnHidden(event -> {
            // TODO: Get customers from database so the listview reloads
            System.out.println("Hidden");
        });
    }

    public static void newProduct() throws IOException {
        final FXMLLoader loader;
        final Scene scene;

        loader = MainApp.loadFXML("newProductView");
        scene = new Scene((javafx.scene.Parent) loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Create Product");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        stage.setOnHidden(event -> {
            // TODO: Get customers from database so the listview reloads
            System.out.println("Hidden " + event.getSource().toString() + " eveny= " + event);

        });
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
