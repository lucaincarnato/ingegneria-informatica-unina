package exception;

/**
 * Evento eccezionale che incorre quando il check-in è effettuato in una giornata diversa rispetto a quella di prenotazione
 */
public class DataNonCorrispondenteException extends CheckInException {
    public DataNonCorrispondenteException() { super("La data della prenotazione non corrisponde a oggi."); }
}
