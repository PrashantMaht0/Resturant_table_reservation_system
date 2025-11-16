package ui;
/*
 * This class provides the main GUI screen for the restaurant table reservation system.
 */
import service.ReservationManager;

import javax.swing.*;
import java.awt.*;

public class MainScreenGUI extends JFrame {

    private final ReservationManager manager;
    // Constructor to initialize the main screen GUI
    public MainScreenGUI(ReservationManager manager) {
        
        super("The Spice India - Table Reservation System");
        this.manager = manager;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700); 
        
        setupLayout(); 
        setLocationRelativeTo(null);
    }
    // Method to set up the layout of the main screen
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        
        JPanel topContainerPanel = new JPanel();
        topContainerPanel.setLayout(new BoxLayout(topContainerPanel, BoxLayout.Y_AXIS)); // Stack title and buttons vertically
        topContainerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("The Spice India");
        Font titleFont = new Font("SansSerif", Font.BOLD, 50);
        titleLabel.setFont(titleFont);
        
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        
        topContainerPanel.add(titleLabel);
        topContainerPanel.add(Box.createVerticalStrut(20)); 

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10)); 
        
        JButton viewButton = new JButton("1: View Reservations");
        viewButton.addActionListener(e -> new ViewTablesGUI(manager).setVisible(true));

        JButton addButton = new JButton("2: Add Reservation");
        addButton.addActionListener(e -> new AddReservationDialog(this, manager).setVisible(true));

        JButton editButton = new JButton("3: Edit Reservation");
        editButton.addActionListener(e -> showEditReservationDialog());

        JButton removeButton = new JButton("4: Remove Reservation");
        removeButton.addActionListener(e -> showRemoveReservationDialog());

        buttonPanel.add(viewButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
        
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        topContainerPanel.add(buttonPanel);
        
        add(topContainerPanel, BorderLayout.NORTH);
        
        add(new JLabel("  Select an action from the menu."), BorderLayout.SOUTH);
    }
    
   // Show dialog to edit an existing reservation
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
    // Show dialog to remove an existing reservation
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