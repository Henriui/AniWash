package aniwash.datastorage;

import jakarta.persistence.*;

public class DatabaseConnector {

    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;

    public static EntityManager getInstance() {
        if (em == null) {
            if (emf == null) {
                emf = Persistence.createEntityManagerFactory("com.aniwash");
            }
            em = emf.createEntityManager();
        }
        return em;
    }
}
