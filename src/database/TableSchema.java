package database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * rappresenta lo schema di una tabella(nome e tipo)
 */
public class TableSchema implements Iterable<Column> {

	private List<Column> tableSchema = new ArrayList<Column>();
	private Column target;
	private String tableName;

	/**
	 * costruttore di classe iniziializza il membro tableName e crea lo schema della
	 * tabella
	 * 
	 * @param tableName rappresenta il nome della tabella
	 * @param db        rappresenta la configurazione del DBMS
	 * @throws SQLException                      viene lanciata nel caso di errori
	 *                                           dell SQL
	 * @throws InsufficientColumnNumberException viene lanciata in caso di mancanza
	 *                                           della variabile target o variabili
	 *                                           indipendenti
	 */
	public TableSchema(String tableName, DbAccess db) throws SQLException, InsufficientColumnNumberException {
		this.tableName = tableName;

		HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();

		// http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html

		mapSQL_JAVATypes.put("CHAR", "string");
		mapSQL_JAVATypes.put("VARCHAR", "string");
		mapSQL_JAVATypes.put("LONGVARCHAR", "string");
		mapSQL_JAVATypes.put("BIT", "string");
		mapSQL_JAVATypes.put("SHORT", "number");
		mapSQL_JAVATypes.put("INT", "number");
		mapSQL_JAVATypes.put("LONG", "number");
		mapSQL_JAVATypes.put("FLOAT", "number");
		mapSQL_JAVATypes.put("DOUBLE", "number");

		DatabaseMetaData meta = db.getConnection().getMetaData();
		ResultSet res = meta.getColumns(null, null, tableName, null);

		while (res.next()) {
			if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
				if (res.isLast())
					target = new Column(
							res.getString("COLUMN_NAME"),
							mapSQL_JAVATypes.get(res.getString("TYPE_NAME")));
				else
					tableSchema.add(new Column(
							res.getString("COLUMN_NAME"),
							mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));

		}

		res.close();
		if (target == null || tableSchema.size() == 0)
			throw new InsufficientColumnNumberException("La tabella selezionata contiene meno di due colonne");

	}

	/**
	 * restituisce nome e tipo della variabile target
	 * 
	 * @return target
	 */
	public Column target() {
		return target;
	}

	/**
	 * restituisce la dimensione dello schema con le variabili indipendenti
	 * 
	 * @return int tableSchema.size()
	 */
	public int getNumberOfAttributes() {
		return tableSchema.size();
	}

	String getTableName() {
		return tableName;
	}

	/**
	 * restituisce l iteratore che scandisce lo schema
	 * 
	 * @return Iterator
	 */
	@Override
	public Iterator<Column> iterator() {
		// TODO Auto-generated method stub
		return tableSchema.iterator();
	}
}
