package ui;

import service.ReservationManager;

import javax.swing.*;
import java.awt.*;

/**
 * The main screen GUI acting as the central menu with a prominent title.
 */
public class MainScreenGUI extends JFrame {

    private final ReservationManager manager;

    public MainScreenGUI(ReservationManager manager) {
        // The title is now set within the frame's content, not the frame title bar.
        super("The Spice India - Table Reservation System");
        this.manager = manager;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700); // Slightly larger frame to accommodate the title
        
        setupLayout(); 
        setLocationRelativeTo(null);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // --- Top Panel: Title and Buttons ---
        
        JPanel topContainerPanel = new JPanel();
        topContainerPanel.setLayout(new BoxLayout(topContainerPanel, BoxLayout.Y_AXIS)); // Stack title and buttons vertically
        topContainerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Big Title: "The Spice India"
        JLabel titleLabel = new JLabel("The Spice India");
        // Create a large, bold font for the title
        Font titleFont = new Font("SansSerif", Font.BOLD, 50);
        titleLabel.setFont(titleFont);
        
        // Center the title label
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        
        topContainerPanel.add(titleLabel);
        topContainerPanel.add(Box.createVerticalStrut(20)); // Add space between title and buttons

        // 2. Menu Buttons Container
        JPanel buttonPanel = new JPanel();
        // Use a vertical layout for clear buttons
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10)); 
        
        // Button 1: View
        JButton viewButton = new JButton("1: View Reservations");
        viewButton.addActionListener(e -> new ViewTablesGUI(manager).setVisible(true));

        // Button 2: Add Reservation
        JButton addButton = new JButton("2: Add Reservation");
        addButton.addActionListener(e -> new AddReservationDialog(this, manager).setVisible(true));

        // Button 3: Edit Reservation
        JButton editButton = new JButton("3: Edit Reservation");
        editButton.addActionListener(e -> showEditReservationDialog());

        // Button 4: Remove Reservation
        JButton removeButton = new JButton("4: Remove Reservation");
        removeButton.addActionListener(e -> showRemoveReservationDialog());

        buttonPanel.add(viewButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
        
        // Center the button panel container
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        topContainerPanel.add(buttonPanel);
        
        add(topContainerPanel, BorderLayout.NORTH);
        
        // South: Status Bar
        add(new JLabel("  Select an action from the menu."), BorderLayout.SOUTH);
    }
    
    // --- Helper Dialog Methods (remain the same) ---
    // (Ensure you include the showEditReservationDialog and showRemoveReservationDialog methods here)

    private void showEditReservationDialog() {
        String tableNoStr = JOptionPane.showInputDialog(this, "Enter Table Number to Edit:", "Edit Reservation", JOptionPane.QUESTION_MESSAGE);
        
        if (tableNoStr != null) {
            try {
                int tableNo = Integer.parseInt(tableNoStr.trim());
                
                if (manager.getReservationByTableNumber(tableNo) == null) {
                    JOptionPane.showMessageDialog(this, "Table " + tableNo + " has no active reservation to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    new EditReservationDialog(this, manager, tableNo).setVisible(true);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showRemoveReservationDialog() {
        String tableNoStr = JOptionPane.showInputDialog(this, "Enter Table Number to Remove Reservation:", "Remove Reservation", JOptionPane.QUESTION_MESSAGE);
        
        if (tableNoStr != null) {
            try {
                int tableNo = Integer.parseInt(tableNoStr.trim());
                
                if (manager.getReservationByTableNumber(tableNo) == null) {
                    JOptionPane.showMessageDialog(this, "Table " + tableNo + " has no active reservation to remove.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    manager.removeReservation(tableNo);
                    JOptionPane.showMessageDialog(this, "Reservation for Table " + tableNo + " successfully removed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}