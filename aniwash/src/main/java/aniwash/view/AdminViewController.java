package aniwash.view;

import aniwash.MainApp;
import aniwash.dao.EmployeeDao;
import aniwash.datastorage.BiscuitExeption;
import aniwash.entity.Employee;
import aniwash.entity.UserType;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class AdminViewController {
    public EmployeeDao ed = new EmployeeDao();

    @FXML
    private RadioButton employee, employer;
    @FXML
    private TextField username, password, firstname, lastname, email, title;

    public void initialize() {
        // Set up the toggle group for the radio buttons.

        ToggleGroup grp = new ToggleGroup();
        employee.setToggleGroup(grp);
        employer.setToggleGroup(grp);

        // Set the default radio button to employee.

        employee.setSelected(true);
    }

    /**
     * Submits the form to create and employee.
     * 
     * @author henriui
     */
    public void submit() {
        // Check priviledges.

        try {
            if (MainApp.getBiscuit().getUser().getUserType() == UserType.EMPLOYEE)
                return;
        } catch (BiscuitExeption e) {
            System.out.println("User has no permission or is timed out.");
        }

        // Create the employee.

        if (createEmployee())
            System.out.println("Employee created");
        else
            System.out.println("Employee not created");

    }

    /**
     * Creates an employee when form is submitted.
     * 
     * @return true if employee is created, false otherwise.
     * @author henriui
     */
    private boolean createEmployee() {
        String username = this.username.getText();
        String password = this.password.getText();
        String name = this.firstname.getText();
        String lastname = this.lastname.getText();
        String email = this.email.getText();
        String title = this.title.getText();

        // Check if any of the fields are empty.

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || lastname.isEmpty() || email.isEmpty()
                || title.isEmpty())
            return false;

        // Check if the username is already taken.

        if (ed.findByUsernameEmployee(username) != null)
            return false;

        // Create the new employee

        Employee e = new Employee(username, password, name + " " + lastname, email, title,
                                  employee.isPressed() ? UserType.EMPLOYEE : UserType.EMPLOYER);

        // Add the employee to the database.
        try {
            ed.addEmployee(e);
        } catch (Exception e1) {
            e1.printStackTrace();
            return false;
        }
        // Clear the form after successful submission.
        this.username.clear();
        this.password.clear();
        this.firstname.clear();
        this.lastname.clear();
        this.email.clear();
        this.title.clear();

        return true;
    }
}
