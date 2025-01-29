package database;

/**
 * eccezione usata per errori del database(ad esempio utente o password
 * sbagliati)
 */
public class DatabaseConnectionException extends Exception {

	DatabaseConnectionException(String msg) {
		super(msg);
	}

	DatabaseConnectionException() {
	}
}
