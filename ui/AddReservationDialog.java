package ui;

import exception.TableNotAvailableExeception;
import service.ReservationManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;

public class AddReservationDialog extends JDialog {
    
    private final ReservationManager manager;
    private final MainScreenGUI parentGUI;

    private JTextField tableNoField, nameField, phoneField;
    private JTextField dateField, timeField; 
    
    private JButton addButton;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public AddReservationDialog(MainScreenGUI parent, ReservationManager manager) {
        super(parent, "Add Reservation", true);
        this.manager = manager;
        this.parentGUI = parent;
        
        setupUI();
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        tableNoField = new JTextField(5);
        nameField = new JTextField(15);
        phoneField = new JTextField(15);
        
        dateField = new JTextField(LocalDate.now().format(DATE_FORMATTER), 10); 
        timeField = new JTextField("19:00", 5); 

        mainPanel.add(new JLabel("Table No:"));
        mainPanel.add(tableNoField);
        mainPanel.add(new JLabel("Customer Name:"));
        mainPanel.add(nameField);
        mainPanel.add(new JLabel("Phone:"));
        mainPanel.add(phoneField);
        
        mainPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        mainPanel.add(dateField);
        mainPanel.add(new JLabel("Time (HH:MM 24h):"));
        mainPanel.add(timeField);

        addButton = new JButton("Book Table");
        addButton.addActionListener(e -> attemptAddReservation());
        
        mainPanel.add(new JLabel());
        mainPanel.add(addButton);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void attemptAddReservation() {
        try {
            int tableNo = Integer.parseInt(tableNoField.getText().trim());
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            
            LocalDate date = LocalDate.parse(dateField.getText().trim(), DATE_FORMATTER);
            LocalTime time = LocalTime.parse(timeField.getText().trim(), TIME_FORMATTER);
            LocalDateTime dateTime = LocalDateTime.of(date, time);

            manager.addReservation(tableNo, name, phone, dateTime);

            JOptionPane.showMessageDialog(this, 
                "Booking successful for Table " + tableNo + " at " + dateTime.format(DATE_FORMATTER) + " " + dateTime.format(TIME_FORMATTER) + ".", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            dispose(); 

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid table number.", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date/time format. Use YYYY-MM-DD and HH:MM.", 
                "Date/Time Error", JOptionPane.ERROR_MESSAGE);
        } catch (TableNotAvailableExeception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                "Booking Failed", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}