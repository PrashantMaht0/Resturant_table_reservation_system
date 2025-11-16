package model;
/*
 * This record represents a reservation made by a customer for a specific table at a given time.
 */
import java.time.LocalDateTime;

public record Reservation(
        String customerName,
        String customerPhone,
        LocalDateTime reservationTime,
        int tableNumber) {

    public Reservation {
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be empty.");
        }
    }
        
}