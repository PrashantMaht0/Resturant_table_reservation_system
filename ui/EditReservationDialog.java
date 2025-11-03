package ui;

import model.Reservation;
import service.ReservationManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Dialog window for editing an existing table reservation, capturing specific date and time.
 */
public class EditReservationDialog extends JDialog {
    
    private final ReservationManager manager;
    private final MainScreenGUI parentGUI;
    private final int tableNumberToEdit;
    private final Reservation originalReservation;

    // UI Components: Fields for specific Date and Time input
    private JTextField nameField, phoneField, dateField, timeField;
    private JButton saveButton;

    // Define standard formats for parsing and displaying
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public EditReservationDialog(MainScreenGUI parent, ReservationManager manager, int tableNo) {
        super(parent, "Edit Reservation for Table " + tableNo, true);
        this.manager = manager;
        this.parentGUI = parent;
        this.tableNumberToEdit = tableNo;

        this.originalReservation = manager.getReservationByTableNumber(tableNo);

        if (this.originalReservation == null) {
            JOptionPane.showMessageDialog(parent, "No active reservation found for Table " + tableNo + ".", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        setupUI();
        prePopulateFields();
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void setupUI() {
        // 5 rows for inputs + labels
        JPanel mainPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        nameField = new JTextField(15);
        phoneField = new JTextField(15);
        dateField = new JTextField(10); 
        timeField = new JTextField(5); 

        mainPanel.add(new JLabel("Customer Name:"));
        mainPanel.add(nameField);
        mainPanel.add(new JLabel("Phone:"));
        mainPanel.add(phoneField);
        
        // New Date and Time fields
        mainPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        mainPanel.add(dateField);
        mainPanel.add(new JLabel("Time (HH:MM 24h):"));
        mainPanel.add(timeField);

        saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> attemptSaveReservation());
        
        mainPanel.add(new JLabel()); // Spacer
        mainPanel.add(saveButton);
        
        add(mainPanel, BorderLayout.CENTER);
    }

    private void prePopulateFields() {
        // Format the existing LocalDateTime for the text fields
        LocalDateTime originalTime = originalReservation.reservationTime();

        nameField.setText(originalReservation.customerName());
        phoneField.setText(originalReservation.customerPhone());
        
        // Populate fields with the current reservation date and time
        dateField.setText(originalTime.format(DATE_FORMATTER));
        timeField.setText(originalTime.format(TIME_FORMATTER));
    }
    
    private void attemptSaveReservation() {
        try {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            
            // --- New Logic: Parse Date and Time to create LocalDateTime ---
            LocalDate date = LocalDate.parse(dateField.getText().trim(), DATE_FORMATTER);
            LocalTime time = LocalTime.parse(timeField.getText().trim(), TIME_FORMATTER);
            LocalDateTime newDateTime = LocalDateTime.of(date, time);

            // 1. Remove the old reservation first
            manager.removeReservation(tableNumberToEdit); 
            
            // 2. Create the new, updated reservation object using the new manager signature
            // This will call the public Reservation addReservation(int tableNo, String name, String phone, LocalDateTime dateTime)
            manager.addReservation(tableNumberToEdit, name, phone, newDateTime);

            JOptionPane.showMessageDialog(this, 
                "Reservation for Table " + tableNumberToEdit + " updated successfully to " + newDateTime.format(DATE_FORMATTER) + " " + newDateTime.format(TIME_FORMATTER) + ".", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // The table view will be refreshed when the user views the table statuses again.
            dispose(); 

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please ensure the table number is valid.", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date/time format. Use YYYY-MM-DD and HH:MM.", 
                "Date/Time Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Catches TableNotAvailableException (if someone somehow books it between remove and add) 
            // and IllegalArgumentException (e.g., time in the past).
            JOptionPane.showMessageDialog(this, "Failed to update reservation: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}