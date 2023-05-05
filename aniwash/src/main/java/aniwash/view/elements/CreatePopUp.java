package aniwash.view.elements;

import aniwash.MainApp;
import aniwash.view.utilities.ControllerUtilities;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DateControl.EntryDetailsParameter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ResourceBundle;

public class CreatePopUp implements Callback<EntryDetailsParameter, Boolean> {

    private final ResourceBundle bundle = MainApp.getBundle();

    static EntryDetailsParameter savedArg0;
    // This method is called when the user double clicks on a calendar entry or an
    // empty space in calendar.

    @Override
    public Boolean call(EntryDetailsParameter arg0) {
        final FXMLLoader loader;
        final Scene scene;
        savedArg0 = arg0;
        if (arg0.getEntry().getTitle().equals("New Entry"))
            try {
                loader = ControllerUtilities.loadFXML("createAppoitment");
                scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle(bundle.getString("createAppointmentTittle"));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        else {
            try {
                loader = ControllerUtilities.loadFXML("editAppoitment");
                System.out.println("Edit Appointment " + loader + " " + loader.getLocation());
                scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle(bundle.getString("editAppointmentTittle"));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Entry<?> getArg() {
        return savedArg0.getEntry();
    }
}
