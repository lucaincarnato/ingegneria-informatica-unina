package boundary;

import javax.swing.*;

/**
 * Landing page principale che appare dopo l'avvio del sistema
 */
public class MainFrame {

    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JButton startButton;
    private JFrame frameEffettuaCheckIn;

    /**
     * Costruttore della GUI. Aggiunge il collegamento alla pagina di check-in tramite l'apposito pulsante.
     */
    public MainFrame() {
        startButton.addActionListener(e -> {
            if (frameEffettuaCheckIn == null || !frameEffettuaCheckIn.isDisplayable()) {
                CheckInPage checkInPage = new CheckInPage();
                frameEffettuaCheckIn = checkInPage.apriCheckInPage();
                frameEffettuaCheckIn.setLocationRelativeTo(null);
                frameEffettuaCheckIn.setVisible(true);
            } else {
                frameEffettuaCheckIn.toFront();
                frameEffettuaCheckIn.requestFocus();
            }
        });
    }

    /**
     * Metodo di apertura del main frame dopo l'avvio del sistema.
     */
    public void apriMainFrame() {
        JFrame frame = new JFrame("Biblioteca universitaria");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(854, 720);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
