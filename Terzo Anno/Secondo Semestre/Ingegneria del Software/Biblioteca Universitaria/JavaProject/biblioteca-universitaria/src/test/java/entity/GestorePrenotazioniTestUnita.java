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
 * Modulo di test black box di unità della classe GestorePrenotazioni.
 * Lo scopo del test è di valutare (almeno a grandi linee) tutte le possibili combinazioni di ingresso.
 * Lo spazio degli input è stato diviso nelle seguenti classi:
 * 1. La prenotazione è nello stesso giorno ma il check-in è fatto meno di un'ora prima;
 * 2. La prenotazione è nello stesso giorno ma il check-in è fatto più di un'ora prima;
 * 3. La prenotazione è nello stesso giorno ma il check-in è fatto dopo l'orario di inizio prenotazione;
 * 4. La prenotazione è nel giorno prima;
 * 5. La prenotazione è nel giorno dopo.
 * Sono stati valutati sia valori di input centrali per ogni classe ma anche i valori di frontiera, la quale è stata approcciata da entrambi i lati.
 * Sono stati utilizzati valori di ingresso diversi rispetto a quelli del modulo di test white box di struttura per simulare una forma elementare di random testing.
 */
@ExtendWith(MockitoExtension.class)
public class GestorePrenotazioniTestUnita {

    /**
     * Componente di persistenza simulato per isolare il comportamento della classe sotto test.
     */
    @Mock
    private GestorePersistenza gestorePersistenza;

    /**
     * Istanza della classe sotto test alla quale è iniettato il componente di persistenza simulato
     */
    @InjectMocks
    private GestorePrenotazioni gestorePrenotazioni;

    private Prenotazione prenotazione;
    private LocalDate oggi;
    private LocalTime adesso;

    @BeforeEach
    void setUp() {
        prenotazione = new Prenotazione();
        oggi = LocalDate.now();
        adesso = LocalTime.now();
    }

    /**
     * Classe 1 (la prenotazione è nello stesso giorno ma il check-in è fatto meno di un'ora prima, prenotazione da annullare).
     * @throws CheckInException evento eccezionale generico che incorre durante l'operazione di check-in.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    @Test
    void stessoGiorno_menoDiUnOra() throws CheckInException, DatabaseException {
        prenotazione.setStato(StatoPrenotazione.Attiva);
        prenotazione.setData(oggi);
        prenotazione.setOrarioInizio(adesso.plusMinutes(30));

        assertThrows(PrenotazioneScadutaException.class, () -> {
            gestorePrenotazioni.checkIn(prenotazione, oggi, adesso);
        });
        assertEquals(StatoPrenotazione.Annullata, prenotazione.getStato());
    }

    /**
     * Classe 1 (la prenotazione è nello stesso giorno ma il check-in è fatto esattamente un'ora prima, check-in valido, avvicinamento da destra).
     * @throws CheckInException evento eccezionale generico che incorre durante l'operazione di check-in.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    @Test
    void stessoGiorno_menoDiUnOra_frontiera() throws CheckInException, DatabaseException {
        prenotazione.setStato(StatoPrenotazione.Attiva);
        prenotazione.setData(oggi);
        prenotazione.setOrarioInizio(adesso.plusMinutes(59).plusMinutes(1));

        gestorePrenotazioni.checkIn(prenotazione, oggi, adesso);

        assertEquals(StatoPrenotazione.CheckedIn, prenotazione.getStato());
    }

    /**
     * Classe 2 (la prenotazione è nello stesso giorno e il check-in è fatto più un'ora prima, check-in valido).
     * @throws CheckInException evento eccezionale generico che incorre durante l'operazione di check-in.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    @Test
    void stessoGiorno_piuDiUnOra() throws CheckInException, DatabaseException {
        prenotazione.setStato(StatoPrenotazione.Attiva);
        prenotazione.setData(oggi);
        prenotazione.setOrarioInizio(adesso.plusMinutes(90));

        gestorePrenotazioni.checkIn(prenotazione, oggi, adesso);

        assertEquals(StatoPrenotazione.CheckedIn, prenotazione.getStato());
    }

    /**
     * Classe 2 (la prenotazione è nello stesso giorno e il check-in è fatto esattamente un'ora prima, check-in valido, avvicinamento da sinistra).
     * @throws CheckInException evento eccezionale generico che incorre durante l'operazione di check-in.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    @Test
    void stessoGiorno_piuDiUnOra_frontiera() throws CheckInException, DatabaseException {
        prenotazione.setStato(StatoPrenotazione.Attiva);
        prenotazione.setData(oggi);
        prenotazione.setOrarioInizio(adesso.plusMinutes(61).minusMinutes(1));

        gestorePrenotazioni.checkIn(prenotazione, oggi, adesso);

        assertEquals(StatoPrenotazione.CheckedIn, prenotazione.getStato());
    }

    /**
     * Classe 3 (la prenotazione è nello stesso giorno e il check-in è fatto un'ora dopo l'inizio della prenotazione, prenotazione da annullare).
     * @throws CheckInException evento eccezionale generico che incorre durante l'operazione di check-in.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    @Test
    void stessoGiorno_dopo() throws CheckInException, DatabaseException {
        prenotazione.setStato(StatoPrenotazione.Attiva);
        prenotazione.setData(oggi);
        prenotazione.setOrarioInizio(adesso.minusMinutes(60));

        assertThrows(PrenotazioneScadutaException.class, () -> {
            gestorePrenotazioni.checkIn(prenotazione, oggi, adesso);
        });
        assertEquals(StatoPrenotazione.Annullata, prenotazione.getStato());
    }

    /**
     * Classe 3 (la prenotazione è nello stesso giorno e il check-in è fatto all'orario di inizio della prenotazione, prenotazione da annullare).
     * @throws CheckInException evento eccezionale generico che incorre durante l'operazione di check-in.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    @Test
    void stessoGiorno_dopo_frontiera() throws CheckInException, DatabaseException {
        prenotazione.setStato(StatoPrenotazione.Attiva);
        prenotazione.setData(oggi);
        prenotazione.setOrarioInizio(adesso.minusMinutes(0));

        assertThrows(PrenotazioneScadutaException.class, () -> {
            gestorePrenotazioni.checkIn(prenotazione, oggi, adesso);
        });
        assertEquals(StatoPrenotazione.Annullata, prenotazione.getStato());
    }

    /**
     * Classe 4 (la prenotazione è nel giorno precedente ma il check-in è stato già effettuato, nessun cambio).
     * @throws CheckInException evento eccezionale generico che incorre durante l'operazione di check-in.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    @Test
    void giornoPrima_Annullata() throws CheckInException, DatabaseException {
        prenotazione.setStato(StatoPrenotazione.Annullata);
        prenotazione.setData(oggi.minusDays(1));
        prenotazione.setOrarioInizio(adesso);

        gestorePrenotazioni.checkIn(prenotazione, oggi, adesso);

        assertEquals(StatoPrenotazione.Annullata, prenotazione.getStato());
    }

    /**
     * Classe 4 (la prenotazione è nel giorno precedente ma il check-in non è stato effettuato e la prenotaizone è stata annullata, nessun cambio).
     * @throws CheckInException evento eccezionale generico che incorre durante l'operazione di check-in.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    @Test
    void giornoPrima_CheckedIn() throws CheckInException, DatabaseException {
        prenotazione.setStato(StatoPrenotazione.CheckedIn);
        prenotazione.setData(oggi.minusDays(1));
        prenotazione.setOrarioInizio(adesso);

        gestorePrenotazioni.checkIn(prenotazione, oggi, adesso);

        assertEquals(StatoPrenotazione.CheckedIn, prenotazione.getStato());
    }

    /**
     * Classe 4 (la prenotazione è nel giorno successivo e il check-in non è disponibile, nessun cambio).
     * @throws CheckInException evento eccezionale generico che incorre durante l'operazione di check-in.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    @Test
    void giornoDopo() throws CheckInException, DatabaseException {
        prenotazione.setStato(StatoPrenotazione.Attiva);
        prenotazione.setData(oggi.plusDays(1));
        prenotazione.setOrarioInizio(adesso);

        assertThrows(DataNonCorrispondenteException.class, () -> {
            gestorePrenotazioni.checkIn(prenotazione, oggi, adesso);
        });
        assertEquals(StatoPrenotazione.Attiva, prenotazione.getStato());
    }

}