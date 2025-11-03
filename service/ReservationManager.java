package service;

import model.AbstractTable;
import model.Reservation;
import exception.TableNotAvailableExeception;
import java.time.LocalDateTime;
import java.util.List;
import model.FourSeaterTable;
import model.TwoSeaterTable;
import model.TableType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Manages the restaurant tables and reservations.
 * Fulfills 'encapsulation', 'this() vs this.', 'overloading', 
 * 'lambdas (Predicate)', 'defensive copying', and 'varargs'.
 */
public class ReservationManager implements ReservationService {

    // --- Encapsulation ---
    // All core data is private and final.
    private final Map<Integer, AbstractTable> tables;
    private final List<Reservation> reservations;
    private final String SYSTEM_VERSION = "v1.0.0"; // Final constant for demonstration

    // --- Constructors: this() vs this. ---
    
    /**
     * Default constructor: initializes tables and reservations.
     */
    public ReservationManager() {
        // Calls the primary constructor (demonstrates this())
        this(new HashMap<>());
        // Initialize default tables after calling the primary constructor
        initializeDefaultTables();
    }
    
    /**
     * Primary constructor: uses passed-in tables.
     * Demonstrates 'this.' to access instance fields.
     */
    public ReservationManager(Map<Integer, AbstractTable> initialTables) {
        this.tables = initialTables; // Demonstrates this.
        this.reservations = new ArrayList<>();
    }
    
    // --- Helper Method for super() vs super. ---
    
    /**
     * Initializes a default set of tables for the restaurant.
     */
    private void initializeDefaultTables() {
        // Adding tables using the base type AbstractTable (Polymorphism)
        tables.put(1, new TwoSeaterTable(1, TableType.WINDOW));
        tables.put(2, new FourSeaterTable(2, TableType.BOOTH));
        tables.put(3, new FourSeaterTable(3, TableType.STANDARD, 6)); // Overloaded constructor
    }
    
    // --- Core ReservationService Implementation ---

    /**
     * Adds a reservation based on a Reservation object.
     * Fulfills 'checked exception' requirement.
     */
    @Override
    public Reservation addReservation(int tableNo, Reservation res) throws TableNotAvailableExeception {
        AbstractTable table = tables.get(tableNo);

        if (table == null) {
            throw new IllegalArgumentException("Table number " + tableNo + " does not exist.");
        }

        // Check from flow diagram: "check if entered table no already has a reservation"
        if (table.isReserved()) {
            throw new TableNotAvailableExeception("Table " + tableNo + " is already reserved.");
        }

        table.setReserved(true);
        reservations.add(res);
        return res;
    }
    
    /**
     * Method Overloading: Adds a reservation calculated from minutes from now.
     * Fulfills the 'method overloading' requirement.
     */
    public Reservation addReservation(int tableNo, String name, String phone, int minutesFromNow) throws TableNotAvailableExeception {
        // Uses static interface method for time calculation
        LocalDateTime time = ReservationService.calculateFutureTime(minutesFromNow);
        
        Reservation res = new Reservation(name, phone, time, tableNo);
        return addReservation(tableNo, res);
    }
    
    /**
     * Implements the "Mark as Complete" logic from the flow diagram.
     * Completes the reservation and changes the table status.
     */
    @Override
    public void removeReservation(int tableNo) {
        AbstractTable table = tables.get(tableNo);
        
        // Find and remove reservation
        reservations.removeIf(res -> res.tableNumber() == tableNo);

        // Change table status to "not reserved"
        if (table != null) {
            table.setReserved(false); 
        }
    }
    
    @Override
    public Reservation getReservationByTableNumber(int tableNo) {
        // Uses a stream and predicate to find the reservation
        return reservations.stream()
                .filter(res -> res.tableNumber() == tableNo)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<AbstractTable> getAvailableTables() {
        // Demonstrates Lambdas (Predicate) for simple filtering
        return tables.values().stream()
                .filter(table -> !table.isReserved())
                .collect(Collectors.toList());
    }
    
    /**
     * Fulfills the 'defensive copying' requirement.
     * Returns a copy of the internal reservation list.
     */
    @Override
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(this.reservations);
    }
    
    // --- Advanced Methods ---

    /**
     * Demonstrates Lambdas and Predicate for advanced filtering.
     * Fulfills 'lambdas (Predicate)' requirement.
     */
    public List<AbstractTable> getFilteredTables(Predicate<AbstractTable> filter) {
        // Uses the provided Predicate lambda to filter the table list
        return tables.values().stream()
                .filter(filter)
                .collect(Collectors.toList());
    }
    
    /**
     * Demonstrates Varargs for flexibility in displaying details.
     * Fulfills the 'varargs' requirement.
     */
    public String getReservationDetails(Reservation res, String... fields) {
        StringBuilder details = new StringBuilder();
        details.append("Reservation Details:\n");
        
        for (var field : fields) { // LVTI 'var' used here
            switch (field.toLowerCase()) { // Fulfills 'switch expressions' (modern compiler supports this)
                case "name" -> details.append("  Name: ").append(res.customerName()).append("\n");
                case "phone" -> details.append("  Phone: ").append(res.customerPhone()).append("\n");
                case "time" -> details.append("  Time: ").append(res.reservationTime()).append("\n");
                default -> details.append("  [Unknown Field: ").append(field).append("]\n");
            }
        }
        return details.toString();
    }
}