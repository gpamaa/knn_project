package data;

import java.io.Serializable;

/**
 * classe utilizzata per definire un intestazione della colonna con valori di
 * tipo numerico
 */
public class ContinuousAttribute extends Attribute implements Serializable {
    private double min = Double.MAX_VALUE;
    private double max = Double.MIN_VALUE;

    /**
     * costruttore di classe
     * 
     * @param name
     *              rappresenta il nome della colonna
     * 
     * @param index
     *              rappresenta l indice della colonna
     */
    public ContinuousAttribute(String name, int index) {
        super(name, index);
    }

    /**
     * utilizzata per impostare il valore più piccolo tra due dobule
     * 
     * @param min
     *            valore passato come parametro per il confronto
     */
    public void setmin(double min) {
        if (this.min > min)
            this.min = min;
    }

    /**
     * utilizzata per impostare il valore più grande tra due double
     * 
     * @param max
     *            valore passato come parametro per il confronto
     */
    public void setmax(double max) {
        if (this.max < max) {
            this.max = max;
        }
    }

    /**
     * usato per trasformare il training set e il query point in modo che tutte le
     * variabili indipendenti abbiano lo stesso range di valori
     * 
     * @param value
     *              variabile indipendente da scalare
     * @return double
     *         restituisce il valore scalato
     */
    public double scale(Double value) {
        return (value - min) / (max - min);
    }
}