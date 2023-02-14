package aniwash.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import aniwash.dao.EmployeeDao;
import aniwash.entity.Employee;

public class LoginController {
    @FXML
    private Button login;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;    
    EmployeeDao employeeDao = new EmployeeDao();

    public void initialize() {
        System.out.println("LoginController initialized");
    }

    public void login() {

    }

    @FXML
    public void loginButton(ActionEvent event) {
        System.out.println("Login button pressed");
        // Get username and password from text fields
        String username = this.username.getText();
        String password = this.password.getText();

        // Check if username and password are correct
        try {
            checkLogin(username, password);
        } catch (Exception e) {
            System.out.println("Error: ");
        }
    }

    private void checkLogin(String username, String password) {
        Employee e = employeeDao.findByUsernameEmployee(username);
        if (e != null) {
            if (e.getPassword().equals(password)) {
                System.out.println("Login successful");
            } else {
                System.out.println("Wrong password");
            }
        } else {
            System.out.println("Wrong username");
        }
    }

}
