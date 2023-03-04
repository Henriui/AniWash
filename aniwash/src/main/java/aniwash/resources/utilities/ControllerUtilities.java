package aniwash.resources.utilities;

import aniwash.MainApp;
import aniwash.entity.Customer;
import aniwash.view.CreateNewAnimalController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerUtilities {

    public static FXMLLoader loadFXML(String fxml) throws IOException {
        return new FXMLLoader(MainApp.class.getResource("view/" + fxml + ".fxml"));
    }

    public static void newCustomer(Stage stage) throws IOException {
        final FXMLLoader loader;
        final Scene scene;

        loader = loadFXML("newCustomerView");
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Create Customer");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public static void editCustomer() throws IOException {
        final FXMLLoader loader;
        final Scene scene;

        loader = loadFXML("editCustomerView");
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


    public static void newAnimal(Customer selectedPerson, Stage stage) throws IOException {
        final FXMLLoader loader;
        final Scene scene;

        loader = loadFXML("createNewAnimalView");
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Create Animal");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        CreateNewAnimalController.setCustomer(selectedPerson);

/*      TODO: Implement this?
        stage.setOnHidden(event -> {
            listView.setItems(FXCollections.observableList(productDao.findAllProduct()));
        });
*/
    }

    public static void newProduct() throws IOException {
        final FXMLLoader loader;
        final Scene scene;

        loader = loadFXML("newProductView");
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

    public static long longifyStringId(String id) {
        StringBuilder sb = new StringBuilder(id);
        sb.deleteCharAt(0);
        sb.deleteCharAt(0);
        return Long.parseLong(sb.toString());
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
