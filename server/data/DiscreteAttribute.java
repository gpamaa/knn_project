package data;

import java.io.Serializable;

/**
 * classe utilizzata per definire campi della tabella di tipo stringa
 */
public class DiscreteAttribute extends Attribute implements Serializable {
    /**
     * costruttore di classe,inizializza i membri name ed index
     * 
     * @param name
     *              definisce il nome della colonna
     * @param index
     *              definisce index della colonna
     */
    public DiscreteAttribute(String name, int index) {
        super(name, index);
    }
}