package aniwash;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

import aniwash.datastorage.Biscuit;
import aniwash.entity.Employee;
import aniwash.resources.model.Calendars;

public class MainApp extends Application {

    @FXML
    private AnchorPane a;
    private static Biscuit cookie;
    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        // FIXME: Delete these two lines after database is implemented
        Calendars calendar = new Calendars();
        calendar.initCalendar();

        // Set stage static so it can be accessed from other classes
        MainApp.stage = stage;

        // TODO: Change this to the login view when project done.
        Parent root = FXMLLoader.load(getClass().getResource("view/mainView.fxml"));
        scene = new Scene(root);
        cookie = new Biscuit();
        stage.setScene(scene);
        stage.show();
    }

    private static void setBiscuit(Employee e) {
        cookie.setBiscuit(e);
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

    // Method to change stage size.
    public static void changeStageSize(int width, int height) {
        stage.setWidth(width);
        stage.setHeight(height);
    }
}
