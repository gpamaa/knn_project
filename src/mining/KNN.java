package mining;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

import data.Data;
import example.Example;

import java.io.IOException;
import utility.*;

/**
 * collezione di esempi di apprendimento (training set)
 */
public class KNN implements Serializable {
    private Data data;

    /**
     * costruttore di classe
     * 
     * @param trainingSet collezione di esempi con variabile target annessa
     */
    public KNN(Data trainingSet) {
        this.data = trainingSet;
    }

    /**
     * restituisce il risultato della predizione sull querypoint e k dell'utente
     * 
     * @param out
     *            canale in scrittura con il server
     * @param in
     *            canale in lettura con il server
     * @return double
     *         valore della predizionne
     * @throws IOException
     *                                eccezione lanciata nel caso di errori nella
     *                                comunicazione con il client
     * @throws ClassNotFoundException
     *                                lanciata quando la classe non è stata trovatea
     * @throws ClassCastException
     *                                lanciata quando un tipo non puo essere
     *                                coonvertito in un altro
     */
    public double predict(ObjectOutputStream out, ObjectInputStream in)
            throws IOException, ClassNotFoundException, ClassCastException {
        Example e = data.readExample(out, in);
        int k = (Integer) in.readObject();
        return data.avgClosest(e, k);
    }

    /**
     * restituisce il risultato della predizione sull querypoint e k dell'utente
     * 
     * @return double valore della predizione
     */
    public double predict() {
        Example e = data.readExample();
        int k = 0;
        do {
            System.out.print("Inserisci valore k>=1:");
            k = Keyboard.readInt();
        } while (k < 1);
        return data.avgClosest(e, k);
    }

    /**
     * permette di serializzare un oggetto, salvandolo su un file
     * 
     * @param nomeFile
     *                 rappresenta il nome del file sul quale caricare i dati
     * @throws IOException
     *                     viene lanciata nel caso di errori durante la
     *                     serializzazione
     */
    public void salva(String nomeFile) throws IOException {
        FileOutputStream file = new FileOutputStream(nomeFile);
        ObjectOutputStream o1 = new ObjectOutputStream(file);

        o1.writeObject(this);
        file.close();
    }

    /**
     * permette di deserializzare un oggetto, acquisendolo da un file
     * 
     * @param nomeFile
     *                 rappresenta il nome del file dal quale acquisire i dati
     * @return KNN
     *         rappresento il KNN con i dati acquisiti dal file
     * @throws IOException
     *                                viene lanciata nel caso di errori durante la
     *                                serializzazione
     * @throws ClassNotFoundException
     *                                viene lanciata nel caso in cui la classe non
     *                                esista
     */
    public static KNN carica(String nomeFile) throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(nomeFile);
        ObjectInputStream o1 = new ObjectInputStream(file);
        KNN k1 = (KNN) o1.readObject();
        file.close();
        return k1;
    }

    /**
     * permette di mostare a video un oggetto della classe KNN
     * 
     * @return String
     *         stringa da restituire corrispondente all'oggetto della classe KNN
     *         desiderato
     */
    public String toString() {
        return data.toString();
    }
}