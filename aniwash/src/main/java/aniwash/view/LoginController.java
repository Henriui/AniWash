package aniwash.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import aniwash.MainApp;
import aniwash.dao.EmployeeDao;
import aniwash.entity.Employee;

public class LoginController {
    @FXML
    private Button login;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    private EmployeeDao employeeDao = new EmployeeDao();

    public void initialize() {
        MainApp.changeStageSize(555, 555);
    }

    @FXML
    public void loginButton(ActionEvent event) {

        // Get username and password from text fields

        String username = this.username.getText();
        String password = this.password.getText();

        // Check if username and password are correct
        try {
            checkLogin(username, password);
        } catch (Exception e) {
            System.out.println("Login error.");
        }
    }

    private void checkLogin(String username, String password) {

        Employee e = employeeDao.findByUsernameEmployee(username);
        if (e != null) {
            if (e.getPassword().equals(password)) {
                System.out.println("Login successful");
                try {
                    MainApp.setRoot("mainView");
                    MainApp.changeStageSize(1300, 800);
                } catch (Exception ee) {
                    System.out.println("Error loading mainView");
                }
            }
        }
        // Set mainView scene.
        System.out.println("Wrong username or password");
    }

}
