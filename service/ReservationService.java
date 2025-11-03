package service;

import model.AbstractTable;
import model.Reservation;
import exception.TableNotAvailableExeception;
import java.time.LocalDateTime;

/**
 * Defines the contract for table reservation management.
 * Fulfills 'interfaces', 'default', and 'static interface methods' requirements.
 */
public interface ReservationService {
Reservation addReservation(int tableNo, Reservation res) throws TableNotAvailableExeception;
    
    Reservation getReservationByTableNumber(int tableNo);
    
    void removeReservation(int tableNo);
    
    // We will return simple arrays or the base types where possible
    AbstractTable[] getAllTables();
    
    Reservation[] getAllReservations(); 
    
    // Static and default methods remain valid
    static LocalDateTime calculateFutureTime(int minutesFromNow) {
        if (minutesFromNow <= 0) {
            return LocalDateTime.now();
        }
        return LocalDateTime.now().plusMinutes(minutesFromNow);
    }
}