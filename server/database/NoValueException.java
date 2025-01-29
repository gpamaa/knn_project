package database;

/**
 * eccezione che viene lanciata quando il tipo dichiarato delle colonne è
 * diverso dal tipo dei valori della tabella
 */
public class NoValueException extends Exception {

	public NoValueException(String msg) {
		// TODO Auto-generated constructor stub
		super(msg);
	}

}
