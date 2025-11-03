package ui;
import service.ReservationManager;
import javax.swing.SwingUtilities;

public class TableReservationApp {
    
    public static void main(String[] args) {
        ReservationManager manager = new ReservationManager();
        
        SwingUtilities.invokeLater(() -> {
            new MainScreenGUI(manager).setVisible(true);
        });
    }
}
