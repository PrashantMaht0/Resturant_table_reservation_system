package model;
/*
 * This class represents a four-seater table in the restaurant.
 */
public final class FourSeaterTable extends AbstractTable implements Table {

    // Capacity can be flexible for larger tables, but defaults to 4
    private int customCapacity;

    public FourSeaterTable(int tableNumber, TableType type) {
        this(tableNumber, type, 4); 
    }
    
    public FourSeaterTable(int tableNumber, TableType type, int customCapacity) {
        super(tableNumber, type);
        this.customCapacity = customCapacity;
    }

    
    @Override
    public int getCapacity() {
        return customCapacity;
    }
}