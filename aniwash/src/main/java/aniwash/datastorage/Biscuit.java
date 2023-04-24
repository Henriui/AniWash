package aniwash.datastorage;

import aniwash.entity.Employee;
import aniwash.enums.UserType;

// Biscuit class is like cookies in html. It stores the user's information
// so that it can be accessed from other classes.

/**
 * Biscuit class is like cookies in html. It stores the user's information
 * so that it can be accessed from other classes.
 *
 * @author henriui
 * @version 1.0
 * @since 2020-11-20
 */

public class Biscuit {

    // Employee object to store the logged in user.
    private Employee e;

    // Time of last activity.
    private long lastActivity;

    /**
     * Set the logged in user and update last activity time.
     *
     * @author henriui
     * @param employee Employee object to set as logged in user.
     */
    public void setBiscuit(Employee employee) {
        // Set employee as logged in user.
        this.e = employee;
        lastActivity = System.currentTimeMillis();
    }

    /**
     * Check if there is a logged in user.
     *
     * @return True if there is a logged in user, false otherwise.
     * @author henriui
     */
    public boolean isBiscuitSet() {
        // Check if there is a logged in user.
        return e != null;
    }

    /**
     * Update last activity time.
     *
     * @author henriui
     */
    public void updateLastActivity() {
        // Update last activity time.
        lastActivity = System.currentTimeMillis();
    }

    /**
     * Check if the user has been inactive for more than 30 minutes.
     *
     * @author henriui
     * @return True if the user has been inactive for more than 30 minutes, false
     *         otherwise.
     */
    public boolean isBiscuitExpired() {
        if (!isBiscuitSet()) {
            // Return true if there is no logged in user.
            return true;
        }
        // Check if the user has been inactive for more than 30 minutes.
        return System.currentTimeMillis() - lastActivity > 1800000;
    }

    /**
     * Get the logged in user.
     *
     * @return Logged in user object.
     * @throws BiscuitExeption If there is no logged in user.
     * @author henriui
     * @see BiscuitExeption
     */
    public Employee getUser() throws BiscuitExeption {
        if (!isBiscuitSet()) {
            // Throw exception if there is no logged in user.
            throw new BiscuitExeption("No user logged in.");
        }
        // Return object of logged in user.
        return e;
    }

    /**
     * Get the name of the logged in user.
     *
     * @return Name of the logged in user.
     * @throws BiscuitExeption If there is no logged in user.
     * @author henriui
     * @see BiscuitExeption
     */
    public UserType getUserType() throws BiscuitExeption {
        if (!isBiscuitSet()) {
            // Throw exception if there is no logged in user.
            throw new BiscuitExeption("No user logged in.");
        }
        // Return user type of logged in user.
        return e.getUserType();
    }

    /**
     * Logout the logged in user.
     * @author henriui
     */
    public void logout() {
        // Set logged in user to null.
        e = null;
    }
}
