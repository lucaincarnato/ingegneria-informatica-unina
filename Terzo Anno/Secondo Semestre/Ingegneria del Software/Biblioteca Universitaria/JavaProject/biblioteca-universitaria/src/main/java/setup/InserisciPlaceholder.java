package setup;

import database.GestorePersistenza;
import entity.*;
import exception.DatabaseException;
import exception.DatiMancantiException;

import java.util.Map;

/**
 * Inserisce dati placeholder nel sistema e nel database come sostituzione dei casi d'uso non ancora implementati.
 */
public class InserisciPlaceholder {

    private static final GestorePersistenza gp = new GestorePersistenza();

    private InserisciPlaceholder() {}

    /**
     * Inserisce nel sistema un bibliotecario.
     * @param b bibliotecario da aggiungere al sistema.
     */
    public static void aggiungi(Bibliotecario b) {
        try {
            gp.salva(b);
        } catch (DatabaseException e) {
            System.err.println("Errore tecnico durante l'inserimento del Bibliotecario: " + e.getMessage());
        }
    }

    /**
     * Inserisce nel sistema uno studente.
     * @param s studente da aggiungere al sistema.
     */
    public static void aggiungi(Studente s) {
        try {
            gp.salva(s);
        } catch (DatabaseException e) {
            System.err.println("Errore tecnico durante l'inserimento del Bibliotecario: " + e.getMessage());
        }
    }

    /**
     * Inserisce nel sistema una sala studio.
     * @param ss sala studio da aggiungere al sistema.
     */
    public static void aggiungi(SalaStudio ss) {
        try {
            Bibliotecario b = gp.cercaPrimoPerCampi(
                    Bibliotecario.class,
                    Map.of(
                            "nome", ss.getBibliotecario().getNome(),
                            "cognome", ss.getBibliotecario().getCognome(),
                            "codiceIdentificativo", ss.getBibliotecario().getCodiceIdentificativo()
                    )
            );
            if (b == null) {
                throw new DatiMancantiException("Impossibile salvare la Sala Studio: il Bibliotecario associato non è presente nel DB.");
            }
            gp.salva(ss);
        } catch (DatabaseException e) {
            System.err.println("Errore di persistenza: " + e.getMessage());
        } catch (DatiMancantiException e) {
            System.err.println("️Errore di coerenza dati: " + e.getMessage());
        }
    }

    /**
     * Inserisce nel sistema una prenotazione.
     * @param p prenotazione da aggiungere al sistema.
     */
    public static void aggiungi(Prenotazione p) {
        try {
            SalaStudio ss = gp.cercaPrimoPerCampi(
                    SalaStudio.class,
                    Map.of(
                            "nome", p.getSalaStudio().getNome(),
                            "descrizione", p.getSalaStudio().getDescrizione()
                    )
            );
            Studente s = gp.cercaPrimoPerCampi(
                    Studente.class,
                    Map.of(
                            "nome", p.getStudente().getNome(),
                            "cognome", p.getStudente().getCognome(),
                            "numeroMatricola", p.getStudente().getNumeroMatricola()
                    )
            );
            if (ss == null) {
                throw new DatiMancantiException("Impossibile salvare la Prenotazione: Sala Studio non esistente.");
            }
            if (s == null) {
                throw new DatiMancantiException("Impossibile salvare la Prenotazione: Studente non esistente.");
            }
            gp.salva(p);
        } catch (DatabaseException e) {
            System.err.println("Errore critico di database: " + e.getMessage());
        } catch (DatiMancantiException e) {
            System.err.println("Violazione integrità dati: " + e.getMessage());
        }
    }

}