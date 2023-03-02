package aniwash.resources.utilities;

import aniwash.MainApp;
import aniwash.entity.Customer;
import aniwash.view.CreateNewAnimalController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerUtilities {

    public static FXMLLoader loadFXML(String fxml) throws IOException {
        return new FXMLLoader(MainApp.class.getResource("view/" + fxml + ".fxml"));
    }

    public static void newCustomer() throws IOException {
        final FXMLLoader loader;
        final Scene scene;

        loader = loadFXML("newCustomerView");
        scene = new Scene((javafx.scene.Parent) loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Create Customer");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        stage.setOnHidden(event -> {
            // TODO: Get customers from database so the listview reloads
            System.out.println("Closed " + event);
        });
    }


    public static void newAnimal(Customer selectedPerson) throws IOException {
        final FXMLLoader loader;
        final Scene scene;

        loader = loadFXML("createNewAnimalView");
        scene = new Scene((javafx.scene.Parent) loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Create Animal");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        CreateNewAnimalController.setCustomer(selectedPerson);

        stage.setOnHidden(event -> {
            // TODO: Get customers from database so the listview reloads
            System.out.println("Closed " + event);
        });
    }

    public static long longifyStringId(String id) {
        StringBuilder sb = new StringBuilder(id);
        sb.deleteCharAt(0);
        sb.deleteCharAt(0);
        return Long.parseLong(sb.toString());
    }

}
