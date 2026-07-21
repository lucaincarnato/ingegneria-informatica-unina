package exception;

/**
 * Evento eccezionale che incorre con richieste di accesso al database vuoto.
 */
public class DatiMancantiException extends Exception {

    public DatiMancantiException() {
        super("Nessun dato presente nel database per l'operazione richiesta.");
    }

    public DatiMancantiException(String messaggio) {
        super(messaggio);
    }
}
