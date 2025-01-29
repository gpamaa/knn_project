package data;

import java.io.Serializable;

/**
 * classe che rappresenta l intestazione di una colonna
 */
abstract public class Attribute implements Serializable {
    private String name;
    private int index;

    /**
     * costruttore della classe
     * inizializza i membri name ed index
     * 
     * @param name  rappresenta il nome della colonna
     * 
     * @param index rappresenta l indice della colonna
     */
    public Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * restituisce il nome della colonna
     * 
     * @return il nome della colonna
     */
    public String getName() {
        return this.name;
    }

    /**
     * permette di mostrare a video lo stato di una istanza della classe Attribute
     * 
     * @return la stringa che rappresenta che lo stato dell'istanza
     */
    public String toString() {
        return "nome " + name + " " + "index " + index;
    }

    /**
     * restituisce l'indice della colonna
     * 
     * @return il valore dell indice della colonna
     */
    public int getIndex() {
        return this.index;
    }
}