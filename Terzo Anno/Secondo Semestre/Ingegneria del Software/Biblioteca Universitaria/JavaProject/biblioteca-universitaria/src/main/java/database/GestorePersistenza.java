package database;

import exception.DatabaseException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Modulo di interazione del sistema con il framework di persistenza, JPA/Hibernate.
 */
public class GestorePersistenza {

    /**
     * Logger utilizzato per raccogliere informazioni dai fault critici.
     */
    private static final Logger LOGGER = Logger.getLogger(GestorePersistenza.class.getName());

    /**
     * Salva l'oggetto specificato nel database.
     * @param oggetto oggetto da salvare nel database.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    public void salva(Object oggetto) throws DatabaseException {
        EntityManager em = JpaUtil.getInstance().getEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(oggetto);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOGGER.log(Level.SEVERE, "Errore durante il salvataggio dell'entità nel database", e);
            throw new DatabaseException("Impossibile salvare i dati: errore di comunicazione col database.", e);
        } finally {
            em.close();
        }
    }

    /**
     * Salva tutti gli oggetti specificati nel database.
     * @param oggetti oggetti da salvare nel database.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    public void salvaTutti(Object... oggetti) throws DatabaseException{
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            for (Object oggetto : oggetti) {
                em.persist(oggetto);
            }
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOGGER.log(Level.SEVERE, "Errore durante il salvataggio delle entità nel database", e);
            throw new DatabaseException("Impossibile salvare la lista dei dati nel database.", e);
        } finally {
            em.close();
        }
    }

    /**
     * Preleva dal database un oggetto di una classe tramite il suo id.
     * @param classe classe dell'oggetto da prelevare.
     * @param id id dell'oggetto nel database da prelevare.
     * @param <T> tipo parametrico dell'oggetto.
     * @return l'oggetto corrispondente alla relativa entry del database tramite id.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    public <T> T trovaPerId(Class<T> classe, Long id) throws DatabaseException {
        try (EntityManager em = JpaUtil.getInstance().getEntityManager()) {
            return em.find(classe, id);
        }  catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "Errore durante la ricerca per ID", e);
            throw new DatabaseException("Errore di rete durante il recupero del record.", e);
        }
    }

    /**
     * Preleva dal database un oggetto di una classe tramite l'accoppiamento campo-valore.
     * @param classe classe dell'oggetto da prelevare.
     * @param nomeCampo nome del campo tramite cui fare l'accoppiamento con il relativo valore.
     * @param valore valore del campo dell'oggetto da prelevare.
     * @param <T> tipo parametrico dell'oggetto.
     * @return lista di oggetti corrispondenti alle relative entry del database tramite accoppiamento campo-valore.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    public <T> List<T> cercaPerCampo(Class<T> classe, String nomeCampo, Object valore) throws DatabaseException {
        return cercaPerCampi(classe, Map.of(nomeCampo, valore));
    }

    /**
     * Metodo helper che costruisce una query JPQL (Java Persistence Query Language) sulla base della classe e dei campi di ricerca
     * @param em EntityManager con il quale effettuare
     * @param classe classe sulla quale costruire la query.
     * @param campi campi di ricerca per la proiezione.
     * @param <T> tipo parametrico dei risultati della query.
     * @return la query per la ricerca di dati nel database.
     */
    private <T> TypedQuery<T> costruisciQueryPerCampi(EntityManager em, Class<T> classe, Map<String, Object> campi) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e FROM ").append(classe.getSimpleName()).append(" e");
        if (!campi.isEmpty()) {
            jpql.append(" WHERE ");
            int contatore = 0;
            for (String nomeCampo : campi.keySet()) {
                if (contatore > 0) {
                    jpql.append(" AND ");
                }
                String nomeParametro = nomeCampo.replace(".", "_");
                jpql.append("e.").append(nomeCampo).append(" = :").append(nomeParametro);
                contatore++;
            }
        }
        TypedQuery<T> query = em.createQuery(jpql.toString(), classe);
        for (Map.Entry<String, Object> entry : campi.entrySet()) {
            String nomeParametro = entry.getKey().replace(".", "_");
            query.setParameter(nomeParametro, entry.getValue());
        }
        return query;
    }

    /**
     * Esegue una query JPQL per la ricerca di oggetti sulla base dei campi specificati.
     * @param classe classe degli oggetti da prelevare.
     * @param campi mappa dei campi tramite cui fare la query.
     * @param <T> tipo parametrico degli oggetti.
     * @return lista di oggetti risultati dalla query JPQL costruita sui campi specificati.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    public <T> List<T> cercaPerCampi(Class<T> classe, Map<String, Object> campi) throws DatabaseException {
        try (EntityManager em = JpaUtil.getInstance().getEntityManager()) {
            return costruisciQueryPerCampi(em, classe, campi).getResultList();
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "Errore durante la ricerca multipla", e);
            throw new DatabaseException("Impossibile recuperare l'elenco dei dati dal database.", e);
        }
    }

    /**
     * Preleva il primo oggetto da una ricerca per campi.
     * @param classe classe dell'oggetto da prelevare.
     * @param campi mappa dei campi tramite cui fare la query.
     * @param <T> tipo parametrico dell'oggetto.
     * @return il primo oggetto della lista risultante dalla ricerca per campi.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    public <T> T cercaPrimoPerCampi(Class<T> classe, Map<String, Object> campi) throws DatabaseException {
        try (EntityManager em = JpaUtil.getInstance().getEntityManager()) {
            return costruisciQueryPerCampi(em, classe, campi)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "Errore durante la ricerca singola", e);
            throw new DatabaseException("Impossibile recuperare il record dal database.", e);
        }
    }

    /**
     * Aggiorna il valore della entry del datbaase relativa all'oggetto specificato.
     * @param oggetto l'oggetto la cui relativa entry va aggiornata.
     * @param <T> tipo parametrico dell'oggetto.
     * @return l'oggetto aggiornato nel database.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    public <T> T aggiorna(T oggetto) throws DatabaseException {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            T oggettoAggiornato = em.merge(oggetto);
            em.getTransaction().commit();
            return oggettoAggiornato;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOGGER.log(Level.SEVERE, "Errore durante l'aggiornamento dell'entità", e);
            // Qui prima c'era 'throw e;'. Ora lo impacchettiamo!
            throw new DatabaseException("Impossibile aggiornare i dati. Il server potrebbe essere offline.", e);
        } finally {
            em.close();
        }
    }

    /**
     * Elimina una entry dal database sulla base della classe e dell'id del relativo oggetto.
     * @param classe classe dell'oggetto la cui relativa entry va eliminata.
     * @param id id dell'oggetto la cui relativa entry va eliminata.
     * @param <T> tipo parametrico dell'oggetto.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    public <T> void elimina(Class<T> classe, Long id) throws DatabaseException {
        EntityManager em = JpaUtil.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            T oggetto = em.find(classe, id);
            if (oggetto != null) {
                em.remove(oggetto);
                em.getTransaction().commit();
            } else {
                em.getTransaction().commit();
                throw new DatabaseException("Impossibile eliminare: il record richiesto non esiste nel database.");
            }
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOGGER.log(Level.SEVERE, "Errore durante l'eliminazione dell'entità dal database", e);
            throw new DatabaseException("Errore di sistema durante l'eliminazione dei dati.", e);
        } finally {
            em.close();
        }
    }

}