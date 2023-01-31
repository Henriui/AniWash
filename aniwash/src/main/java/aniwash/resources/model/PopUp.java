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
public class PopUp implements Callback<EntryDetailsParameter, Boolean> {

    private static FXMLLoader loadFXML(String fxml) throws IOException {
        // Finds fxml file from the resources folder.
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/" + fxml + ".fxml"));
        return fxmlLoader;
    }
    
    @Override
    public Boolean call(EntryDetailsParameter arg0){
        FXMLLoader loader;
        Scene scene;
        try {
            loader = loadFXML("asd");
            scene = new Scene((Parent) loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("New Calendar");
            //stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println(arg0.getEntry().getTitle());
            arg0.getEntry().setTitle("This is importans");
            stage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
  
        return true;
    }
    
}
