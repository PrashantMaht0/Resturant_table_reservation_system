package service;

import model.AbstractTable;
import model.Reservation;
import exception.TableNotAvailableExeception;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Defines the contract for table reservation management.
 * Fulfills 'interfaces', 'default', and 'static interface methods' requirements.
 */
public interface ReservationService {

    // Core methods to be implemented by ReservationManager
    Reservation addReservation(int tableNo, Reservation res) throws TableNotAvailableExeception;
    
    Reservation getReservationByTableNumber(int tableNo);
    
    void removeReservation(int tableNo);
    
    List<Reservation> getAllReservations();
    
    List<AbstractTable> getAvailableTables();

    // --- Advanced Interface Methods (Java 8+) ---
    
    /**
     * Default method to check table status without needing manager access.
     * Demonstrates a 'default' interface method.
     */
    default boolean isReservationPossible(int tableNo, LocalDateTime time) {
        // A simple check that can be done by external classes using the interface instance
        return true; 
    }

    /**
     * Static method to calculate a future reservation time.
     * Demonstrates a 'static' interface method.
     */
    static LocalDateTime calculateFutureTime(int minutesFromNow) {
        if (minutesFromNow <= 0) {
            return LocalDateTime.now();
        }
        return LocalDateTime.now().plusMinutes(minutesFromNow);
    }
}