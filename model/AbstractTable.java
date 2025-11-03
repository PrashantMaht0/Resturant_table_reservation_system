package model;

public abstract class AbstractTable {

    // Protected (used by subclasses) and final for encapsulation
    protected final int tableNumber;
    protected final TableType type;
    private boolean isReserved;

    public AbstractTable(int tableNumber, TableType type) {
        // Demonstrates 'this.' to differentiate field and parameter
        this.tableNumber = tableNumber;
        this.type = type;
        this.isReserved = false;
    }

    // Abstract method to be implemented by concrete classes (Polymorphism)
    public abstract int getCapacity();

    // Getter methods
    public int getTableNumber() {
        return tableNumber;
    }

    public TableType getType() {
        return type;
    }

    public boolean isReserved() {
        return isReserved;
    }

    // Setter for status change (used when a reservation is added or completed)
    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }
    
    // Demonstrates LVTI (Local Variable Type Inference) 'var'
    public String getTableInfo() {
        var status = isReserved ? "RESERVED" : "NOT RESERVED";
        return String.format("Table %d (%s) - Capacity: %d, Status: %s",
                tableNumber, type, getCapacity(), status);
    }
}