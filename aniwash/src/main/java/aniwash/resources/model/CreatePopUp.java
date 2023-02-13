package aniwash.resources.model;

import java.io.IOException;
import com.calendarfx.view.DateControl.EntryDetailsParameter;
import aniwash.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class CreatePopUp implements Callback<EntryDetailsParameter, Boolean> {

    static EntryDetailsParameter savedArg0;

    // This method is called when the user double clicks on a calendar entry or an empty space in calendar.

    @Override
    public Boolean call(EntryDetailsParameter arg0) {
        final FXMLLoader loader;
        final Scene scene;
        System.out.println("ARG = "+ arg0);
        savedArg0 = arg0;
        if (arg0.getEntry().getTitle().equals("New Entry"))
            try {
                loader = loadFXML("createAppoitment");
                scene = new Scene((Parent) loader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Create Appoitment");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        else {
            try {
                loader = loadFXML("editAppoitment");
                scene = new Scene((Parent) loader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Edit Appoitment");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
                System.out.println("Customers name is " + arg0.getEntry().getLocation());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public EntryDetailsParameter getArg() {
        return savedArg0;
    }
    
    public void saveEntry(EntryDetailsParameter arg0) {
        System.out.println("Tallennetaan " + arg0.getEntry().getTitle() + " " + arg0.getEntry().getLocation() + " " + arg0.getEntry().getStartTime() + " " + arg0.getEntry().getEndTime() + " " + arg0.getEntry().getId());
    }
    // This method is used to load the fxml file.

    private static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/" + fxml + ".fxml"));
        return fxmlLoader;
    }
}
