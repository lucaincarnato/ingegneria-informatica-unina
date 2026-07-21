package exception;

/**
 *  Evento eccezionale generico che incorre durante l'operazione di check-in.
 */
public class CheckInException extends Exception {
    public CheckInException(String messaggio) { super(messaggio); }
}


