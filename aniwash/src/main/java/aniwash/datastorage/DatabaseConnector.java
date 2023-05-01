package aniwash.datastorage;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseConnector {

    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;

    /**
     * Opens a connection to the database.
     * Used for using the default persistence unit.
     *
     * @return The EntityManager instance.
     * @author rasmushy
     * @see Persistence#createEntityManagerFactory(String)
     * @see EntityManagerFactory#createEntityManager()
     */
    public static EntityManager getInstance() {
        if (em == null) {
            if (emf == null) {
                emf = Persistence.createEntityManagerFactory("com.aniwash");
            }
            em = emf.createEntityManager();
        }
        return em;
    }

    /**
     * Opens a connection to the database.
     * Used for using a different persistence unit than the default (testing).
     *
     * @param persistenceUnitName The name of the persistence unit to use.
     * @author rasmushy
     * @see Persistence#createEntityManagerFactory(String)
     */
    public static void openDbConnection(String persistenceUnitName) {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        }
    }

    /**
     * Closes the connection to the database.
     *
     * @author rasmushy
     */
    public static void closeDbConnection() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }

}
