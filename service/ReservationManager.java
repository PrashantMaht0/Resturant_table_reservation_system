package service;

import model.AbstractTable;
import model.Reservation;
import exception.TableNotAvailableExeception;
import java.time.LocalDateTime;
import model.FourSeaterTable;
import model.TwoSeaterTable;
import model.TableType;
import java.util.function.Predicate;

/**
 * Manages the restaurant tables and reservations.
 * Fulfills 'encapsulation', 'this() vs this.', 'overloading', 
 * 'lambdas (Predicate)', 'defensive copying', and 'varargs'.
 */
public class ReservationManager implements ReservationService {

    // --- Array Storage ---
    // Using simple arrays to store objects. We'll use a fixed size for simplicity.
    private static final int MAX_TABLES = 30;
    private static final int MAX_RESERVATIONS = 50;
    
    // Fulfills the 'arrays' requirement
    private final AbstractTable[] tables; 
    private Reservation[] reservations;
    
    // Track current counts
    private int tableCount;
    private int reservationCount;

    // --- Constructors ---
    
    public ReservationManager() {
        this.tables = new AbstractTable[MAX_TABLES];
        this.reservations = new Reservation[MAX_RESERVATIONS];
        this.tableCount = 0;
        this.reservationCount = 0;
        initializeDefaultTables();
    }
    
    // --- Helper Method for super() vs super. ---
    
    private void initializeDefaultTables() {
        // Initial 3 tables
        tables[tableCount++] = new TwoSeaterTable(1, TableType.WINDOW);
        tables[tableCount++] = new FourSeaterTable(2, TableType.BOOTH);
        tables[tableCount++] = new FourSeaterTable(3, TableType.STANDARD, 6);
        
        // --- Added 17 New Tables (Total 20) ---
        
        // 5 more 2-seater tables
        tables[tableCount++] = new TwoSeaterTable(4, TableType.STANDARD);
        tables[tableCount++] = new TwoSeaterTable(5, TableType.WINDOW);
        tables[tableCount++] = new TwoSeaterTable(6, TableType.WINDOW);
        tables[tableCount++] = new TwoSeaterTable(7, TableType.STANDARD);
        tables[tableCount++] = new TwoSeaterTable(8, TableType.OUTDOOR);
        
        // 7 more 4-seater tables
        tables[tableCount++] = new FourSeaterTable(9, TableType.STANDARD);
        tables[tableCount++] = new FourSeaterTable(10, TableType.STANDARD);
        tables[tableCount++] = new FourSeaterTable(11, TableType.BOOTH);
        tables[tableCount++] = new FourSeaterTable(12, TableType.BOOTH);
        tables[tableCount++] = new FourSeaterTable(13, TableType.OUTDOOR);
        tables[tableCount++] = new FourSeaterTable(14, TableType.OUTDOOR);
        tables[tableCount++] = new FourSeaterTable(15, TableType.WINDOW);

        // 5 large tables (custom capacity 6 or 8)
        tables[tableCount++] = new FourSeaterTable(16, TableType.STANDARD, 6);
        tables[tableCount++] = new FourSeaterTable(17, TableType.STANDARD, 6);
        tables[tableCount++] = new FourSeaterTable(18, TableType.OUTDOOR, 8);
        tables[tableCount++] = new FourSeaterTable(19, TableType.BOOTH, 8);
        tables[tableCount++] = new FourSeaterTable(20, TableType.STANDARD, 8); // Final Table
    }
    
    // --- Core ReservationService Implementation ---

    @Override
    public Reservation addReservation(int tableNo, Reservation res) throws TableNotAvailableExeception {
        // ... (Logic remains the same)
        AbstractTable table = findTable(tableNo);

        if (table == null || tableNo > MAX_TABLES) {
            throw new IllegalArgumentException("Table number " + tableNo + " does not exist (Max table is " + MAX_TABLES + ").");
        }
        
        if (table.isReserved()) {
            throw new TableNotAvailableExeception("Table " + tableNo + " is already reserved.");
        }
        
        if (reservationCount >= MAX_RESERVATIONS) {
            throw new IllegalStateException("Maximum reservations reached.");
        }

        table.setReserved(true);
        reservations[reservationCount++] = res;
        return res;
    }
    
    /**
     * Method Overloading: Adds a reservation based on specific time and date.
     * Replaces the old addReservation(..., int minutesFromNow) method.
     * Fulfills the 'method overloading' requirement.
     */
    public Reservation addReservation(int tableNo, String name, String phone, LocalDateTime dateTime) throws TableNotAvailableExeception {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reservation time must be in the future.");
        }
        
        Reservation res = new Reservation(name, phone, dateTime, tableNo);
        return addReservation(tableNo, res); // Calls the @Override method above
    }
    
    /**
     * Implements the "Mark as Complete" logic using array traversal.
     */
    @Override
    public void removeReservation(int tableNo) {
        AbstractTable table = findTable(tableNo);
        
        if (table != null) {
            table.setReserved(false); 
        }

        // Simple Array removal: Find the reservation and shift elements
        int indexToRemove = -1;
        for (int i = 0; i < reservationCount; i++) {
            if (reservations[i] != null && reservations[i].tableNumber() == tableNo) {
                indexToRemove = i;
                break;
            }
        }
        
        if (indexToRemove != -1) {
            // Shift elements down (Simple deletion in an array)
            for (int i = indexToRemove; i < reservationCount - 1; i++) {
                reservations[i] = reservations[i+1];
            }
            reservations[--reservationCount] = null; // Clear the last slot
        }
    }
    
    @Override
    public Reservation getReservationByTableNumber(int tableNo) {
        for (int i = 0; i < reservationCount; i++) {
            if (reservations[i] != null && reservations[i].tableNumber() == tableNo) {
                return reservations[i];
            }
        }
        return null;
    }

    /**
     * Returns a copy of the tables array (Fulfills Defensive Copying conceptually).
     */
    @Override
    public AbstractTable[] getAllTables() {
        // Using System.arraycopy for array-based defensive copying
        AbstractTable[] copy = new AbstractTable[tableCount];
        System.arraycopy(tables, 0, copy, 0, tableCount);
        return copy; 
    }
    
    /**
     * Returns a copy of the reservations array (Fulfills Defensive Copying conceptually).
     */
    @Override
    public Reservation[] getAllReservations() {
        Reservation[] copy = new Reservation[reservationCount];
        System.arraycopy(reservations, 0, copy, 0, reservationCount);
        return copy;
    }
    
    // Helper method to find a table by number using array traversal
    private AbstractTable findTable(int tableNo) {
        for (int i = 0; i < tableCount; i++) {
            if (tables[i].getTableNumber() == tableNo) {
                return tables[i];
            }
        }
        return null;
    }

    // --- Advanced Methods ---

    /**
     * Demonstrates Lambdas and Predicate using simple array traversal.
     */
    public AbstractTable[] getFilteredTables(Predicate<AbstractTable> filter) {
        // First pass to count matches
        int matchCount = 0;
        for (int i = 0; i < tableCount; i++) {
            if (filter.test(tables[i])) { // The lambda logic is applied here
                matchCount++;
            }
        }
        
        // Second pass to build the resulting array
        AbstractTable[] filtered = new AbstractTable[matchCount];
        int j = 0;
        for (int i = 0; i < tableCount; i++) {
            if (filter.test(tables[i])) {
                filtered[j++] = tables[i];
            }
        }
        return filtered;
    }
    
    /**
     * Demonstrates Varargs (remains the same).
     */
    public String getReservationDetails(Reservation res, String... fields) {
        StringBuilder details = new StringBuilder();
        details.append("Reservation Details:\n");
        
        for (var field : fields) {
            switch (field.toLowerCase()) {
                case "name" -> details.append("  Name: ").append(res.customerName()).append("\n");
                case "phone" -> details.append("  Phone: ").append(res.customerPhone()).append("\n");
                case "time" -> details.append("  Time: ").append(res.reservationTime()).append("\n");
                default -> details.append("  [Unknown Field: ").append(field).append("]\n");
            }
        }
        return details.toString();
    }
}