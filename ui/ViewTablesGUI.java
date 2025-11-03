package ui;

import model.AbstractTable;
import model.Reservation;
import service.ReservationManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

/**
 * Dedicated window for viewing table statuses and handling inline actions.
 */
public class ViewTablesGUI extends JFrame {

    private final ReservationManager manager;
    private JTable reservationTable;
    private DefaultTableModel tableModel;

    private static final String[] COLUMN_NAMES = {"Table No", "Type", "Capacity", "Status", "Action"};

    public ViewTablesGUI(ReservationManager manager) {
        super("Table Status - The Spice India");
        this.manager = manager;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this window, not the main app
        setSize(700, 450);
        
        setupTable();
        setupLayout();
        updateReservationTable();
        setLocationRelativeTo(null);
    }
    
    private void setupTable() {
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        reservationTable = new JTable(tableModel);
        
        // Use the inner classes from this new frame
        reservationTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        reservationTable.getColumn("Action").setCellEditor(new ButtonEditor(new JTextField(), this));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Add a refresh button at the top
        JButton refreshButton = new JButton("Refresh Status");
        refreshButton.addActionListener(e -> updateReservationTable());
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(refreshButton);
        add(topPanel, BorderLayout.NORTH);

        // Center: Reservation Info Table
        add(new JScrollPane(reservationTable), BorderLayout.CENTER);
    }
    
    // --- Core Functionality (Array-based) ---

    public void updateReservationTable() {
        tableModel.setRowCount(0);

        for (AbstractTable table : manager.getAllTables()) {
            if (table != null) {
                Vector<Object> row = new Vector<>();
                row.add(table.getTableNumber());
                row.add(table.getType());
                row.add(table.getCapacity());
                row.add(table.isReserved() ? "RESERVED" : "NOT RESERVED");
                // The button text is correctly set here
                row.add(table.isReserved() ? "View/Complete" : "Book"); 
                tableModel.addRow(row);
            }
        }
        tableModel.fireTableDataChanged();
    }

    public void showReservationDetails(int tableNo) {
        Reservation reservation = manager.getReservationByTableNumber(tableNo);
        
        // If the table is not reserved, prompt the user to book
        if (reservation == null) {
            int result = JOptionPane.showConfirmDialog(this, 
                "Table " + tableNo + " is NOT RESERVED. Would you like to book it now?", 
                "Book Table", JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                 // Launch the Add Reservation dialog (Need reference to the main app/dialog)
                 // NOTE: For simplicity, we'll just show a message here. Real implementation needs a direct link.
                 JOptionPane.showMessageDialog(this, "Please use the 'Add Reservation' button on the main menu.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            return;
        }

        // --- Logic for Reserved Table (View/Complete) ---
        String details = manager.getReservationDetails(reservation, "name", "phone", "time");
        
        Object[] options = {"Mark as Complete", "Close"};
        int result = JOptionPane.showOptionDialog(this,
            details,
            "Reservation Info - Table " + tableNo,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null, options, options[1]);

        if (result == JOptionPane.YES_OPTION) {
            manager.removeReservation(tableNo); 
            JOptionPane.showMessageDialog(this, "Reservation for Table " + tableNo + " marked complete.", "Complete", JOptionPane.INFORMATION_MESSAGE);
            updateReservationTable(); // Refresh the table after completion
        }
    }
    
    // --- Inner Classes for JTable Button Functionality (Renderer and Editor) ---
    
    /**
     * Renders the JTable cell as a functional button.
     */
    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() { setOpaque(true); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            // FIX: Ensure the renderer always uses the value passed to it
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    /**
     * Handles the click action when the button is pressed in the JTable cell.
     */
    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int tableRow;
        private ViewTablesGUI parentFrame; // Reference to the parent frame

        public ButtonEditor(JTextField textField, ViewTablesGUI parent) {
            super(textField);
            setClickCountToStart(1);
            this.parentFrame = parent;
            
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                fireEditingStopped();
                int tableNo = (Integer) parentFrame.reservationTable.getValueAt(tableRow, 0);
                parentFrame.showReservationDetails(tableNo);
                // After action, the table will be refreshed by showReservationDetails, resolving the text issue.
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.tableRow = row;
            // FIX: Ensure the editor button uses the correct text from the cell
            button.setText((value == null) ? "" : value.toString()); 
            return button;
        }
    }
}