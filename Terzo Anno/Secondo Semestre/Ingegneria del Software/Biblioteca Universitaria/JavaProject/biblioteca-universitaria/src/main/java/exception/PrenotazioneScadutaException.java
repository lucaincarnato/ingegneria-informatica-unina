package exception;

/**
 * Evento eccezionale che incorre a prenotazione scaduta e annullata.
 */
public class PrenotazioneScadutaException extends CheckInException {
    public PrenotazioneScadutaException() { super("La prenotazione è scaduta ed è stata annullata."); }
}
