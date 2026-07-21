package database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Classe di utilità che gestisce l'inizializzazione e l'accesso al framework di persistenza JPA/Hibernate.
 * È un singleton per garantire che venga creata un'unica istanza globale di EntityManagerFactory durante l'intero ciclo di vita del sistema.
 */
public class JpaUtil {

    private static JpaUtil instance;
    private final EntityManagerFactory emf;

    /**
     * Costruttore privato per evitare l'istanza di più oggetti JpaUtil
     */
    private JpaUtil() {
        emf = Persistence.createEntityManagerFactory("biblioteca-universitaria");
    }

    /**
     * Restituisce l'unica istanza della classe JpaUtil o, se non esiste, provvede a crearla.
     * @return L'istanza singleton di JpaUtil.
     */
    public static JpaUtil getInstance() {
        if (instance == null) {
            instance = new JpaUtil();
        }

        return instance;
    }

    /**
     * Genera e restituisce un nuovo EntityManager, una singola sessione di lavoro con il database.
     * @return istanza di EntityManager.
     */
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Chiude definitivamente la EntityManagerFactory e rilascia le risorse allocate al termine dell'esecuzione del sistema per garantire una corretta disconnessione dal database.
     */
    public void chiudi() {
        emf.close();
    }
}
