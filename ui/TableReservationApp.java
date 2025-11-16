package ui;
/*
 * This is the main application class to launch the restaurant table reservation system.
 */
import service.ReservationManager;
import javax.swing.SwingUtilities;

public class TableReservationApp {
    
    public static void main(String[] args) {
        ReservationManager manager = new ReservationManager();
        // Launch the main GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new MainScreenGUI(manager).setVisible(true);
        });
    }
}
