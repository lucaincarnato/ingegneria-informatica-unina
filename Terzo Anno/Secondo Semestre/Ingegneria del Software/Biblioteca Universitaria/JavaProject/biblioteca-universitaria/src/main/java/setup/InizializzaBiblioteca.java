package setup;

import boundary.MainFrame;
import database.JpaUtil;
import entity.*;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Inizializza il sistema, il database, ne inserisce valori placeholder, e l'interfaccia grafica.
 */
public class InizializzaBiblioteca {

    /**
     * Logger utilizzato per raccogliere informazioni dai fault critici.
     */
    private static final Logger LOGGER = Logger.getLogger(InizializzaBiblioteca.class.getName());

    static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, e) -> {
            System.err.println("Errore critico non gestito nel thread: " + thread.getName());
            LOGGER.log(Level.SEVERE, "Si è verificato un errore critico nel thread", e);

            JOptionPane.showMessageDialog(
                    null,
                    "Si è verificato un errore imprevisto di sistema.\n\nDettaglio: " + e.getMessage(),
                    "Errore Fatale",
                    JOptionPane.ERROR_MESSAGE
            );
        });
        try {
            System.out.println("Connessione al database in corso...");
            JpaUtil.getInstance();
            inizializzaDatabase();
            System.out.println("Database inizializzato correttamente.");
        } catch (Exception e) {
            System.err.println("Attenzione: Impossibile inizializzare il database al primo avvio.");
            LOGGER.log(Level.SEVERE, "Impossibile inizializzare il database al primo avvio.", e);
            JOptionPane.showMessageDialog(
                    null,
                    "Attenzione: il database è offline o i dati di base non sono stati caricati.\nL'applicazione si aprirà, ma alcune funzioni potrebbero non essere disponibili.",
                    "Problema di Rete",
                    JOptionPane.WARNING_MESSAGE
            );
        }
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.apriMainFrame();
        });
    }

    /**
     * Popola il database con valori placeholder come sostituzione dei casi d'uso non ancora implementati.
     * È possibile fare test di sistema con i valori inseriti di input inseriti, che rappresentano possibili valori delle classi in cui è stato diviso lo spazio degli input. 
     * Sono stati utilizzati valori di ingresso diversi rispetto a quelli dei moduli di test per simulare una forma elementare di random testing.
     */
    private static void inizializzaDatabase(){
        // Creazione bibliotecari
        Bibliotecario chiaraDeLuca = new Bibliotecario(
                "Chiara",
                "De Luca",
                "chiara.deluca@unina.it",
                "B23004829"
        );
        Bibliotecario lorenzoRomano = new Bibliotecario(
                "Lorenzo",
                "Romano",
                "lorenzo.romano@unina.it",
                "B23003810"
        );
        // Creazione studenti
        Studente svevaEsposito = new Studente(
                "Sveva",
                "Esposito",
                "sveva.esposito@studenti.unina.it",
                "N46101203"
        );
        // Creazione sale studio
        SalaStudio pierPaoloPasolini = new SalaStudio(
                "Pier Paolo Pasolini",
                """
                    spazio dedicato all'approfondimento critico e alla ricerca individuale. \
                    Le postazioni ampie e silenziose favoriscono la concentrazione e l'analisi di testi complessi, \
                    onorando lo spirito intellettuale e libero di Pasolini.
                """,
                LocalTime.of(8, 30),
                LocalTime.of(18, 30),
                90
        );
        SalaStudio rosaParks = new SalaStudio(
                "Rosa Parks",
                """
                    Un ambiente dinamico e inclusivo progettato per favorire la collaborazione tra studenti. \
                    Attrezzata con tavoli condivisi e supporti per le presentazioni, \
                    la sala celebra la forza della collettività e l'impegno civile di Rosa Parks.
                """,
                LocalTime.of(9, 0),
                LocalTime.of(21, 0),
                50
        );
        // Creazione prenotazioni
        Prenotazione prenotazioneFutura = new Prenotazione(
                LocalDate.now().plusDays(2),
                LocalTime.of(12, 30).truncatedTo(ChronoUnit.MINUTES),
                LocalTime.of(17, 30).truncatedTo(ChronoUnit.MINUTES),
                StatoPrenotazione.Attiva,
                pierPaoloPasolini
        );
        Prenotazione prenotazioneNoCheckIn = new Prenotazione(
                LocalDate.now(),
                LocalTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES),
                LocalTime.now().plusHours(4).plusHours(2).truncatedTo(ChronoUnit.MINUTES),
                StatoPrenotazione.Attiva,
                rosaParks
        );
        Prenotazione prenotazionePresente = new Prenotazione(
                LocalDate.now(),
                LocalTime.now().plusMinutes(35).truncatedTo(ChronoUnit.MINUTES),
                LocalTime.now().plusHours(4).plusMinutes(35).truncatedTo(ChronoUnit.MINUTES),
                StatoPrenotazione.Attiva,
                pierPaoloPasolini
        );
        Prenotazione prenotazioneFuturaStessoGirono = new Prenotazione(
                LocalDate.now(),
                LocalTime.now().minusHours(2).truncatedTo(ChronoUnit.MINUTES),
                LocalTime.now().minusHours(4).minusHours(2).truncatedTo(ChronoUnit.MINUTES),
                StatoPrenotazione.Attiva,
                rosaParks
        );
        Prenotazione prenotazionePassata = new Prenotazione(
                LocalDate.now().plusDays(-2),
                LocalTime.of(10, 45).truncatedTo(ChronoUnit.MINUTES),
                LocalTime.of(12, 30).truncatedTo(ChronoUnit.MINUTES),
                StatoPrenotazione.Annullata,
                rosaParks
        );
        // Aggiungi sala studio a bibliotecario
        chiaraDeLuca.aggiungiSala(pierPaoloPasolini);
        lorenzoRomano.aggiungiSala(rosaParks);
        // Aggiungi prenotazioni a studente
        svevaEsposito.aggiungiPrenotazione(prenotazioneFutura);
        svevaEsposito.aggiungiPrenotazione(prenotazionePresente);
        svevaEsposito.aggiungiPrenotazione(prenotazioneNoCheckIn);
        svevaEsposito.aggiungiPrenotazione(prenotazioneFuturaStessoGirono);
        svevaEsposito.aggiungiPrenotazione(prenotazionePassata);
        // inserimento database
        InserisciPlaceholder.aggiungi(chiaraDeLuca);
        InserisciPlaceholder.aggiungi(lorenzoRomano);
        InserisciPlaceholder.aggiungi(svevaEsposito);
        InserisciPlaceholder.aggiungi(pierPaoloPasolini);
        InserisciPlaceholder.aggiungi(rosaParks);
        InserisciPlaceholder.aggiungi(prenotazioneFutura);
        InserisciPlaceholder.aggiungi(prenotazionePresente);
        InserisciPlaceholder.aggiungi(prenotazioneNoCheckIn);
        InserisciPlaceholder.aggiungi(prenotazioneFuturaStessoGirono);
        InserisciPlaceholder.aggiungi(prenotazionePassata);
    }

}
