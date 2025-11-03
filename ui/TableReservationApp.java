package ui;
import service.ReservationManager;
import javax.swing.SwingUtilities;

/**
 * Main application launcher class.
 */
public class TableReservationApp {
    
    public static void main(String[] args) {
        // Instantiate the core business logic manager
        ReservationManager manager = new ReservationManager();
        
        // Use SwingUtilities.invokeLater to ensure thread safety when launching the GUI
        SwingUtilities.invokeLater(() -> {
            new MainScreenGUI(manager).setVisible(true);
        });
    }
}
