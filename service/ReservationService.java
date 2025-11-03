package service;

import model.AbstractTable;
import model.Reservation;
import exception.TableNotAvailableExeception;
import java.time.LocalDateTime;

public interface ReservationService {
Reservation addReservation(int tableNo, Reservation res) throws TableNotAvailableExeception;
    
    Reservation getReservationByTableNumber(int tableNo);
    
    void removeReservation(int tableNo);
    
    AbstractTable[] getAllTables();
    
    Reservation[] getAllReservations(); 
    
    static LocalDateTime calculateFutureTime(int minutesFromNow) {
        if (minutesFromNow <= 0) {
            return LocalDateTime.now();
        }
        return LocalDateTime.now().plusMinutes(minutesFromNow);
    }
}