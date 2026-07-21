package entity;

import database.GestorePersistenza;
import exception.CheckInException;
import exception.DatabaseException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Modulo di test black box di integrazione della classe GestorePrenotazioni.
 * Lo scopo del test è di valutare il ciclo CRUD (Create, Read, Update, Delete) della prenotazione, l'interazione con il database e la propagazione delle transizioni.
 * Non ha senso effettuare test di integrazione con il controller dal momento in cui esso è un pass-through per GestorePrenotazioni.
 */
public class GestorePrenotazioniTestIntegrazione {

    private GestorePrenotazioni gestorePrenotazioni;
    private GestorePersistenza gestorePersistenza;
    private Prenotazione prenotazione;

    /**
     * Crea gli elementi necessari al test e la prenotazione su cui effettuare il test.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    @BeforeEach
    void setUp() throws DatabaseException {
        gestorePersistenza = new GestorePersistenza();
        gestorePrenotazioni = new GestorePrenotazioni();
        prenotazione = new Prenotazione();

        prenotazione.setStato(StatoPrenotazione.Attiva);
        prenotazione.setData(LocalDate.now());
        prenotazione.setOrarioInizio(LocalTime.of(10, 0));

        gestorePersistenza.salva(prenotazione);
        assertNotNull(prenotazione.getId(), "La prenotazione deve avere un ID generato dal DB.");
    }

    /**
     * Elimina dal database la prenotazione di test, dal momento in cui non è stata effettuata realmente da alcuno studente.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    @AfterEach
    void tearDown() throws DatabaseException {
        if (prenotazione != null && prenotazione.getId() != null) {
            gestorePersistenza.elimina(Prenotazione.class, prenotazione.getId());
        }
    }

    /**
     * Testa la lettura e l'aggiornamento della prenotazione integrando l'interazione con il database in relazione all'operazione di check-in.
     * @throws CheckInException evento eccezionale generico che incorre durante l'operazione di check-in.
     * @throws DatabaseException evento eccezionale che incorre a database non collegato.
     */
    // 2. Read, 3. Update
    @Test
    void checkInSuccesso_AggiornaDatabase() throws CheckInException, DatabaseException {
        LocalTime adesso = LocalTime.of(9, 0);
        gestorePrenotazioni.checkIn(prenotazione, LocalDate.now(), adesso);
        assertEquals(StatoPrenotazione.CheckedIn, prenotazione.getStato());

        // Valutazione propagazione delle transizioni
        Prenotazione prenotazioneLettaDalDB = gestorePersistenza.cercaPerCampi(
                Prenotazione.class,
                java.util.Map.of(
                        "id", prenotazione.getId()
                )
        ).getFirst();

        assertEquals(
                StatoPrenotazione.CheckedIn, prenotazioneLettaDalDB.getStato(),
                "Il database non ha salvato correttamente il nuovo stato!"
        );
    }

}