package exception;


/**
 * Evento eccezionale che incorre a database non collegato.
 */
public class DatabaseException extends Exception {

    public DatabaseException() {
        super("Errore di comunicazione con il database. Il servizio potrebbe essere offline.");
    }

    public DatabaseException(String messaggio) {
        super(messaggio);
    }

    public DatabaseException(String messaggio, Throwable causa) {
        super(messaggio, causa);
    }
}