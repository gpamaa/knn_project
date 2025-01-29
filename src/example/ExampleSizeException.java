package example;

/**
 * eccezione lanciata nel caso di esempi con dimensione diversa
 */
public class ExampleSizeException extends RuntimeException {
    /**
     * costruttore di classe
     */
    public ExampleSizeException() {
    }

    /**
     * costruttore di classe con in input il messaggio di errore
     * 
     * @param s il messaggio di errore
     */
    public ExampleSizeException(String s) {
    }
}