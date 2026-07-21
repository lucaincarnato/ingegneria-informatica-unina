package boundary;

import controller.ControllerPrenotazioni;
import entity.Prenotazione;
import exception.DatabaseException;
import exception.DatiMancantiException;

import java.awt.*;
import java.util.List;
import javax.swing.*;

/**
 * Pagina di check-in dove appariranno tutte le prenotazioni effettuate da uno studente.
 */
public class CheckInPage {

    private JPanel checkInPanel;
    private JScrollPane scrollPanel;
    private JPanel listContainer;

    /**
     * Costruttore della GUI e delle card associate ad ogni prenotazione.
     */
    public CheckInPage() {
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        try {
            List<Prenotazione> prenotazioni = ControllerPrenotazioni.getPrenotazioni(ControllerPrenotazioni.getStudente());
            for (Prenotazione prenotazione : prenotazioni) {
                CardPrenotazione cardPrenotazione = new CardPrenotazione(prenotazione);
                JPanel cardPanel = cardPrenotazione.getPanel();
                Dimension cardSize = cardPanel.getPreferredSize();
                cardPanel.setMinimumSize(cardSize);
                cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardSize.height));
                cardPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
                listContainer.add(cardPanel);
            }
        } catch (DatiMancantiException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Attenzione", JOptionPane.WARNING_MESSAGE);
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Impossibile connettersi al database. Riprova più tardi.", "Errore Critico", JOptionPane.ERROR_MESSAGE);
        }
        scrollPanel.setViewportView(listContainer);
    }

    /**
     * Metodo di apertura della pagina di check-in dopo l'interazione con l'apposito pulsante.
     * @return il frame contenente la pagina di check-in.
     */
    public JFrame apriCheckInPage() {
        JFrame frame = new JFrame("Effettua check-in");
        frame.setContentPane(checkInPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(854, 720);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }

}
