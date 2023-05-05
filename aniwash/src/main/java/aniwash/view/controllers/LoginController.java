package aniwash.view.controllers;

import aniwash.MainApp;
import aniwash.dao.EmployeeDao;
import aniwash.entity.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * The LoginController class handles user login functionality in a Java application.
 */
public class LoginController {
    @FXML
    private Button login;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    private EmployeeDao employeeDao = new EmployeeDao();

    public void initialize() {
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

        Employee e = employeeDao.findByUsername(username);
        if (e != null) {
            if (e.getPassword().equals(password)) {
                System.out.println("Login successful");
                try {

                    //Check biscuit and set mainView scene.

                    MainApp.setBiscuit(e);
                    MainApp.setRoot("mainView");
                    return;
                } catch (Exception ee) {
                    System.out.println("Error loading mainView");
                }
            }
        }
        System.out.println("Wrong username or password");
    }

}
