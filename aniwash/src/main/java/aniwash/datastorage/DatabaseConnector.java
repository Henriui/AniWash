package aniwash.datastorage;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConnector {

    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;

    public static EntityManager getInstance() {
        if (em == null) {
            if (emf == null) {
                Properties persistenceProperties = new Properties();
                try {
                    persistenceProperties.load(new FileInputStream("./aniwash/persistence.properties"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                emf = Persistence.createEntityManagerFactory("com.aniwash", persistenceProperties);
            }
            em = emf.createEntityManager();
        }
        return em;
    }
}
