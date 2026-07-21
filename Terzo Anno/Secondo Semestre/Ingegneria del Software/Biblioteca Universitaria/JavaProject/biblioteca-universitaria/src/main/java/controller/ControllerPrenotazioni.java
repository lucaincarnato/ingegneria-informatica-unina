package controller;

import entity.GestorePrenotazioni;
import entity.Prenotazione;
import entity.Studente;
import exception.CheckInException;
import exception.DatabaseException;
import exception.DatiMancantiException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Strato controller dell'architettura che regola le operazioni da fare sull package entity sulla base dell'input utente nel package boundary.
 */
public class ControllerPrenotazioni {

    /**
     * Preleva la lista di prenotazioni effettuate da uno studente.
     * @param studente studente di cui si vogliono prelevare le prenotazioni.
     * @return la lista di prenotazioni effettuate dallo studente.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    public static List<Prenotazione> getPrenotazioni(Studente studente) throws DatabaseException {
        GestorePrenotazioni gestorePrenotazioni = new GestorePrenotazioni();
        return gestorePrenotazioni.getPrenotazioniPerStudente(studente);
    }

    /**
     * Preleva il primo studente registrato nel database come sostituzione dei casi d'uso non ancora implementati.
     * @return il primo studente registrato nel database.
     * @throws DatiMancantiException  evento eccezionale che incorre con richieste di accesso al database vuoto.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    public static Studente getStudente() throws DatiMancantiException, DatabaseException {
        GestorePrenotazioni gestorePrenotazioni = new GestorePrenotazioni();
        List<Studente> studenti = gestorePrenotazioni.getStudenti();
        if (studenti == null || studenti.isEmpty()) {
            throw new DatiMancantiException("Nessuno studente trovato nel database.");
        }
        return studenti.getFirst();
    }

    /**
     * Effettua il check-in di una prenotazione sulla base della prenotazione di cui lo studente vuole fare il check-in.
     * @param prenotazione prenotazione di cui si vuole fare il check-in.
     * @param oggi giornata in cui è effettuato il check-in.
     * @param adesso orario in cui è effettuato il check-in.
     * @throws CheckInException evento eccezionale generico che incorre durante l'operazione di check-in.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    public static void checkIn(Prenotazione prenotazione, LocalDate oggi, LocalTime adesso) throws CheckInException, DatabaseException {
        GestorePrenotazioni gestorePrenotazioni = new GestorePrenotazioni();
        gestorePrenotazioni.checkIn(prenotazione, oggi, adesso);
    }

}
