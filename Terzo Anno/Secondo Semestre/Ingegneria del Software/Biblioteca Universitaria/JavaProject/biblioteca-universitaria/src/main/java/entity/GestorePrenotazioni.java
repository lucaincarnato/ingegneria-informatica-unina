package entity;

import database.GestorePersistenza;
import exception.CheckInException;
import exception.DataNonCorrispondenteException;
import exception.DatabaseException;
import exception.PrenotazioneScadutaException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Modulo di gestione delle prenotazioni e del loro ciclo di vita.
 */
public class GestorePrenotazioni {

    /**
     * Modulo di gestione della persistenza delle informazioni nel database.
     */
	private final GestorePersistenza gestorePersistenza;

	public GestorePrenotazioni() {
		this.gestorePersistenza = new GestorePersistenza();
	}

	public GestorePrenotazioni(GestorePersistenza gestorePersistenza) {
		this.gestorePersistenza = gestorePersistenza;
	}

    /**
	 * Effettua il check-in di una prenotazione sulla base della data e dell'orario di inizio della prenotazione e di quanto tempo rimane fino al suo inizio.
     * @param prenotazione prenotazione di cui si vuole fare il check-in.
     * @param oggi giornata in cui è effettuato il check-in.
     * @param adesso orario in cui è effettuato il check-in.
     * @throws CheckInException evento eccezionale generico che incorre durante l'operazione di check-in.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    public void checkIn(Prenotazione prenotazione, LocalDate oggi, LocalTime adesso) throws CheckInException, DatabaseException {
		if (prenotazione.getStato() != StatoPrenotazione.Attiva) { return; }
		LocalDate dataPrenotazione = prenotazione.getData();
		LocalTime orarioPrenotazione = prenotazione.getOrarioInizio();
		// Check-in non ancora disponibile
		if (!confrontaData(dataPrenotazione, oggi)) {
			throw new DataNonCorrispondenteException();
		}
		// Check-in disponibile ma non effettuato in tempo
		if (!confrontaOrario(orarioPrenotazione, adesso)) {
			prenotazione.setStato(StatoPrenotazione.Annullata);
			gestorePersistenza.aggiorna(prenotazione);
			throw new PrenotazioneScadutaException();
		}
		// Check-in disponibile ed effettuato in tempo
		prenotazione.setStato(StatoPrenotazione.CheckedIn);
		gestorePersistenza.aggiorna(prenotazione);
	}

    /**
	 * Verifica se la data della prenotazione coincide con la data in cui è stato effettuato il check-in.
     * @param dataPrenotazione data della prenotazione di cui si vuole effettuare il check-in.
     * @param oggi giornata in cui è effettuato il check-in.
     * @return true se il check-in è effettuato nella stessa giornata della prenotazione, false altrimenti.
     */
	private boolean confrontaData(LocalDate dataPrenotazione, LocalDate oggi) {
		return oggi.equals(dataPrenotazione);
	}

    /**
	 * Verifica se il check-in è stato effettuato entro un certo intervallo di tempo di tolleranza.
     * @param orarioPrenotazione orario di inizio della prenotazione.
     * @param adesso orario in cui è effettuato il check-in.
     * @return true se il check-in è effettuato a più di x minuti dall'inizio della prenotazione, con x intervallo di tempo di tolleranza, false altrimenti.
     */
	private boolean confrontaOrario(LocalTime orarioPrenotazione, LocalTime adesso) {
		long differenzaMinuti = Duration.between(adesso, orarioPrenotazione).toMinutes();
        // Tolleranza per il ritardo in minuti (60 minuti è un valore d'esempio)
        long TOLLERANZA_MINUTI = 60;
        return differenzaMinuti >= TOLLERANZA_MINUTI;
	}

    /**
	 * Preleva dal database gli studenti registrati a sistema.
     * @return gli studenti registrati a sistema e salvati correttamente nel database.
	 * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
	public List<Studente> getStudenti() throws DatabaseException {
		return gestorePersistenza.cercaPerCampi(Studente.class, Map.of());
	}

    /**
	 * Preleva dal database le prenotazioni effettuate dallo studente specificato.
     * @param studente studente del quale si vogliono le prenotazioni effettuate.
     * @return le prenotazioni effettuate dallo studente indicato.
	 * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
	public List<Prenotazione> getPrenotazioniPerStudente(Studente studente) throws DatabaseException {
		System.out.println(studente.getNome() + " " + studente.getId());
		return gestorePersistenza.cercaPerCampi(
				Prenotazione.class,
				Map.of(
						"studente.id", studente.getId()
				)
		);
	}
}