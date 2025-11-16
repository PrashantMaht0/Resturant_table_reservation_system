package model;
/*
 * This interface represents a table in the restaurant.
 */
public sealed interface Table 
    permits TwoSeaterTable, FourSeaterTable {
}
