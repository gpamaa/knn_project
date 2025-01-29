package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestisce l'accesso al DB per la lettura dei dati di training
 * 
 * @author Map Tutor
 * 
 */

public class DbAccess {

	private final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
	private final String DBMS = "jdbc:mysql";
	private final String SERVER = "localhost";
	private final int PORT = 3306;
	private final String DATABASE = "Map";
	private final String USER_ID;
	private final String PASSWORD;

	private Connection conn;

	/**
	 * Inizializza una connessione al DB utilizzando username e password passati
	 * come parametri
	 * 
	 * @param username
	 *                 rappresenta il nome utente
	 * @param password
	 *                 rappresenta la password dell utente
	 * @throws DatabaseConnectionException
	 *                                     viene lanciata in caso di errore nel
	 *                                     database(ad esempio porta sbagliata,o
	 *                                     utente inesistente)
	 */
	public DbAccess(String username, String password) throws DatabaseConnectionException {
		this.USER_ID = username;
		this.PASSWORD = password;
		String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
				+ "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";
		try {
			conn = DriverManager.getConnection(connectionString);
		} catch (SQLException e) {
			throw new DatabaseConnectionException(e.toString());
		}
	}

	/**
	 * Inizializza una connessione al DB
	 * 
	 * @throws DatabaseConnectionException
	 *                                     viene lanciata in caso di errore nel
	 *                                     database(ad esempio porta sbagliata,o
	 *                                     utente inesistente)
	 */
	public DbAccess() throws DatabaseConnectionException {
		USER_ID = "root";
		PASSWORD = "marcoguzzo";
		String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
				+ "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";
		try {
			conn = DriverManager.getConnection(connectionString);

		} catch (SQLException e) {
			throw new DatabaseConnectionException(e.toString());
		}

	}

	/**
	 * restituisce la connessione al database
	 * 
	 * @return Connection
	 *         connessione restituita
	 */
	public Connection getConnection() {
		return conn;
	}

	/**
	 * chiude la connessione al database
	 */
	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Impossibile chiudere la connessione");
		}
	}

}
