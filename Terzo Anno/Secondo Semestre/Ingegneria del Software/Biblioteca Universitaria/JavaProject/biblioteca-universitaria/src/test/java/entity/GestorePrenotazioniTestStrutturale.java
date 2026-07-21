package entity;

import database.GestorePersistenza;
import exception.CheckInException;
import exception.DataNonCorrispondenteException;
import exception.DatabaseException;
import exception.PrenotazioneScadutaException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Modulo di test white box di struttura della classe GestorePrenotazioni.
 * Lo scopo del test è di valutare la possibilità del sistema di incorrere in tutti i possibili cammini nell'esecuzione del caso d'uso.
 * I cammini in questione sono i seguenti (in relazione al file GestorePrenotazioni.java):
 * 1. Non si può fare il check-in di una prenotazione già checked-in o annullata (riga 26);
 * 2. Non si può fare il check-in di una prenotazione non di oggi (riga 27-33);
 * 3. Non si può fare il check-in di una prenotazione a meno di 60 minuti dal suo inizio (riga 34-39);
 * 4. Si può fare il check-in (riga 40-43).
 * Sono stati utilizzati valori di ingresso diversi rispetto a quelli del modulo di test black box di unità per simulare una forma elementare di random testing.
 */
@ExtendWith(MockitoExtension.class)
public class GestorePrenotazioniTestStrutturale {

    /**
     * Componente di persistenza simulato per isolare il comportamento della classe sotto test.
     */
    @Mock
    private GestorePersistenza gestorePersistenza;

    /**
     * Istanza della classe sotto test alla quale è iniettato il componente di persistenza simulato.
     */
    @InjectMocks
    private GestorePrenotazioni gestorePrenotazioni;

    private Prenotazione prenotazione;
    private LocalDate oggi;
    private LocalTime adesso;

    /**
     * Crea la prenotazione su cui effettuare il test.
     */
    @BeforeEach
    void setUp() {
        prenotazione = new Prenotazione();
        oggi = LocalDate.now();
        adesso = LocalTime.now();
    }

    /**
     * Percorso di test numero 1 (non si può fare il check-in di una prenotazione già checked-in o annullata).
     * @throws CheckInException evento eccezionale generico che incorre durante l'operazione di check-in.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    @Test
    void prenotazioneNonAttiva_path1() throws CheckInException, DatabaseException {
        prenotazione.setStato(StatoPrenotazione.CheckedIn);
        prenotazione.setData(oggi);
        prenotazione.setOrarioInizio(adesso);

        gestorePrenotazioni.checkIn(prenotazione, oggi, adesso);

        assertEquals(StatoPrenotazione.CheckedIn, prenotazione.getStato());
    }

    /**
     * Percorso di test numero 2 (non si può fare il check-in di una prenotazione non di oggi).
     * @throws CheckInException evento eccezionale generico che incorre durante l'operazione di check-in.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    @Test
    void prenotazioneNonDiOggi_path2() throws CheckInException, DatabaseException {
        prenotazione.setStato(StatoPrenotazione.Attiva);
        prenotazione.setData(oggi.plusDays(2));
        prenotazione.setOrarioInizio(adesso);

        assertThrows(DataNonCorrispondenteException.class, () -> {
            gestorePrenotazioni.checkIn(prenotazione, oggi, adesso);
        });
        assertEquals(StatoPrenotazione.Attiva, prenotazione.getStato());
    }

    /**
     * Percorso di test numero 3 (non si può fare il check-in di una prenotazione a meno di 60 minuti dal suo inizio).
     * @throws CheckInException evento eccezionale generico che incorre durante l'operazione di check-in.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    @Test
    void prenotazioneSottoTolleranza_path3() throws CheckInException, DatabaseException {
        prenotazione.setStato(StatoPrenotazione.Attiva);
        prenotazione.setData(oggi);
        prenotazione.setOrarioInizio(adesso.plusMinutes(15));

        assertThrows(PrenotazioneScadutaException.class, () -> {
            gestorePrenotazioni.checkIn(prenotazione, oggi, adesso);
        });
        assertEquals(StatoPrenotazione.Annullata, prenotazione.getStato());
    }

    /**
     * Percorso di test numero 4 (Si può fare il check-in).
     * @throws CheckInException evento eccezionale generico che incorre durante l'operazione di check-in.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    @Test
    void prenotazioneCheckIn_path3() throws CheckInException, DatabaseException {
        prenotazione.setStato(StatoPrenotazione.Attiva);
        prenotazione.setData(oggi);
        prenotazione.setOrarioInizio(adesso.plusMinutes(120));

        gestorePrenotazioni.checkIn(prenotazione, oggi, adesso);

        assertEquals(StatoPrenotazione.CheckedIn, prenotazione.getStato());
    }

}