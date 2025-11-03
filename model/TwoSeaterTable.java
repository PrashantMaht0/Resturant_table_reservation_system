package model;

public final class TwoSeaterTable extends AbstractTable implements Table {
    
    private static final int CAPACITY = 2;

    public TwoSeaterTable(int tableNumber, TableType type) {
        super(tableNumber, type);
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }
}