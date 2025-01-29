package example;

import java.util.ArrayList;
import java.util.Iterator;

import data.TrainingDataException;
import mining.KNN;
import utility.Keyboard;
import data.Data;

import java.io.FileNotFoundException;
import java.io.Serializable;

/**
 * rappresenta una riga del trainingset con le colonne indipendenti
 */

public class Example implements Serializable {
    private ArrayList example;

    /**
     * costruttore di classe,crea un oggetto example con dimensione size
     * 
     * @param size
     *             rappresenta la dimensione dell'example
     */
    public Example(int size) {
        example = new ArrayList<Object>(size);
    }

    /**
     * aggiunge all'arraylist di Example un nuovo oggetto nella posizione
     * specificata
     * 
     * @param o
     *              oggetto da aggiungere all'arrayList
     * @param index
     *              posizione nella quale aggiungere il nuovo oggetto dell'example
     */
    public void set(Object o, int index) {
        example.add(index, o);
    }

    /**
     * restituisce l'oggetto dell'arraylist di Example nella posizione specificata
     * 
     * @param index
     *              posizione specifica dell'arraylist
     * @return Object
     *         oggetto dell'Example da restituire
     */
    public Object get(int index) {
        return example.get(index);
    }

    /**
     * scambia un Example passato come parametro alla funzione con l'Example
     * corrente
     * 
     * @param e
     *          Example passato come parametro da scambiare con l'Example corrente
     */
    public void swap(Example e) {
        ArrayList appoggio = new ArrayList(this.example.size());
        if (e.example.size() == this.example.size()) {
            appoggio = example;
            example = e.example;
            e.example = appoggio;
        } else {
            throw new ExampleSizeException("ESEMPI di dimensioni diverse");
        }
    }

    /**
     * utilizzata per calcolare la distanza di un Example
     * 
     * @param e
     *          Example del quale si vuole calcolare la distanza
     * @return double
     *         distanza dell'Example passato in input
     */
    public double distance(Example e) {
        if (e.example.size() == this.example.size()) {
            double dist = 0;
            Iterator i = example.iterator();
            int y = 0;
            while (i.hasNext()) {
                Object o = i.next();
                if (o instanceof String) {
                    if ((o.equals(e.get(y)) == false)) {
                        dist++;
                    }
                } else if (o instanceof Double) {
                    dist = dist + Math.abs(((Double) o) - ((Double) e.get(y)));
                }
                y++;
            }
            return dist;
        } else {
            throw new ExampleSizeException("esempi di dimensioni diverse");
        }

    }

    /**
     * permette di mostare a video un oggetto della classe Example
     * 
     * @return String
     *         stringa da restituire corrispondente all'oggetto della classe Example
     *         desiderato
     */
    public String toString() {
        return example.toString();
    }

    /**
     * funzione principale della classe Example
     * 
     * @param args
     *             contiene comandi dal cmd
     * @throws FileNotFoundException
     *                               viene lanciata se il file non esiste
     */
    public static void main(String[] args) throws FileNotFoundException {
        try {
            Data trainingSet = new Data("src/provaC.dat");
            System.out.println(trainingSet);
            KNN knn = new KNN(trainingSet);
            String r;
            do {
                // read example withKeyboard
                System.out.println("Prediction:" + knn.predict());
                System.out.println("Vuoi ripetere? Y/N");
                r = Keyboard.readString();
            } while (r.toLowerCase().equals("y"));
        } catch (TrainingDataException exc) {
            System.out.println(exc.getMessage());
        }
    }
}
