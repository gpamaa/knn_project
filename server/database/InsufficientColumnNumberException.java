package database;

/**
 * eccezione che viene lanciata in caso di mancanza di variabile target o
 * mancanza di variabili indipendenti
 */
public class InsufficientColumnNumberException extends Exception {
	public InsufficientColumnNumberException(String msg) {
		super(msg);
	}
}
