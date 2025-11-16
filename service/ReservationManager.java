package service;
/*
 * This class defines the operations for managing reservations and tables in the restaurant.
 */
import model.AbstractTable;
import model.Reservation;
import exception.TableNotAvailableExeception;
import java.time.LocalDateTime;
import model.FourSeaterTable;
import model.TwoSeaterTable;
import model.TableType;
import java.util.function.Predicate;

public class ReservationManager implements ReservationService {

    // --- Array Storage ---
    private static final int MAX_TABLES = 30;
    private static final int MAX_RESERVATIONS = 50;
    
    private final AbstractTable[] tables; 
    private Reservation[] reservations;
    
    private int tableCount;
    private int reservationCount;

    // Constructor initializes tables and reservations
    public ReservationManager() {
        this.tables = new AbstractTable[MAX_TABLES];
        this.reservations = new Reservation[MAX_RESERVATIONS];
        this.tableCount = 0;
        this.reservationCount = 0;
        initializeDefaultTables();
    }
    
    // Initialize some default tables for the restaurant
    private void initializeDefaultTables() {
        tables[tableCount++] = new TwoSeaterTable(1, TableType.WINDOW);
        tables[tableCount++] = new FourSeaterTable(2, TableType.BOOTH);
        tables[tableCount++] = new FourSeaterTable(3, TableType.STANDARD, 6);
        tables[tableCount++] = new TwoSeaterTable(4, TableType.STANDARD);
        tables[tableCount++] = new TwoSeaterTable(5, TableType.WINDOW);
        tables[tableCount++] = new TwoSeaterTable(6, TableType.WINDOW);
        tables[tableCount++] = new TwoSeaterTable(7, TableType.STANDARD);
        tables[tableCount++] = new TwoSeaterTable(8, TableType.OUTDOOR);
        tables[tableCount++] = new FourSeaterTable(9, TableType.STANDARD);
        tables[tableCount++] = new FourSeaterTable(10, TableType.STANDARD);
        tables[tableCount++] = new FourSeaterTable(11, TableType.BOOTH);
        tables[tableCount++] = new FourSeaterTable(12, TableType.BOOTH);
        tables[tableCount++] = new FourSeaterTable(13, TableType.OUTDOOR);
        tables[tableCount++] = new FourSeaterTable(14, TableType.OUTDOOR);
        tables[tableCount++] = new FourSeaterTable(15, TableType.WINDOW);
        tables[tableCount++] = new FourSeaterTable(16, TableType.STANDARD, 6);
        tables[tableCount++] = new FourSeaterTable(17, TableType.STANDARD, 6);
        tables[tableCount++] = new FourSeaterTable(18, TableType.OUTDOOR, 8);
        tables[tableCount++] = new FourSeaterTable(19, TableType.BOOTH, 8);
        tables[tableCount++] = new FourSeaterTable(20, TableType.STANDARD, 8); // Final Table
    }
    
    // Implementation of ReservationService methods
    @Override
    public Reservation addReservation(int tableNo, Reservation res) throws TableNotAvailableExeception {
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
    // Overloaded method to create and add a reservation
    public Reservation addReservation(int tableNo, String name, String phone, LocalDateTime dateTime) throws TableNotAvailableExeception {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reservation time must be in the future.");
        }
        
        Reservation res = new Reservation(name, phone, dateTime, tableNo);
        return addReservation(tableNo, res); // Calls the @Override method above
    }
    
    //mark reservation as complete and free up the table
    @Override
    public void removeReservation(int tableNo) {
        AbstractTable table = findTable(tableNo);
        
        if (table != null) {
            table.setReserved(false); 
        }

        int indexToRemove = -1;
        for (int i = 0; i < reservationCount; i++) {
            if (reservations[i] != null && reservations[i].tableNumber() == tableNo) {
                indexToRemove = i;
                break;
            }
        }
        
        if (indexToRemove != -1) {
            for (int i = indexToRemove; i < reservationCount - 1; i++) {
                reservations[i] = reservations[i+1];
            }
            reservations[--reservationCount] = null;
        }
    }
    // method to get reservation by table number
    @Override
    public Reservation getReservationByTableNumber(int tableNo) {
        for (int i = 0; i < reservationCount; i++) {
            if (reservations[i] != null && reservations[i].tableNumber() == tableNo) {
                return reservations[i];
            }
        }
        return null;
    }
    // method to get all tables
    @Override
    public AbstractTable[] getAllTables() {
        AbstractTable[] copy = new AbstractTable[tableCount];
        System.arraycopy(tables, 0, copy, 0, tableCount);
        return copy; 
    }
    // method to get all reservations 
    @Override
    public Reservation[] getAllReservations() {
        Reservation[] copy = new Reservation[reservationCount];
        System.arraycopy(reservations, 0, copy, 0, reservationCount);
        return copy;
    }
    // Helper method to find a table by its number
    private AbstractTable findTable(int tableNo) {
        for (int i = 0; i < tableCount; i++) {
            if (tables[i].getTableNumber() == tableNo) {
                return tables[i];
            }
        }
        return null;
    }
    // Method to get filtered tables based on a predicate
    public AbstractTable[] getFilteredTables(Predicate<AbstractTable> filter) {
        int matchCount = 0;
        for (int i = 0; i < tableCount; i++) {
            if (filter.test(tables[i])) { 
                matchCount++;
            }
        }
        
        AbstractTable[] filtered = new AbstractTable[matchCount];
        int j = 0;
        for (int i = 0; i < tableCount; i++) {
            if (filter.test(tables[i])) {
                filtered[j++] = tables[i];
            }
        }
        return filtered;
    }
    // Method to get reservation details as a formatted string to be displayed on table 
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