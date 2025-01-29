package data;

/**
 * questa eccezione viene utilizzata nel caso di errori del trainingset
 */
public class TrainingDataException extends Exception {
    /**
     * costruttore di classe
     */
    public TrainingDataException() {
    }

    /**
     * costruttore di classe per mostrare a video un errore
     * 
     * @param s rappresenta la stringa di errore
     */
    public TrainingDataException(String s) {
    }
}
