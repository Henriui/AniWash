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

    public static long longifyStringId(String id) {
        StringBuilder sb = new StringBuilder(id);
        sb.deleteCharAt(0);
        sb.deleteCharAt(0);
        return Long.parseLong(sb.toString());
    }

}