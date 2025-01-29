package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import example.Example;

/**
 * rappresenta i valori contenuti in una tabella con il relativo schema
 */
public class TableData {
	private DbAccess db;
	private String table;
	private TableSchema tSchema;
	private List<Example> transSet;
	private List target;

	/**
	 * costruttore di classe inizializza db e tschema
	 * 
	 * @param db      rappresenta la configurazione del db
	 * @param tSchema rappresenta lo schema della tabella
	 * @throws SQLException                      viene lanciata nel caso di errori
	 *                                           dell SQL
	 * @throws InsufficientColumnNumberException eccezione che viene lanciata in
	 *                                           caso di mancanza di variabile
	 *                                           target o
	 *                                           mancanza di variabili indipendenti
	 */
	public TableData(DbAccess db, TableSchema tSchema) throws SQLException, InsufficientColumnNumberException {
		this.db = db;
		this.tSchema = tSchema;
		this.table = tSchema.getTableName();
		transSet = new ArrayList();
		target = new ArrayList();
		init();
	}

	private void init() throws SQLException {
		String query = "select ";
		for (Column c : tSchema) {
			query += c.getColumnName();
			query += ",";
		}
		query += tSchema.target().getColumnName();
		query += (" FROM " + table);

		Statement statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		while (rs.next()) {
			Example currentTuple = new Example(tSchema.getNumberOfAttributes());
			int i = 0;
			for (Column c : tSchema) {
				if (c.isNumber())
					currentTuple.set(rs.getDouble(i + 1), i);
				else
					currentTuple.set(rs.getString(i + 1), i);
				i++;
			}
			transSet.add(currentTuple);

			if (tSchema.target().isNumber())
				target.add(rs.getDouble(tSchema.target().getColumnName()));
			else
				target.add(rs.getString(tSchema.target().getColumnName()));
		}
		rs.close();
		statement.close();
	}

	/**
	 * restituisce la lista dei valori delle colonne indipendenti
	 * 
	 * @return transSet
	 */
	public List<Example> getExamples() {
		return transSet;
	}

	/**
	 * restituisce i valori della variabile target
	 * 
	 * @return List
	 */
	public List getTargetValues() {
		return target;
	}

	/**
	 * restituisce il minimo o massimo valore di una colonna
	 * 
	 * @param column    rappresenta la colonna da cui estrarre minimo o massimo
	 * @param aggregate indica se estrare il minimo o il massimo
	 * @return Object il valore minimo o massimo della colonna
	 * @throws SQLException                      viene lanciata nel caso di errori
	 *                                           dell SQL
	 * @throws NoValueException                  viene lanciata nel caso di
	 *                                           incoerenza dei tipi dei valori
	 *                                           rispetto allo schema dichiarato
	 * @throws InsufficientColumnNumberException
	 *                                           viene lanciata in caso di mancanza
	 *                                           della variabile target o assenza di
	 *                                           variabili indipendenti
	 */
	public Object getAggregateColumnValue(Column column, QUERY_TYPE aggregate)
			throws SQLException, NoValueException, InsufficientColumnNumberException {

		String query;
		Iterator<Column> i = tSchema.iterator();
		boolean trovato = false;

		if (!column.isNumber())
			throw new NoValueException("colonna di stringhe");
		while (i.hasNext() && !trovato) {
			Column aux = i.next();
			if (aux.getColumnName().equals(column.getColumnName()))
				trovato = true;
		}

		if (!trovato)
			throw new InsufficientColumnNumberException("colonna non presente");
		switch (aggregate) {
			case MIN:
				query = "select " + column.getColumnName() + " from " + tSchema.getTableName() + " order by "
						+ column.getColumnName() + " asc";
				break;
			case MAX:
				query = "select " + column.getColumnName() + " from " + tSchema.getTableName() + " order by "
						+ column.getColumnName() + " DESC";
				break;
			default:
				throw new NoValueException("inserire un campo valido");
		}
		Statement statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		rs.next();

		return rs.getDouble(column.getColumnName());
	}

	/**
	 * main utilizzato per testare le funzionalità della classe in versioni
	 * precedenti
	 * 
	 * 
	 * @param args
	 *             contiene i comandi del cmd
	 */
	public static void main(String[] args) {
		TableData table;
		QUERY_TYPE aggregate = QUERY_TYPE.MAX;
		try {
			table = new TableData(new DbAccess(), new TableSchema("map.provaC", new DbAccess()));
			System.out.println(table.getAggregateColumnValue(new Column("Y", "number"), aggregate));
		} catch (SQLException exc) {
		} catch (NoValueException exc) {
			System.out.println("novalue");
		} catch (InsufficientColumnNumberException exc) {
			System.out.println("insufficient");
		} catch (DatabaseConnectionException exc) {
			System.out.println("databasecon");
		}
	}
}