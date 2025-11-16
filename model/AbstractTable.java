package model;
/*
 * This abstract class provides a base implementation for different types of tables in the restaurant.
 */
public abstract class AbstractTable {

    protected final int tableNumber;
    protected final TableType type;
    private boolean isReserved;

    public AbstractTable(int tableNumber, TableType type) {
        this.tableNumber = tableNumber;
        this.type = type;
        this.isReserved = false;
    }

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

    // Setter method
    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }
    
    //get table information as string for display in the table list
    public String getTableInfo() {
        var status = isReserved ? "RESERVED" : "NOT RESERVED";
        return String.format("Table %d (%s) - Capacity: %d, Status: %s",
                tableNumber, type, getCapacity(), status);
    }
}