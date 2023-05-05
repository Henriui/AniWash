package aniwash;

import aniwash.dao.EmployeeDao;
import aniwash.dao.IEmployeeDao;
import aniwash.datastorage.Biscuit;
import aniwash.entity.Employee;
import aniwash.enums.UserType;
import aniwash.view.utilities.LanguageSave;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The MainApp class is responsible for initializing the application, setting the scene and stage,
 * loading FXML files, and managing the user's login status and locale.
 */
public class MainApp extends Application {

    @FXML
    private AnchorPane a;
    private static Biscuit cookie;
    private static Scene scene;
    private static Stage stage;
    private static Locale locale;
    private static ResourceBundle bundle;

    @Override
    public void start(Stage stage) throws IOException {
        // Add debug employee to database
        // TODO: Delete this before release.


        Employee empl = new Employee("admin", "admin", "Tim Cook", "tim.cook@lethimcook.com", "CEO entrepeneur", UserType.EMPLOYER);
        IEmployeeDao ed = new EmployeeDao();
        try {
            ed.add(empl);
        } catch (Exception ex) {
            System.out.println("Error deleting all employees");
        }

        // Set stage static so it can be accessed from other classes
        MainApp.stage = stage;

        // Initialise biscuit
        cookie = new Biscuit();

        try {
            locale = LanguageSave.readLanguageFromFile();
            if (locale == null) {
                locale = new Locale.Builder().setLanguage("en").setRegion("US").build();
            }
        } catch (Exception e) {
            locale = new Locale.Builder().setLanguage("en").setRegion("US").build();
        }
        setLocale(locale);
        cookie.setBiscuit(empl);
        Parent root = loadParent("login");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void setBiscuit(Employee e) {
        cookie.setBiscuit(e);
    }

    public static Biscuit getBiscuit() {
        return cookie;
    }

    public static void setRoot(String fxml) throws IOException {
        // Check if user is logged in. Otherwise, redirect to login view.

        if (!cookie.isBiscuitSet() || cookie.isBiscuitExpired()) {
            scene.setRoot(loadParent("login"));
            return;
        }
        cookie.updateLastActivity();
        scene.setRoot(loadParent(fxml));
    }

    public static void setLocale(Locale locale) {
        LanguageSave.writeLanguageToFile(locale);
        MainApp.locale = locale;
        Locale.setDefault(locale);
        bundle = ResourceBundle.getBundle("aniwash.languages.Resources", locale);
    }

    public static Locale getLocale() {
        return locale;
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }


    private static Parent loadParent(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/" + fxml + ".fxml"));
        fxmlLoader.setResources(bundle);
        return fxmlLoader.load();
    }

    public static void startSimulation(String[] args) {
        launch(args);
    }

    // Method to change stage size.
    public static void changeStageSize(int width, int height) {
        stage.setWidth(width);
        stage.setHeight(height);
    }

}
