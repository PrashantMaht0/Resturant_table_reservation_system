package ui;
/*
 * This class provides the GUI to view the status of all tables in the restaurant.
 */
import model.AbstractTable;
import model.Reservation;
import service.ReservationManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;


public class ViewTablesGUI extends JFrame {

    private final ReservationManager manager;
    private JTable reservationTable;
    private DefaultTableModel tableModel;
    // Column names for the table
    private static final String[] COLUMN_NAMES = {"Table No", "Type", "Capacity", "Status", "Action"};
    // Constructor to initialize the GUI
    public ViewTablesGUI(ReservationManager manager) {
        super("Table Status - The Spice India");
        this.manager = manager;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setSize(700, 450);
        
        setupTable();
        setupLayout();
        updateReservationTable();
        setLocationRelativeTo(null);
    }
    // Method to set up the reservation table
    private void setupTable() {
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        reservationTable = new JTable(tableModel);
        
        reservationTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        reservationTable.getColumn("Action").setCellEditor(new ButtonEditor(new JTextField(), this));
    }
    // Method to set up the layout of the GUI
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JButton refreshButton = new JButton("Refresh Status");
        refreshButton.addActionListener(e -> updateReservationTable());
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(refreshButton);
        add(topPanel, BorderLayout.NORTH);

        add(new JScrollPane(reservationTable), BorderLayout.CENTER);
    }
    
    // Method to update the reservation table with current data
    public void updateReservationTable() {
        tableModel.setRowCount(0);

        for (AbstractTable table : manager.getAllTables()) {
            if (table != null) {
                Vector<Object> row = new Vector<>();
                row.add(table.getTableNumber());
                row.add(table.getType());
                row.add(table.getCapacity());
                row.add(table.isReserved() ? "RESERVED" : "NOT RESERVED");
                row.add(table.isReserved() ? "View/Complete" : "Book"); 
                tableModel.addRow(row);
            }
        }
        tableModel.fireTableDataChanged();
    }
    public void showReservationDetails(int tableNo) {
    Reservation reservation = manager.getReservationByTableNumber(tableNo);
    
    if (reservation == null) {
        int result = JOptionPane.showConfirmDialog(this, 
            "Table " + tableNo + " is NOT RESERVED. Would you like to book it now?", 
            "Book Table", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
             JOptionPane.showMessageDialog(this, "Please use the 'Add Reservation' button on the main menu.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
        return;
    }

    // --- Logic for Reserved Table (View/Complete) ---
    LocalDateTime dateTime = reservation.reservationTime();
    
    // Define formatters for clean output
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy");
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");

    // Construct the details string with separate lines for date and time
    String details = String.format(
        "Reservation Details for Table %d:\n\n" +
        "Customer: %s\n" +
        "Phone: %s\n\n" +
        "Date: %s\n" + 
        "Time: %s",   
        tableNo,
        reservation.customerName(),
        reservation.customerPhone(),
        dateTime.format(dateFormat),
        dateTime.format(timeFormat)
    );

    // Show options to mark as complete or just close
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
        updateReservationTable(); 
    }
}
    // Custom renderer for the button in the table
    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() { setOpaque(true); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int tableRow;
        private ViewTablesGUI parentFrame; 

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
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.tableRow = row;
            button.setText((value == null) ? "" : value.toString()); 
            return button;
        }
    }
}