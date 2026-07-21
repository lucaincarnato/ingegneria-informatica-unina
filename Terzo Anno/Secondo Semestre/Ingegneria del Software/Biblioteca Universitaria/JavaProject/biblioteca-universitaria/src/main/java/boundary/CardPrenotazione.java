package boundary;

import controller.ControllerPrenotazioni;
import entity.Prenotazione;
import exception.CheckInException;
import exception.DataNonCorrispondenteException;
import exception.DatabaseException;
import exception.PrenotazioneScadutaException;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Card contenente le informazioni di una prenotazione e il pulsante per effettuarne il check-in.
 */
public class CardPrenotazione {

    private JPanel prenotazione;
    private JLabel salaStudio;
    private JLabel bibliotecario;
    private JLabel infoPrenotazione;
    private JButton checkIn;
    private JLabel stato;

    /**
     * Costruttore della card. Invoca il controller per l'operazione di check-in all'interazione con l'apposito pulsante.
     * @param prenotazione prenotazione di cui si vuole fare il check-in
     */
    public CardPrenotazione(Prenotazione prenotazione) {
        salaStudio.setText(prenotazione.getSalaStudio().getNome());
        infoPrenotazione.setText(
                prenotazione.getData().toString() +
                        ", " +
                        prenotazione.getOrarioInizio().toString() +
                        " - " +
                        prenotazione.getOrarioFine().toString()
        );

        bibliotecario.setText("Gestita da: " +
                (prenotazione.getSalaStudio() != null && prenotazione.getSalaStudio().getBibliotecario() != null ?
                prenotazione.getSalaStudio().getBibliotecario().getNome() : "Non assegnato")
        );

        stato.setText(prenotazione.getStato().toString());

        checkIn.setEnabled(!Objects.equals(prenotazione.getStato().toString(), "Annullata"));
        checkIn.setText(!Objects.equals(prenotazione.getStato().toString(), "Attiva") ? "Check-in non disponibile" : "Effettua check-in");
        checkIn.addActionListener(e -> {
            LocalDate oggi = LocalDate.now();
            LocalTime adesso = LocalTime.now();
            try {
                // Effettua il check-in e aggiorna le informazioni a schermo sulla base del risultato.
                ControllerPrenotazioni.checkIn(prenotazione, oggi, adesso);
                stato.setText(prenotazione.getStato().toString());
                checkIn.setEnabled(false);
                checkIn.setText("Check-in effettuato");
                // Alert di successo, check-in effettuato.
                JOptionPane.showMessageDialog(null, "Check-in effettuato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } catch (DataNonCorrispondenteException ex) {
                // Alert di warning, non è possibile effettuare il check-in.
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Attenzione", JOptionPane.WARNING_MESSAGE);
            } catch (PrenotazioneScadutaException ex) {
                // Alert di errore, la prenotazione è scaduta ed è stata annullata.
                stato.setText(prenotazione.getStato().toString());
                checkIn.setEnabled(false);
                checkIn.setText("Check-in non disponibile");
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (CheckInException ex) {
                // Alert di errore, check-in non effettuato.
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (DatabaseException ex) {
                // Alert di errore, il database non è collegato. 
                JOptionPane.showMessageDialog(null, "Impossibile confermare il check-in sul server: " + ex.getMessage(), "Errore di Persistenza", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Metodo di restituzione della card di una prenotazione.
     * @return la card di prenotazione.
     */
    public JPanel getPanel() {
        return prenotazione;
    }

}